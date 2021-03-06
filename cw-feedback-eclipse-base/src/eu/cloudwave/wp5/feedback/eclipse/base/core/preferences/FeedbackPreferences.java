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
package eu.cloudwave.wp5.feedback.eclipse.base.core.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import eu.cloudwave.wp5.feedback.eclipse.base.core.BaseIds;
import eu.cloudwave.wp5.feedback.eclipse.base.core.BasePluginActivator;

/**
 * Provides utility methods to access base preferences and contains keys of preferences related to the feedback plug-in.
 * 
 * Keep in mind: Preferences are project independent, they are set on workspace level
 */
public class FeedbackPreferences {

  public static final String FEEDBACK_HANDLER__URL = BaseIds.ID + ".feedbackHandler.url"; //$NON-NLS-1$

  public static final String CLOUDWAVE_DASHBOARD__URL = BaseIds.ID + ".cloudWave.dashboard.url"; //$NON-NLS-1$

  /**
   * Returns the current value of the string-valued preference with the given name.
   * 
   * @param name
   *          name the name of the preference
   * @return the current value of the string-valued preference with the given name
   */
  public static String getString(final String name) {
    return getPreferenceStore().getString(name);
  }

  /**
   * Returns the preference store for the plug-in.
   * 
   * @return the preference store for the plug-in
   */
  public static IPreferenceStore getPreferenceStore() {
    return BasePluginActivator.getDefault().getPreferenceStore();
  }

}
