package org.eclim.plugin.performancehat.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclim.annotation.Command;
import org.eclim.command.CommandLine;
import org.eclim.command.Error;
import org.eclim.command.Options;
import org.eclim.plugin.core.command.AbstractCommand;
import org.eclim.plugin.core.util.ProjectUtils;
import org.eclim.util.file.FileOffsets;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;

@Command(
		  name = "hat_src_update",
		  options =
		    "REQUIRED p project ARG," +
		    "REQUIRED f file ARG," +
		    "OPTIONAL v validate NOARG," +
		    "OPTIONAL b build NOARG"
		)
public class SrcUpdateCommand extends AbstractCommand{

	public Object execute(CommandLine commandLine) throws Exception {
		String file = commandLine.getValue(Options.FILE_OPTION);
	    String projectName = commandLine.getValue(Options.PROJECT_OPTION);
	    IProject project = ProjectUtils.getProject(projectName, true);
	    IFile ifile = ProjectUtils.getFile(project, file);

	    // validate the src file.
	      String filename = ifile.getLocation().toOSString().replace('\\', '/');
	      FileOffsets offsets = FileOffsets.compile(filename);   
	      List<Error> errors = new ArrayList<Error>();
	      IMarker[] markers = ifile.findMarkers("eu.cloudwave.wp5.feedback.eclipse.performance.markers.PerformanceMarker", true, IResource.DEPTH_INFINITE);
	      for(IMarker m:markers){
	    	  int[] lineColumnStart = offsets.offsetToLineColumn((Integer)m.getAttribute(IMarker.CHAR_START,0));
	    	  int[] lineColumnEnd = offsets.offsetToLineColumn((Integer)m.getAttribute(IMarker.CHAR_END,0));    	  
	    	  errors.add(new Error(
	    	            m.getAttribute(IMarker.MESSAGE,""),
	    	            filename,
	    	            lineColumnStart[0],
	    	            lineColumnStart[1],
	    	            lineColumnEnd[0],
	    	            lineColumnEnd[1],
	    	            m.getAttribute(IMarker.SEVERITY,1) == IMarker.SEVERITY_WARNING));
	      }
	      
	      System.out.println("RUNS");
	      errors.add(new Error(
	    	            "Test",
	    	            filename,
	    	            2,
	    	            0,
	    	            5,
	    	            0,
	    	            true)); 
	      return errors;
	  }

}
