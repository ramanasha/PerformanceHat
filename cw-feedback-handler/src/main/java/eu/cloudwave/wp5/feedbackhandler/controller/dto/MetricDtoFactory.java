package eu.cloudwave.wp5.feedbackhandler.controller.dto;

import java.util.List;

import eu.cloudwave.wp5.common.dto.AggregatedProcedureMetricsDto;
import eu.cloudwave.wp5.common.dto.model.ProcedureExecutionMetricDto;
import eu.cloudwave.wp5.common.model.ProcedureExecutionMetric;
import eu.cloudwave.wp5.feedbackhandler.repositories.aggregations.ProcedureMetricAggregation;

/**
 * A factory for metric related data transfer objects.
 */
public interface MetricDtoFactory {

  /**
   * Creates a {@link ProcedureExecutionMetricDto} out of a {@link ProcedureExecutionMetric}.
   * 
   * @param metric
   *          the input {@link ProcedureExecutionMetric}
   * @return the created {@link ProcedureExecutionMetricDto}
   */
  public ProcedureExecutionMetricDto create(final ProcedureExecutionMetric metric);

  /**
   * Creates a {@link ProcedureExecutionMetricDto} for each element in the given {@link List} of
   * {@link ProcedureExecutionMetric}'s.
   * 
   * @param metrics
   *          the list of metrics to be converted.
   * @return an array containing the created {@link ProcedureExecutionMetricDto}'s
   */
  public List<ProcedureExecutionMetricDto> create(final List<? extends ProcedureExecutionMetric> metrics);

  /**
   * Creates an array of {@link AggregatedProcedureMetricsDto} based on the given aggregated execution times and CPU
   * usage values.
   * 
   * @param aggregatedExecutionTimes
   *          the aggregated average execution times
   * @param aggregatedCpuUsages
   *          the aggregated average CPU usage values
   * @return an array containing the created {@link AggregatedProcedureMetricsDto}'s
   */
  public List<AggregatedProcedureMetricsDto> create(List<ProcedureMetricAggregation> aggregatedExecutionTimes, List<ProcedureMetricAggregation> aggregatedCpuUsages);

}
