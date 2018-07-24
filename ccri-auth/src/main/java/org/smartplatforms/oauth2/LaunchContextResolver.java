package org.smartplatforms.oauth2;

import java.io.Serializable;
import java.util.Map;

public interface LaunchContextResolver {
	public Serializable resolve(String launchId, Map<String, String> needs) throws NeedUnmetException;
    public String[] getServiceURL();
}