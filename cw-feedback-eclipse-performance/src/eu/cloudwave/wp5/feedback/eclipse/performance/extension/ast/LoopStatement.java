package eu.cloudwave.wp5.feedback.eclipse.performance.extension.ast;

import com.google.common.base.Optional;

public interface LoopStatement extends IAstNode{
	
	
	/**
	   * Determines the source of the collection that is iterated in the given foreach-loop. The source is either a
	   * parameter or a return value of a procedure (method or constructor). This information is required to fetch
	   * information about the average size of the collection from the feedback handler.
	   * 
	   * @return
	   */
	  public Optional<IAstNode> getSourceNode();
}