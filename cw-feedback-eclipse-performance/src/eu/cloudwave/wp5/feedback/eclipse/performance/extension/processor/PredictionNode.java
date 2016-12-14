package eu.cloudwave.wp5.feedback.eclipse.performance.extension.processor;

import java.util.Collection;

/**
 * A node to represent a Prediction made by the BlockTimePrediction Framework
 * Each Prediction has a name/text a predicted average Time as well as some Child nodes from which the prediction was made
 * @author Markus Knecht
 */
public interface PredictionNode {
	/**
	 * Defines if this node has a text and prediction time associated or is just a grouping node 
	 * @return true if it has data false otherwise
	 */
	public boolean isDataNode();
	
	/**
	 * The Prediction test of the node
	 * @return the text
	 */
	public String getText();
	
	/**
	 * The Prediction time of the node
	 * @return the time
	 */
	public double getPredictedTime();
	
	/**
	 * The children of the node
	 * @return the children
	 */
	public Collection<PredictionNode> getChildren();
}
