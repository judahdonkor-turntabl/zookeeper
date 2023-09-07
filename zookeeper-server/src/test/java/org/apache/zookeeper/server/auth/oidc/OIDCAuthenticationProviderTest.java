package org.apache.zookeeper.server.auth.oidc;

import org.apache.zookeeper.server.auth.oidc.OIDCAuthenticationProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class OIDCAuthenticationProviderTest {

    private final OIDCAuthenticationProvider oidcAuthenticationProvider = new OIDCAuthenticationProvider();

    @ParameterizedTest
    @ValueSource(strings = {"uid:userOne", "gid:groupOne"})
    void isValidSHouldReturnTrueWithValidIds(String input){
        assertTrue(oidcAuthenticationProvider.isValid(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"uuid.userTwo", "gid:group:Two", ":", "gid:", ":groupTwo"})
    void isValidSHouldReturnFalseWithInvalidIds(String input){
        assertFalse(oidcAuthenticationProvider.isValid(input));
    }

    @Test
    void matchesWithIdenticalIds() {
        String id = "uid:userOne";
        String aclExpr = "uid:userOne";

        assertTrue(oidcAuthenticationProvider.matches(id, aclExpr));
    }

    @Test
    void matchesWithUnidenticalIds() {
        String id = "uid:userOne";
        String aclExpr = "gid:groupOne";

        assertFalse(oidcAuthenticationProvider.matches(id, aclExpr));
    }
}