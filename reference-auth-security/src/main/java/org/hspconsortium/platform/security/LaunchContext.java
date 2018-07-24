/*
 * #%L
 * hspc-security
 * %%
 * Copyright (C) 2014 - 2015 Healthcare Services Platform Consortium
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.hspconsortium.platform.security;

import java.util.HashMap;
import java.util.Map;

public final class LaunchContext {

    private Long id;

    private String launchId;

    private Map<String, Object> launchContextParams;

    public LaunchContext() {
    }

    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the launchId
     */
    public String getLaunchId() {
        return launchId;
    }

    /**
     * @param launchId the launchId to set
     */
    public void setLaunchId(String launchId) {
        this.launchId = launchId;
    }

    /**
     * @param id the parameter id to set
     * @param value the parameter value to set
     */
    public void addLaunchContextParam(String id, Object value) {
        if (this.launchContextParams == null) {
            this.launchContextParams = new HashMap<String, Object>();
        }
        launchContextParams.put(id, value);
    }

    /**
     * @param launchContextParams the launch context parameters to set
     */
    public void setLaunchContextParams(Map<String, Object> launchContextParams) {
        this.launchContextParams = launchContextParams;
    }

    public Object getParam(String key) {
        return this.launchContextParams.get(key);
    }

    public Map<String, Object> getParams() {
        return this.launchContextParams;
    }

}
