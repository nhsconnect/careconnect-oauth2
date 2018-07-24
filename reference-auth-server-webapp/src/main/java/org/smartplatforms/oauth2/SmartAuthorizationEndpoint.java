package org.smartplatforms.oauth2;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.openid.connect.config.ConfigurationPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@SessionAttributes("authorizationRequest")
public class SmartAuthorizationEndpoint extends AuthorizationEndpoint implements
		InitializingBean {

	@Autowired
	private SmartLaunchContextResolver smartLaunchContextResolver;

	@Autowired
	private ConfigurationPropertiesBean configBean;

	@Autowired
	private ClientDetailsEntityService clientService;

	@Autowired
	LaunchOrchestrationReceiveEndpoint launchOrchestration;


	private static Logger logger = LoggerFactory
			.getLogger(SmartAuthorizationEndpoint.class);

	private String getCurrentRequestUrl(AuthorizationRequest r) {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = sra.getRequest();
		String ret = request.getQueryString();
		if (!request.getParameterMap().containsKey("scope")){
			ret += "&scope=" + Joiner.on(" ").join(r.getScope());
		}
		return ret;
	}

	@RequestMapping(value = "/authorize")
	@Override
	public ModelAndView authorize(Map<String, Object> model,
								  @RequestParam Map<String, String> parameters,
								  SessionStatus sessionStatus, Principal principal) {

		model.remove("authorizationRequest");
		ModelAndView mv = super.authorize(model, parameters, sessionStatus,
				principal);

		AuthorizationRequest authorizationRequest = (AuthorizationRequest) model
				.get("authorizationRequest");

		// verify launch is valid
		if(authorizationRequest != null && authorizationRequest.getExtensions().containsKey("invalid_aud")){
			throw new InvalidClientException("Invalid aud, verify the iss is registered with the auth server.");
		}

		// If launch context is needed, redirect to an external service to
		// go and get it
		if (authorizationRequest != null && authorizationRequest.getExtensions().containsKey(
				"external_launch_required")) {

			logger.warn("About to redirect to context resolver UI");

			String contextRequested = "no_context";
			if (parameters.containsKey("scope")) {
				if (containsScope(parameters.get("scope"), "launch/patient")){
					contextRequested = "patient_context";
				} else if (containsScope(parameters.get("scope"), "launch/location")) {
					contextRequested = "location_context";
				} else if (containsScope(parameters.get("scope"), "launch/encounter")) {
					contextRequested = "encounter_context";
				}
			}

			//TODO future: add "launch/encounter" & "launch/location" pickers
			// Could make Patient Picker a generic Picker based on the contextRequested
			if (contextRequested.equalsIgnoreCase("patient_context")) {

				ClientDetailsEntity client = clientService.loadClientByClientId(authorizationRequest.getClientId());
				String resolveParams = "/resolve/";
				resolveParams += doubleEncode(contextRequested) + "/against/";
				resolveParams += doubleEncode((String) mv.getModel().get("aud")) + "/for/";
				resolveParams += doubleEncode(client.getClientName()) + "/then/";
				resolveParams += doubleEncode(
						(configBean.getIssuer().endsWith("/") ? configBean.getIssuer() : configBean.getIssuer() + "/")
								+ "authorize?" + getCurrentRequestUrl(authorizationRequest));

				String url = null;
				try {
					url = smartLaunchContextResolver.getResolveEndpoint() + "?path=" + URLEncoder.encode(resolveParams, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return new ModelAndView(new RedirectView(url));

			//Standalone launch with no additional context requested
			} else if (contextRequested.equalsIgnoreCase("no_context")) {
				Map<String, Object> jsonMap = new HashMap<>();
				jsonMap.put("client_id", parameters.get("client_id"));
				if (parameters.containsKey("parameters")) {
					jsonMap.put("parameters", parameters.get("parameters"));
				} else {
					jsonMap.put("parameters", new HashMap<String, Object>());
				}
				Map<String, Object> launchContext = launchOrchestration.createLaunchContext(new Gson().toJson(jsonMap), null);

				String url = (configBean.getIssuer().endsWith("/") ? configBean.getIssuer() : configBean.getIssuer() + "/") + "authorize?" + getCurrentRequestUrl(authorizationRequest);
				url = url.replace("scope=", "launch=" + (String)launchContext.get("launch_id") + "&scope=");
				return new ModelAndView(new RedirectView(url));
			}
		}

		// Plan: for SMART requests that need patient-level context, redirect to
		// a patient-picker, passing CSRF token
		// For SMART request that don't need patient-level context, redirect to
		// a simplified approval screen
		// be sure to have automatic approval of the trusted context-picking
		// app, so it always knows the current user.

		return mv;
	}

	private String doubleEncode(String s) {
		try {
			return URLEncoder.encode(URLEncoder.encode(s, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	private boolean containsScope(String scopeParam, String checkScope) {
		String[] scopes = scopeParam.split(" ");
		for (String scope : scopes) {
			if (scope.trim().equalsIgnoreCase(checkScope.trim())) {
				return true;
			};
		}
		return false;
	}

	@RequestMapping(value = "/authorize", method = RequestMethod.POST, params = OAuth2Utils.USER_OAUTH_APPROVAL)
	@Override
	public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model,
							  SessionStatus sessionStatus, Principal principal) {
		return super.approveOrDeny(approvalParameters, model, sessionStatus, principal);
	}
}
