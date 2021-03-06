/*******************************************************************************
 * Copyright 2015 Software Evolution and Architecture Lab, University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.cloudwave.wp5.feedback.eclipse.base.core.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.google.common.base.Optional;

import eu.cloudwave.wp5.common.error.RequestException;
import eu.cloudwave.wp5.feedback.eclipse.base.core.builders.participants.FeedbackBuilderParticipant;
import eu.cloudwave.wp5.feedback.eclipse.base.core.feedbackhandler.RequestExceptionHandler;
import eu.cloudwave.wp5.feedback.eclipse.base.infrastructure.logging.Logger;
import eu.cloudwave.wp5.feedback.eclipse.base.resources.core.java.FeedbackJavaFile;
import eu.cloudwave.wp5.feedback.eclipse.base.resources.core.java.FeedbackJavaProject;
import eu.cloudwave.wp5.feedback.eclipse.base.resources.core.java.FeedbackJavaResourceDelta;
import eu.cloudwave.wp5.feedback.eclipse.base.resources.core.java.FeedbackJavaResourceFactory;

/**
 * A Builder for the feedback plugin. Delegates the Work to a list of registered {@link FeedbackBuilderParticipant}'s.
 */
public abstract class FeedbackBuilder extends IncrementalProjectBuilder {
  /*
   * Returns implementation of FeedbackJavaResourceFactory (each plugin can use its own dependency injector in the
   * FeedbackBuilder subclass)
   */
  protected abstract FeedbackJavaResourceFactory getFeedbackJavaResourceFactory();

  /*
   * Returns implementation of FeedbackCleaner (each plugin can use its own dependency injector in the FeedbackBuilder
   * subclass)
   */
  protected abstract FeedbackCleaner getFeedbackCleaner();

  /*
   * Returns list of build participants, has to be implemented in the concrete builder (e.g. PerformanceBuilder,
   * CostBuilder)
   */
  protected abstract List<FeedbackBuilderParticipant> getParticipants();

  /**
   * {@inheritDoc}
   */
  @Override
  protected IProject[] build(final int kind, final Map<String, String> args, final IProgressMonitor monitor) throws CoreException {
    try {
      final Optional<? extends FeedbackJavaProject> javaProjectOptional = this.getFeedbackJavaResourceFactory().create(getProject());
      if (javaProjectOptional.isPresent()) {
        final FeedbackJavaProject project = javaProjectOptional.get();
        Logger.print(String.format("Triggered %S.", BuildTypes.INSTANCE.get(kind)));
        if (kind == IncrementalProjectBuilder.FULL_BUILD || kind == IncrementalProjectBuilder.CLEAN_BUILD) {
          fullBuild(project, monitor);
        }
        else {
          incrementalBuild(project, monitor);
        }
      }
    }
    catch (final RequestException e) {
      new RequestExceptionHandler().handle(getProject(), e);
    }
    return null;
  }

  /**
   * All resources in the project are built (no delta is available).
   * 
   * @throws CoreException
   *           if markers could not be correctly added or removed
   */
  private void fullBuild(final FeedbackJavaProject project, final IProgressMonitor monitor) throws CoreException {
    this.getFeedbackCleaner().cleanAll(project);
	
 	List<FeedbackBuilderParticipant> participants = this.getParticipants();
 	Set<FeedbackJavaFile> files = project.getJavaSourceFiles();
 	 	
    for (final FeedbackBuilderParticipant participant : participants) {
      participant.prepare(project, files);
    }
    try {
    	int remaining = files.size();
    	final SubMonitor subMonitor = SubMonitor.convert(monitor,remaining);

    	for(FeedbackJavaFile javaFile:files){
	  		if(subMonitor.isCanceled()) throw new OperationCanceledException();
    		subMonitor.setWorkRemaining(remaining--);
    		Optional<CompilationUnit> astRoot = javaFile.getAstRoot();
    		if(!astRoot.isPresent()) continue;
    		
    		// Vom Gescheiterten Versuch einen zweiten Ast zu erhalten um differenz basierte analysen zu machen
	  		//Optional<CompilationUnit> oldAstRoot = loadOldUnit(javaFile);
    		
    		subMonitor.newChild(1);
    		subMonitor.setTaskName("Processing feedback for "+javaFile.getName());
    		for (final FeedbackBuilderParticipant participant : participants) {
    	      participant.buildFile(project, javaFile, astRoot.get()/*, oldAstRoot.orNull()*/);
    	    }
    	}
    } finally {
    	for (final FeedbackBuilderParticipant participant : participants) {
    	      participant.cleanup(project, files);
	    }
    }   
  }

  /**
   * Only changed resources should be built (a delta is available).
   * 
   * @throws CoreException
   *           if markers could not be correctly added or removed
   */
  private void incrementalBuild(final FeedbackJavaProject project, final IProgressMonitor monitor) throws CoreException {
    final IResourceDelta resourceDelta = getDelta(getProject());
    final Optional<? extends FeedbackJavaResourceDelta> feedbackDeltaOptional = this.getFeedbackJavaResourceFactory().create(resourceDelta);
    if (feedbackDeltaOptional.isPresent()) {
      final FeedbackJavaResourceDelta delta = feedbackDeltaOptional.get();
      this.getFeedbackCleaner().cleanDelta(delta);
      
   		List<FeedbackBuilderParticipant> participants = this.getParticipants();
   		Set<FeedbackJavaFile> files = feedbackDeltaOptional.get().getChangedJavaFiles();
   	
	    for (final FeedbackBuilderParticipant participant : participants) {
	        participant.prepare(project, files);
	    }
	    try {
	    	int remaining = files.size();
	    	final SubMonitor subMonitor = SubMonitor.convert(monitor,remaining);

		  	for(FeedbackJavaFile javaFile:files){		  				  		
		  		
		  		if(subMonitor.isCanceled()) throw new OperationCanceledException();
	    		subMonitor.setWorkRemaining(remaining--);
		  		Optional<CompilationUnit> astRoot = javaFile.getAstRoot();
		  		if(!astRoot.isPresent()) continue;
		  		
		  		//Vom Gescheiterten Versuch einen zweiten Ast zu erhalten um differenz basierte analysen zu machen
		  		//Optional<CompilationUnit> oldAstRoot = loadOldUnit(javaFile);
		  		
		  		subMonitor.newChild(1);
	    		subMonitor.setTaskName("Processing feedback for "+javaFile.getName());
		  		for (final FeedbackBuilderParticipant participant : participants) {
		  	      participant.buildFile(project, javaFile, astRoot.get()/*, oldAstRoot.orNull()*/);
		  	    }
		  	}
	    }finally{
	    	for (final FeedbackBuilderParticipant participant : participants) {
		        participant.cleanup(project, files);
		    }
	    } 	
    }
   
  }
  
  /*
  public static Optional<CompilationUnit> loadOldUnit(FeedbackJavaFile javaFile) throws CoreException{
		final ASTParser parser = ASTParser.newParser(AST.JLS4);
	    parser.setKind(ASTParser.K_COMPILATION_UNIT);
	    IFileState[] history = javaFile.getHistory(new NullProgressMonitor());
	    if(history.length == 0) return Optional.absent();
	    InputStream inp = history[0].getContents();
	    Scanner scanner  = new Scanner(inp).useDelimiter("\\A");
	    String result = scanner.hasNext() ? scanner.next() : "";
	    scanner.close();
	    parser.setSource(result.toCharArray());
	    parser.setResolveBindings(true); //does not help, bindings will not be their
	    return Optional.of((CompilationUnit) parser.createAST(new NullProgressMonitor()));
	}
	*/
}
