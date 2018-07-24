/*
 * #%L
 * hspc-authorization-server
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

package org.hspconsortium.platform.authorization.web;

import org.hspconsortium.platform.authorization.launchcontext.LaunchContextHolder;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class LaunchContextSessionListener implements HttpSessionListener {

    @Inject
    LaunchContextHolder launchContextHolder;

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        String sessionLaunchContextId = (String)session.getAttribute(Constants.LAUNCH_CONTEXT_ID_KEY);
        if (sessionLaunchContextId != null) {
            launchContextHolder.removeLaunchContext(sessionLaunchContextId);
        }
    }
}
