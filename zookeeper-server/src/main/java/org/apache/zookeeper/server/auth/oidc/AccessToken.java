package org.apache.zookeeper.server.auth.oidc;

import java.util.Set;

/**
 * <h2>Standard Structure For Access Token After Extraction</h2>
 */
public class AccessToken {

  private String userID;
  private Set<String> groups;

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public Set<String> getGroups() {
    return groups;
  }

  public void setGroups(Set<String> groups) {
    this.groups = groups;
  }
}
