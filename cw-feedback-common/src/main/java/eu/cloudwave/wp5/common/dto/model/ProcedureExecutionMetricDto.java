package eu.cloudwave.wp5.common.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.cloudwave.wp5.common.model.MetricType;
import eu.cloudwave.wp5.common.model.Procedure;
import eu.cloudwave.wp5.common.model.ProcedureExecution;
import eu.cloudwave.wp5.common.model.ProcedureExecutionMetric;
import eu.cloudwave.wp5.common.model.impl.MetricTypeImpl;
import eu.cloudwave.wp5.common.model.impl.ProcedureExecutionMetricImpl;

public class ProcedureExecutionMetricDto extends ProcedureExecutionMetricImpl implements ProcedureExecutionMetric {

  public ProcedureExecutionMetricDto() {
    this(null, new NullProcedureExecutionDto(), null, null);
  }

  public ProcedureExecutionMetricDto(final MetricType type, final ProcedureExecution procedureExecution, final String additionalQualifier, final Number value) {
    super(type, procedureExecution, additionalQualifier, value);
  }

  public ProcedureExecutionMetricDto(final long timestamp, final MetricType type, final Procedure procedure, final String additionalQualifier, final Number value) {
    super(timestamp, type, procedure, additionalQualifier, value);
  }

  /**
   * {@inheritDoc}
   */
  @JsonDeserialize(as = MetricTypeImpl.class)
  @Override
  public MetricType getType() {
    return super.getType();
  }

  /**
   * {@inheritDoc}
   */
  @JsonDeserialize(as = ProcedureDto.class)
  @Override
  public Procedure getProcedure() {
    return super.getProcedure();
  }

  /**
   * {@inheritDoc}
   */
  @JsonIgnore
  @Override
  public String getQualifier() {
    return super.getQualifier();
  }

  /**
   * Null implementation of {@link ProcedureExecutionDto}. Is passed as value in the default constructor of
   * {@link ProcedureExecutionMetricDto} to avoid NPE's, because the values of the passed procedure execution are
   * accessed in the super constructors.
   */
  private static class NullProcedureExecutionDto extends ProcedureExecutionDto {

    public NullProcedureExecutionDto() {
      super(new ProcedureDto(), -1);
    }

  }

}
