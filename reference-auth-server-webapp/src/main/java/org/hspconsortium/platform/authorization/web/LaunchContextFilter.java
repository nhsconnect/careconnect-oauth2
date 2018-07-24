///*
// * #%L
// * hspc-authorization-server
// * %%
// * Copyright (C) 2014 - 2015 Healthcare Services Platform Consortium
// * %%
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// * #L%
// */
//
//package org.hspconsortium.platform.authorization.web;
//
//import org.hspconsortium.platform.authorization.launchcontext.LaunchContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.annotation.Resource;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//@Component("launchContextFilter")
//public class LaunchContextFilter extends OncePerRequestFilter {
//
//    @Resource(name="launchContextHolder")
//    LaunchContextHolder launchContextHolder;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        try {
//            String launchContextId = (String)session.getAttribute(Constants.LAUNCH_CONTEXT_ID_KEY);
////            launchContextHolder.put
////            launchContextHolder.setLaunchContextIds(launchContextIds);
//
//            filterChain.doFilter(request, response);
//
//        } finally {
////            session.setAttribute(Constants.LAUNCH_CONTEXT_ID_MAP_KEY, OldLaunchContextHolder.getLaunchContextIds());
////            OldLaunchContextHolder.clearLaunchContextIds();
//        }
//    }
//}
