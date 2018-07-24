package org.smartplatforms.oauth2;

import org.smartplatforms.openid.connect.token.SmartTofuUserApprovalHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component("smartUserApprovalHandler")
public class SmartUserApprovalHandler extends SmartTofuUserApprovalHandler {

	/*
	 * 	Never consider a request for pre-approval if it still needs launch context bound
	 */
	@Override
	public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
		if (authorizationRequest.getExtensions().containsKey("external_launch_required")) {
			return authorizationRequest;
		}
		return super.checkForPreApproval(authorizationRequest, userAuthentication);
	}

}
