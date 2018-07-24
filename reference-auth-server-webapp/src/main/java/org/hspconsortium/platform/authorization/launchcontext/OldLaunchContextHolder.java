package org.hspconsortium.platform.authorization.launchcontext;///*
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
//package org.hspconsortium.platform.authorization.launchcontext;
//
//import org.hspconsortium.platform.security.LaunchContext;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//public class OldLaunchContextHolder {
//
////    private static ThreadLocal<Set<String>> inFlightLaunchContextIdHolder = new ThreadLocal<>();
//    private static Map<String, LaunchContext> globalLaunchContextMap = new HashMap<>();
//
////    public static Set<String> getLaunchContextIds() {
////        Set<String> launchContextIds = inFlightLaunchContextIdHolder.get();
////        if (launchContextIds == null) {
////            launchContextIds = new HashSet<>();
////            inFlightLaunchContextIdHolder.set(launchContextIds);
////        }
////        return launchContextIds;
////    }
//
//    public static LaunchContext getLaunchContext(String launchId) {
//        return globalLaunchContextMap.get(launchId);
//    }
//
////    public static void setLaunchContextIds(Set<String> launchContextIds) {
////        inFlightLaunchContextIdHolder.set(launchContextIds);
////    }
////
//    public static void addLaunchContext(LaunchContext launchContext) {
//        if (launchContext != null) {
//            globalLaunchContextMap.put(launchContext.getLaunchId(), launchContext);
////            Set<String> launchContextIds = getLaunchContextIds();
////            launchContextIds.add(launchContext.getLaunchId());
//        }
//    }
//
//    public static void clearGlobalContextMap(Set<String> launchContextIds) {
//        if (launchContextIds != null) {
//            for (String contextId : launchContextIds) {
//                globalLaunchContextMap.remove(contextId);
//            }
//        }
//    }
//
////    public static void clearLaunchContextIds() {
////        inFlightLaunchContextIdHolder.remove();
////    }
//
//}
