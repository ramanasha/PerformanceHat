package eu.cloudwave.wp5.feedback.eclipse.performance.extension.example;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import eu.cloudwave.wp5.common.util.Numbers;
import eu.cloudwave.wp5.common.util.TimeValues;
import eu.cloudwave.wp5.feedback.eclipse.base.infrastructure.template.TemplateHandler;
import eu.cloudwave.wp5.feedback.eclipse.base.resources.markers.MarkerAttributes;
import eu.cloudwave.wp5.feedback.eclipse.performance.Ids;
import eu.cloudwave.wp5.feedback.eclipse.performance.core.builders.participants.ProcedureExecutionData;
import eu.cloudwave.wp5.feedback.eclipse.performance.core.markers.PerformanceMarkerTypes;
import eu.cloudwave.wp5.feedback.eclipse.performance.core.properties.PerformanceFeedbackProperties;
import eu.cloudwave.wp5.feedback.eclipse.performance.extension.ProgrammMarker;
import eu.cloudwave.wp5.feedback.eclipse.performance.extension.ProgrammMarkerContext;
import eu.cloudwave.wp5.feedback.eclipse.performance.extension.ast.LoopStatement;
import eu.cloudwave.wp5.feedback.eclipse.performance.extension.visitor.ProgrammMarkerVisitor;
import eu.cloudwave.wp5.feedback.eclipse.performance.infrastructure.config.PerformanceConfigs;

//TODO: DOES NOT WORK FIND OUT WHY
public class CriticalLoopProgrammMarker implements ProgrammMarker, BlockTimeCollectorCallback{

	  private static final String PROCEDURE_EXECUTIONS = "procedureExecutions";
	  private static final String AVG_TIME_PER_ITERATION = "avgTimePerIteration";
	  private static final String AVG_INTERATIONS = "avgInterations";
	  private static final String AVG_TOTAL = "avgTotal";
	  private static final String LOOP = "loop";
	  private static final int DECIMAL_PLACES = 3;
	  private static final String MESSAGE_PATTERN = "Critical Loop: Average Total Time is %s (Average Iterations: %s).";
	  private static final String COLLECTION_SIZE_TAG = "CollectionSize";
	  static final String AVG_EXEC_TIME_TAG = "AvgExcecutionTime";

	  @Override
	  public List<String> getRequiredTags() {
		  	return Lists.asList(COLLECTION_SIZE_TAG,AVG_EXEC_TIME_TAG, new String[]{});
	  }

	public static void createCriticalLoopMarker(LoopStatement loop, TemplateHandler template, double averageSize, double avgExecTimePerIteration, List<ProcedureExecutionData> procedureExecutionTimes ){
		  final Double avgTotalExecTime = averageSize * avgExecTimePerIteration;
          final String avgIterationsText = new Double(Numbers.round(averageSize, DECIMAL_PLACES)).toString();
          final String avgExecTimePerIterationText = TimeValues.toText(avgExecTimePerIteration, DECIMAL_PLACES);
          final String avgTotalExecTimeText = TimeValues.toText(avgTotalExecTime, DECIMAL_PLACES);
          final String msg = String.format(MESSAGE_PATTERN, avgTotalExecTimeText, avgIterationsText);
          final Map<String, Object> context = Maps.newHashMap();
          context.put(AVG_TOTAL, avgTotalExecTimeText);
          context.put(AVG_INTERATIONS, avgIterationsText);
          context.put(AVG_TIME_PER_ITERATION, avgExecTimePerIterationText);
          context.put(PROCEDURE_EXECUTIONS,procedureExecutionTimes);
          final String desc = template.getContent(LOOP, context);
          final Map<String, Object> additionalAttributes = Maps.newHashMap();
		  additionalAttributes.put(MarkerAttributes.DESCRIPTION, desc);
		  loop.markWarning(Ids.PERFORMANCE_MARKER,PerformanceMarkerTypes.COLLECTION_SIZE,msg, additionalAttributes);
	  }
	 
	 
	  //Same as BlockTimeMeasurer, but react's only on loops, if we would like predictions for Methods, then we could just add them here
	  //But then Hotspot could fastly get irrelevant, basically it would be easy todo everithing here
	  @Override
	  public ProgrammMarkerVisitor createFileVisitor(final ProgrammMarkerContext rootContext) {
		  return new ProgrammMarkerVisitor() {
				@Override
				public ProgrammMarkerVisitor visit(LoopStatement loop) {
					  Double averageSize = LoopUtils.findNumOfIterations(loop, rootContext);
					  if(averageSize == null) return CONTINUE;
					  if(averageSize == 0) return SKIP_CHILDS;
					  return new LoopBlockTimeCollector(CriticalLoopProgrammMarker.this,rootContext, averageSize,loop);
			     }
		  };
	}

	@Override
	public ProcedureExecutionData loopBlockMeasured(double avgExecTimePerIteration, double avgSize, List<ProcedureExecutionData> procedureExecutionTimes, LoopStatement loop, ProgrammMarkerContext context) {
		final double threshold = context.getProject().getFeedbackProperties().getDouble(PerformanceFeedbackProperties.TRESHOLD__LOOPS, PerformanceConfigs.DEFAULT_THRESHOLD_LOOPS);
		if (avgExecTimePerIteration >= threshold) CriticalLoopProgrammMarker.createCriticalLoopMarker(loop,context.getTemplateHandler(),avgSize,avgExecTimePerIteration,procedureExecutionTimes);
		return null;
	}

	
	  
}
