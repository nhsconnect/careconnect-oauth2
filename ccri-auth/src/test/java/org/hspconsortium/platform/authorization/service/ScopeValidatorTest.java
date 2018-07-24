package org.hspconsortium.platform.authorization.service;

import org.junit.Test;
import org.mockito.Mockito;
import org.smartplatforms.oauth2.ScopeValidator;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.io.Serializable;
import java.util.Map;

public class ScopeValidatorTest {

    @Test
    public void validateScopeTest() {
        AuthorizationRequest mockAuthorizationRequest = Mockito.mock(AuthorizationRequest.class);
        ClientDetails clientDetails = Mockito.mock(ClientDetails.class);

        ScopeValidator scopeValidator = new ScopeValidator();
        scopeValidator.validateScope(mockAuthorizationRequest, clientDetails);
    }

    @Test(expected = InvalidScopeException.class)
    public void validateScopeFailureTest() {
        String invalidLaunch = "invalidLaunch";

        Map<String, Serializable> mockExtensions = Mockito.mock(Map.class);
        Mockito.when(mockExtensions.get("invalid_launch")).thenReturn(invalidLaunch);

        AuthorizationRequest mockAuthorizationRequest = Mockito.mock(AuthorizationRequest.class);
        Mockito.when(mockAuthorizationRequest.getExtensions()).thenReturn(mockExtensions);


        ClientDetails clientDetails = Mockito.mock(ClientDetails.class);

        ScopeValidator scopeValidator = new ScopeValidator();
        scopeValidator.validateScope(mockAuthorizationRequest, clientDetails);
    }

}