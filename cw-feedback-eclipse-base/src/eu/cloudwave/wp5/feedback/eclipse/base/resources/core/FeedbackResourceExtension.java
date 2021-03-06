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
package eu.cloudwave.wp5.feedback.eclipse.base.resources.core;

import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * This interface contains methods that are shared by all implementors of sub-interfaces of {@link FeedbackResource}.
 * The respective implementation is provided by {@link FeedbackResourceExtensionImpl}.
 * 
 * This is required, because implementors of sub-interfaces of {@link FeedbackResource} cannot inherit from a shared
 * implementor of {@link FeedbackResource}, because they inherit from other respective base implementation (and Java
 * does not support multiple inheritance).
 * 
 * Therefore these sub-interfaces of {@link FeedbackResource} can delegate the execution of the methods specified here
 * to an instance of {@link FeedbackResourceExtensionImpl}.
 */
public interface FeedbackResourceExtension {

  /**
   * Deletes all feedback markers on the current {@link IResource} and all its child {@link IResource}'s.
   * 
   * @throws CoreException
   *           if one or more markers could not be deleted
   */
  public void deleteMarkers() throws CoreException;

  /**
   * Returns all markers with the given id.
   * 
   * @param id
   *          the id of the markers to be found
   * @return a {@link Set} containing all markers with the given id
   * @throws CoreException
   *           if the resource does not exist or is not open
   */
  public Set<IMarker> findMarkers(String id);

}
