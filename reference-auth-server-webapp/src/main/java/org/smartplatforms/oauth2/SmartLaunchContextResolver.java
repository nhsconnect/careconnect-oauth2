package org.smartplatforms.oauth2;

import org.hspconsortium.platform.authorization.launchcontext.LaunchContextHolder;
import org.hspconsortium.platform.security.LaunchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

//@Component
public class SmartLaunchContextResolver implements LaunchContextResolver {


	private static Logger log = LoggerFactory
			.getLogger(SmartLaunchContextResolver.class);
	private LaunchContextHolder launchContextHolder;

	private String[] fhirEndpoint;
	private String resolveEndpoint;
	private String username;
	private String password;


	@Override
	public Serializable resolve(String launchId, Map<String,String> needs) throws NeedUnmetException {
		log.info("KGM launchId = " + launchId);
		if (needs != null) log.info("Needs "+needs.toString());
		LaunchContext launchContext = launchContextHolder.getLaunchContext(launchId);
		if (launchContext == null) {
			log.warn("LaunchContext not found ");
		}
		HashMap<String,String> params = new HashMap<>();
		// TODO check for mismatched needs and throw an exception if found
		// TODO verify that the use who created this context is the same as the
		//      currently authenticated user.

		for (Entry<String, Object> need : launchContext.getParams().entrySet()) {
			String k = need.getKey();
			if (launchContext.getParams().get(k) != null){
				params.put(k, launchContext.getParams().get(k).toString());
			}
		}

		return params;
	}

	@Override
	public String[] getServiceURL() {
		return getFhirEndpoint();
	}

	public LaunchContextHolder getLaunchContextHolder() {
		return launchContextHolder;
	}

	public void setLaunchContextHolder(LaunchContextHolder launchContextHolder) {
		this.launchContextHolder = launchContextHolder;
	}

	public String[] getFhirEndpoint() {
		return fhirEndpoint;
	}

	public void setFhirEndpoint(String[] fhirEndpoint) {
		this.fhirEndpoint = fhirEndpoint;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getResolveEndpoint() {
		return resolveEndpoint;
	}

	public void setResolveEndpoint(String resolveEndpoint) {
		this.resolveEndpoint = resolveEndpoint;
	}

}
