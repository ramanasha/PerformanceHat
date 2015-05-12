package eu.cloudwave.wp5.feedback.eclipse.base.resources.core;

import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;

import eu.cloudwave.wp5.feedback.eclipse.base.core.BaseIds;
import eu.cloudwave.wp5.feedback.eclipse.base.core.FeedbackProperties;
import eu.cloudwave.wp5.feedback.eclipse.base.resources.base.AbstractProjectDecorator;

/**
 * Implementation of {@link FeedbackProject}. Acts as a decorator for the wrapped {@link IProject}.
 */
public class FeedbackProjectImpl extends AbstractProjectDecorator implements FeedbackProject {

  private static final String EMPTY = "";

  private final FeedbackResourceExtension feedbackResourceExtension;

  protected FeedbackProjectImpl(final IProject project, final FeedbackResourceExtensionFactory extensionFactory) {
    super(project);
    this.feedbackResourceExtension = extensionFactory.create(project);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteMarkers() throws CoreException {
    this.feedbackResourceExtension.deleteMarkers();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<IMarker> findMarkers(final String id) {
    return this.feedbackResourceExtension.findMarkers(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IEclipsePreferences getFeedbackProperties() {
    final IScopeContext context = new ProjectScope(project);
    return context.getNode(BaseIds.ID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAccessToken() {
    return getFeedbackProperties().get(FeedbackProperties.FEEDBACK_HANDLER__ACCESS_TOKEN, EMPTY);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getApplicationId() {
    return getFeedbackProperties().get(FeedbackProperties.FEEDBACK_HANDLER__APPLICATION_ID, EMPTY);
  }

}
