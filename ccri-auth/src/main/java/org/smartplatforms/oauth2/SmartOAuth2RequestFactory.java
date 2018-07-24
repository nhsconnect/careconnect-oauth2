package org.smartplatforms.oauth2;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.openid.connect.request.ConnectOAuth2RequestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

@Service
public class SmartOAuth2RequestFactory extends ConnectOAuth2RequestFactory {

    private Gson gson = new Gson();

	private static Logger log = LoggerFactory
			.getLogger(SmartOAuth2RequestFactory.class);

    @Autowired
	private LaunchContextResolver launchContextResolver;

	Predicate<String> isLaunchContext = new Predicate<String>() {
		@Override
		public boolean apply(String input) {
			return input.startsWith("launch");
		}
	};

	@Autowired
	public SmartOAuth2RequestFactory(
			ClientDetailsEntityService clientDetailsService) {
		super(clientDetailsService);
	}

	@Override
	public AuthorizationRequest createAuthorizationRequest(Map<String, String> inputParams) {
		AuthorizationRequest ret = super.createAuthorizationRequest(inputParams);

		HashMap<String, String> launchReqs = new HashMap<String, String>();
		
		for (Entry<String, String> e : FluentIterable.from(ret.getScope())
				.filter(isLaunchContext).transform(toMapEntry)) {
			launchReqs.put(e.getKey(), e.getValue());
		}

		boolean requestingLaunch = launchReqs.size() > 0;

        launchReqs.remove("launch");

        String launchId = ret.getRequestParameters().get("launch");
        String aud = ret.getRequestParameters().get("aud");
        log.debug("aud = "+aud);
        String[] serviceURLs = launchContextResolver.getServiceURL();

        if (aud != null && aud.endsWith("/")) {
            aud = aud.substring(0, aud.length() - 1);
        }

		boolean validAud = false;
		for (String serviceURL : serviceURLs) {
			// 	log.warn("serviceURL = "+serviceURL);
			if (serviceURL != null && serviceURL.endsWith("/")) {
				serviceURL = serviceURL.substring(0, serviceURL.length() - 1);
			}
			// KGM work around to get around error
			if (aud != null) {
				log.info("aud = "+aud + " ServiceUrl = "+serviceURL);

			}
			if ((aud != null && aud.startsWith(serviceURL))) {
				validAud = true;
			}
		}

        if (!validAud) {
            ret.getExtensions().put("invalid_aud", "Incorrect service URL (aud): " + aud);
        } else {
            if (launchId != null) {
                try {
                    @SuppressWarnings("unchecked")
                    HashMap<String,String> params = (HashMap<String, String>)launchContextResolver.resolve(launchId, launchReqs);

                    ret.getExtensions().put("launch_context", gson.toJson(params));
                } catch (NeedUnmetException e1) {
                    ret.getExtensions().put("invalid_launch", "Couldn't resolve launch id: " + launchId);
                }
            } else if (requestingLaunch) { // asking for launch, but no launch ID provided
                ret.getExtensions().put("external_launch_required", launchReqs);
            }
        }

		ret.setScope(Sets.difference(ret.getScope(),
                FluentIterable.from(ret.getScope()).filter(isLaunchContext)
                        .toSet()));

		if (launchId != null) {
			Set<String> plusLaunch = new HashSet<String>(ret.getScope());
			plusLaunch.add("launch");
			ret.setScope(plusLaunch);
		}
		
		return ret;
	}

	Function<String, String> withRequestedValue = new Function<String, String>() {

		@Override
		public String apply(String input) {
			// TODO Auto-generated method stub
			return null;
		}

	};

	Function<String, Entry<String, String>> toMapEntry = new Function<String, Entry<String, String>>() {
		@Override
		public Entry<String, String> apply(String input) {
			String[] parts = input.split(Pattern.quote(":"), 2);
			if (parts.length == 1) {
				return new AbstractMap.SimpleEntry<String, String>(parts[0],
						null);
			}
			return new AbstractMap.SimpleEntry<String, String>(parts[0],
					parts[1]);

		}
	};

}
