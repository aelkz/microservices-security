package com.microservices.apigateway.security.auth.oauth2;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class OAuth2ClientCredentialsTest {
    // @Test
    @Ignore
    public void testGetToken() throws IOException {
        Token token = new OAuth2ClientCredentials(
                System.getenv("SERVER_URL"),
                System.getenv("REALM"),
                System.getenv("CLIENT_ID"),
                System.getenv("CLIENT_SECRET"),
                System.getenv("GRANT_TYPE"),
                System.getenv("USERNAME"),
                System.getenv("PASSWORD")).getToken();

        String accessToken = token.getAccessToken();

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("API");

        httpGet.addHeader("Authorization", String.format("Bearer %s", accessToken));

        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        assertEquals("Return code should be 200", 200, response1.getStatusLine().getStatusCode());
    }
}
