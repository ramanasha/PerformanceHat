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
package eu.cloudwave.wp5.common.model.impl;

import java.util.Map;

import eu.cloudwave.wp5.common.model.Annotation;

public class AnnotationImpl implements Annotation {

  private String name;

  private Map<String, Object> members;

  public AnnotationImpl(String name, Map<String, Object> members) {
    this.name = name;
    this.members = members;
  }

  @Override
  public Map<String, Object> getMembers() {
    return this.members;
  }

  @Override
  public Object getMember(String key) {
    return this.members.get(key);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {

    String members = "";

    for (Map.Entry<String, Object> entry : getMembers().entrySet()) {
      members += entry.getKey() + ":" + entry.getValue() + ", ";
    }

    return "@" + getName() + "[" + members + "]";
  }
}
