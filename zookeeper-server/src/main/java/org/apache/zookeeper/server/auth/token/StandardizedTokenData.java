package org.apache.zookeeper.server.auth.token;

import java.util.List;

public class StandardizedTokenData {
    private String userId;
    private List<String> groups;

    public StandardizedTokenData(String userId, List<String> groups) {
        this.userId = userId;
        this.groups = groups;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "StandardizedTokenData{" +
                "userId=" + userId +
                ", groups='" + groups + '\'' +
                '}';
    }
}
