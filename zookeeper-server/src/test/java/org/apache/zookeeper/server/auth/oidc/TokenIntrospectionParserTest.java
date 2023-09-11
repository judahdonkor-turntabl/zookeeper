package org.apache.zookeeper.server.auth.oidc;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TokenIntrospectionParserTest {
    private TokenIntrospectionParser parser;
    private HttpURLConnection mockedConnection;

    @Before
    public void setUp() {
        parser = new TokenIntrospectionParser();
        mockedConnection = Mockito.mock(HttpURLConnection.class);
    }

    @Test
    public void testParseValidToken() throws IOException {
        // Generate a valid google access token and assign it to the validToken String
        String validToken = "valid-access-token";
        String mockedResponse = "{\"sub\":\"user123\",\"scope\":\"group1 group2\"}";

        when(mockedConnection.getResponseCode()).thenReturn(200);
        when(mockedConnection.getInputStream()).thenReturn(
                new ByteArrayInputStream(mockedResponse.getBytes(StandardCharsets.UTF_8)));

        URL mockedUrl = Mockito.mock(URL.class);
        when(mockedUrl.openConnection()).thenReturn(mockedConnection);

        AccessToken accessToken = parser.parse(validToken.getBytes(StandardCharsets.UTF_8));

        assertEquals("user123", accessToken.getUserID());
        assertEquals(2, accessToken.getGroups().size());
        assertTrue(accessToken.getGroups().contains("group1"));
        assertTrue(accessToken.getGroups().contains("group2"));

        verify(mockedConnection).setRequestMethod("GET");
        verify(mockedConnection).getResponseCode();
        verify(mockedConnection).getInputStream();
        verify(mockedConnection).disconnect();
    }

    @Test
    public void testParseInvalidToken() throws IOException {
        String invalidToken = "invalid-access-token";

        when(mockedConnection.getResponseCode()).thenReturn(400); // Simulate an invalid token response

        URL mockedUrl = Mockito.mock(URL.class);
        when(mockedUrl.openConnection()).thenReturn(mockedConnection);

        AccessToken accessToken = parser.parse(invalidToken.getBytes(StandardCharsets.UTF_8));

        assertNull(accessToken);

        verify(mockedConnection).setRequestMethod("GET");
        verify(mockedConnection).getResponseCode();
    }


}