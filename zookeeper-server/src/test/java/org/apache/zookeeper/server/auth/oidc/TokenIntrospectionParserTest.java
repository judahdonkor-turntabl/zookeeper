package org.apache.zookeeper.server.auth.oidc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.zookeeper.server.auth.oidc.TokenIntrospectionParser.AccessTokenParameterType;
import org.apache.zookeeper.server.auth.oidc.TokenIntrospectionParser.Method;
import org.junit.jupiter.api.Test;

class TokenIntrospectionParserTest {

    /**
     * Rename Test Cases Appropriately
     */
    @Test
    void constructURLUsingPathParameterTest() {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.PATH_PARAMETER,
                "access_token",
                "https://auth-server.com/tokeninfo",
                Method.GET);

        String url = tokenIntrospectionParser.constructURL("ya29.c.b0Aaekm1KrZxCpPeHv1cTV803Z7jawVmrrlphlIiaSVBmYXjhH0vWRMYorQmsvGw08RZwm6aUUbzLWEKL4LglgHVBxQaSrm68lAVEWa6O1jkfRDwbMFgZVmo79dN6Y6DX3PXYO5vHaVSG4woD7zJhyDvKIqio8otrdXJ5XosbDbm5Cq1V0QhOR4x994rwE_mf3DN5TM-2YqEa07CojitsPjNglU_C1UhdOrnwIpac5z1iMRjQUZ_EG3TtcbjnfhoTiAhAPPJxXwbzZxJqjkajzeBNukwUbU0GuOhFsguN22qzS68aAJqYqx9ZrxCBtUdzG-ncC2l_zY2wG340DBvvhqiioFrjZ8rjthMdgfrz9tzWW7MXQzpI8t00c46QWpQRIJntgg5QMxxv3ysyS_flv63tvbYBe_jXOO5Ixbx0VZXpbOIYOiaOb3Yh7U3R04eB8SM_mR55bg_JIqFxVeh0ofghVfWqIs-078SU09_ie_31b7XbesbaS_XIUdoRte99Z2iq79cS8eg3BcZ-xftFm8Woai6pz09hU58Iev56o70oiviJxw5sW-rv_FuiyuXxo578BB3m9oB7fmS7qQYn1B8o0p9hqOSqgfmxtXhJOJtYrpz4-V-0Y48qgiUmdvB8-XskI81Jep2t2wR02uuS8O3OJOSxJzj0vYVWlv2nU7-J5YQZM350jVm-M3tgtm166w1a_SB4muIWbVw05IUZb4izVjUXO64moRsjrfFQ437qpijSJxg6pvBvmdVisoc6iFvtqU_sVWXbfVVQhzlkq6F4pI25VYqV5Xc-Zrp7buu7Vd95jufVOZ4-2gU8vcU5cRBBXQeQshSkf1ZW-MYt2ogWk1fjv74k9Ur-a4m3luFIuFfpUpibnee7ZOyOeWt-ogZx8IJt23bF7cOwra4mIsXZOF59qYeQlj9q24J3pqZhZvqZ8UstkjrFv4gr7r6lb1a2sY05p358Ui048e-3ak2sruR3vFjXdSnxn_WUjzhz2prljzqVqjbb"
                .getBytes());

        assertEquals("https://auth-server.com/tokeninfo/access_token/ya29.c.b0Aaekm1KrZxCpPeHv1cTV803Z7jawVmrrlphlIiaSVBmYXjhH0vWRMYorQmsvGw08RZwm6aUUbzLWEKL4LglgHVBxQaSrm68lAVEWa6O1jkfRDwbMFgZVmo79dN6Y6DX3PXYO5vHaVSG4woD7zJhyDvKIqio8otrdXJ5XosbDbm5Cq1V0QhOR4x994rwE_mf3DN5TM-2YqEa07CojitsPjNglU_C1UhdOrnwIpac5z1iMRjQUZ_EG3TtcbjnfhoTiAhAPPJxXwbzZxJqjkajzeBNukwUbU0GuOhFsguN22qzS68aAJqYqx9ZrxCBtUdzG-ncC2l_zY2wG340DBvvhqiioFrjZ8rjthMdgfrz9tzWW7MXQzpI8t00c46QWpQRIJntgg5QMxxv3ysyS_flv63tvbYBe_jXOO5Ixbx0VZXpbOIYOiaOb3Yh7U3R04eB8SM_mR55bg_JIqFxVeh0ofghVfWqIs-078SU09_ie_31b7XbesbaS_XIUdoRte99Z2iq79cS8eg3BcZ-xftFm8Woai6pz09hU58Iev56o70oiviJxw5sW-rv_FuiyuXxo578BB3m9oB7fmS7qQYn1B8o0p9hqOSqgfmxtXhJOJtYrpz4-V-0Y48qgiUmdvB8-XskI81Jep2t2wR02uuS8O3OJOSxJzj0vYVWlv2nU7-J5YQZM350jVm-M3tgtm166w1a_SB4muIWbVw05IUZb4izVjUXO64moRsjrfFQ437qpijSJxg6pvBvmdVisoc6iFvtqU_sVWXbfVVQhzlkq6F4pI25VYqV5Xc-Zrp7buu7Vd95jufVOZ4-2gU8vcU5cRBBXQeQshSkf1ZW-MYt2ogWk1fjv74k9Ur-a4m3luFIuFfpUpibnee7ZOyOeWt-ogZx8IJt23bF7cOwra4mIsXZOF59qYeQlj9q24J3pqZhZvqZ8UstkjrFv4gr7r6lb1a2sY05p358Ui048e-3ak2sruR3vFjXdSnxn_WUjzhz2prljzqVqjbb",
                url);
    }
}