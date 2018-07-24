package org.hspconsortium.platform.authorization.launchcontext;

import org.hspconsortium.platform.security.LaunchContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryLaunchContextHolder implements LaunchContextHolder {

    private Map<String, LaunchContext> contextMap = new HashMap<>();

    @Override
    public LaunchContext getLaunchContext(String launchContextId) {
        return contextMap.get(launchContextId);
    }

    @Override
    public LaunchContext putLaunchContext(String contextId, LaunchContext launchContext) {
        return contextMap.put(contextId, launchContext);
    }

    @Override
    public LaunchContext removeLaunchContext(String launchContextId) {
        return contextMap.remove(launchContextId);
    }
}
