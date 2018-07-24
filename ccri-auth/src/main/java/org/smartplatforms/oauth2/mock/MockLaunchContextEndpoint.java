package org.smartplatforms.oauth2.mock;

import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.PlainJWT;
import net.minidev.json.JSONObject;
import org.mitre.openid.connect.config.ConfigurationPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartplatforms.oauth2.SmartLaunchContextResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Configuration
@Conditional(MockEnabledCondition.class)
@Controller
public class MockLaunchContextEndpoint {

	private static String UTF8 = StandardCharsets.UTF_8.name();

	@Value("${mock.endpoints.enabled}")
	private boolean mockEnabled;

	@Autowired
	private SmartLaunchContextResolver smartLaunchContextResolver;

	@Autowired
	private ConfigurationPropertiesBean configBean;

	private static Logger logger = LoggerFactory
			.getLogger(MockLaunchContextEndpoint.class);

	@RequestMapping(value = "/mock/launch", method = RequestMethod.GET, params = {"aud","context_params","patients", "show_patient_id", "launch_uri"})
	public ModelAndView launch(@RequestParam String aud, @RequestParam String context_params, @RequestParam String patients, @RequestParam String launch_uri, @RequestParam String show_patient_id) throws UnsupportedEncodingException {

		if (!mockEnabled) {
			throw new EndpointDisabledException();
		}

		logger.warn("About to redirect to context resolver UI");

		String issuerContext = (configBean.getIssuer().endsWith("/") ? configBean.getIssuer() : configBean.getIssuer() + "/");

		if (patients.equalsIgnoreCase("none")) {
			String url = issuerContext + "mock/after_picker?iss=";
			url += URLDecoder.decode(aud, UTF8) + "&launch_uri=";
			url += URLDecoder.decode(launch_uri, UTF8) + "&context_params=";
			url += URLDecoder.decode(context_params, UTF8) + "&patient_id=";
			url += patients;

			return new ModelAndView(new RedirectView(url));
		} else {
			String resolveParams = "/resolve/launch/";
			resolveParams += URLEncoder.encode(aud, UTF8) + "/for/";
			resolveParams += URLEncoder.encode(launch_uri, UTF8) + "/with/";
			resolveParams += URLEncoder.encode(context_params, UTF8) + "/and/";
			resolveParams += URLEncoder.encode(patients, UTF8) + "/show/";
			resolveParams += URLEncoder.encode(show_patient_id, UTF8) + "/then/";
			resolveParams += doubleEncode(issuerContext + "mock/after_picker");

			String url = null;
			try {
				url = smartLaunchContextResolver.getResolveEndpoint() + "?path=" + URLEncoder.encode(resolveParams, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView(new RedirectView(url));
		}
	}

	@RequestMapping(value = "/mock/after_picker", method = RequestMethod.GET, params = {"patient_id","iss","launch_uri","context_params"})
	public void afterPicker(HttpServletResponse response, @RequestParam String patient_id, @RequestParam String launch_uri,
							@RequestParam String iss, @RequestParam String context_params) throws UnsupportedEncodingException, ParseException {

		Map<String, String> jwtType = new HashMap<>();
		jwtType.put("typ", "JWT");
		jwtType.put("alg", "none");

		Map<String, String> launchContext = new HashMap<>();
		if (!patient_id.equalsIgnoreCase("none")) {
			launchContext.put("patient", patient_id);
		}
		if (!context_params.equalsIgnoreCase("none")) {
			context_params = URLDecoder.decode(context_params, UTF8);

			StringTokenizer st = new StringTokenizer(context_params, ",");
			while (st.hasMoreTokens()) {
				String[] namedParams = st.nextToken().split("=");
				launchContext.put(namedParams[0], namedParams[1]);
			}
		}

		PlainJWT launchId = new PlainJWT(Base64URL.encode(new JSONObject(jwtType).toJSONString().getBytes()),
				Base64URL.encode(new JSONObject(launchContext).toJSONString().getBytes()));

		// build the response redirect
		response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
		String redirect = URLDecoder.decode(launch_uri, UTF8) +
				"?launch=" + URLEncoder.encode(launchId.serialize(), UTF8) +
				"&iss=" + iss;
		response.setHeader("Location", redirect);
	}

	private String doubleEncode(String s) {
		try {
			return URLEncoder.encode(URLEncoder.encode(s, UTF8), UTF8);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@ExceptionHandler(EndpointDisabledException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleAuthorizationException(HttpServletResponse response, Exception e) throws IOException {
		response.getWriter().write(e.getMessage());
	}

}
