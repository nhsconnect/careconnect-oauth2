/*
 * #%L
 * hspc-auth
 * %%
 * Copyright (C) 2014 - 2015 Healthcare Services Platform Consortium
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.hspconsortium.platform.authentication;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LaunchAuthenticationEndpoint {

    @Value("${hspc.platform.persona.oauthUserLoginEndpointURL}")
    String oauthUserLoginEndpointURL;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = "application/json", produces ="application/json")
    public void handleLaunchRequest(HttpServletRequest request, HttpServletResponse response, @RequestBody String jsonString) {

        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();

        JsonElement usernameJSON = json.get("username");
        String username = null;
        if (usernameJSON != null) {
            username = usernameJSON.getAsString();
        }
        JsonElement passwordJSON = json.get("password");
        String password = null;
        if (passwordJSON != null) {
            password = passwordJSON.getAsString();
        }

        List<Cookie> cookies = authenticateUser(username, password);
        Cookie cookie = cookies.get(0);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("SET-COOKIE", "JSESSIONID=" + cookie.getValue() + "; Path=" + cookie.getPath() + "; HttpOnly");
    }

    private List<Cookie> authenticateUser(String username, String password) {
        HttpPost postRequest = new HttpPost(oauthUserLoginEndpointURL);
        postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        postRequest.addHeader("Connection", "Keep-Alive");

        try {
            List<NameValuePair> formData = new ArrayList<>();
            formData.add(new BasicNameValuePair("j_username", username));
            formData.add(new BasicNameValuePair("j_password", password));
            formData.add(new BasicNameValuePair("submit", "Sign in"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formData);
            postRequest.setEntity(entity);

        } catch (UnsupportedEncodingException uee_ex) {
            throw new RuntimeException(uee_ex);
        }

        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useSSL().build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
        HttpClientBuilder builder = HttpClientBuilder.create();
        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        builder.setSSLSocketFactory(sslConnectionFactory);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslConnectionFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
        builder.setConnectionManager(ccm);


        CloseableHttpClient httpClient = builder.setRedirectStrategy(new DefaultRedirectStrategy() {

            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
                boolean isRedirect=false;
                try {
                    isRedirect = super.isRedirected(request, response, context);
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == HttpServletResponse.SC_MOVED_PERMANENTLY ||
                            responseCode == HttpServletResponse.SC_MOVED_TEMPORARILY) {
                        return true;
                    }
                }
                return false;
            }
        }).build();

        HttpClientContext context = HttpClientContext.create();
        try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(postRequest, context)) {
            if (closeableHttpResponse.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
                HttpEntity rEntity = closeableHttpResponse.getEntity();
                String responseString = EntityUtils.toString(rEntity, StandardCharsets.UTF_8);
                throw new RuntimeException(String.format("Invalid Credentials\n" +
                                "Response Status : %s .\nResponse Detail :%s."
                        , closeableHttpResponse.getStatusLine()
                        , responseString));
            }
            CookieStore cookieStore = context.getCookieStore();

            return cookieStore.getCookies();
        } catch (IOException io_ex) {
            throw new RuntimeException(io_ex);
        } finally {
            try {
                httpClient.close();
            }catch (IOException e) {
            }
        }

    }

}
