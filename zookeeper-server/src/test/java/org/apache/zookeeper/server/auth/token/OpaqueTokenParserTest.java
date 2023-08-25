package org.apache.zookeeper.server.auth.token;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpaqueTokenParserTest {

    @Test
    void getOidcProviderFromZookeeperConfigurationFile() {
        OpaqueTokenParser opaqueTokenParser =new OpaqueTokenParser();
        String tokenProvider = opaqueTokenParser.getOidcProviderFromZookeeperConfigurationFile();
        Assertions.assertEquals(tokenProvider,"google");
    }
}