package org.mule.extension.zt.internal;

import org.mule.runtime.http.api.client.HttpClient;


/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class DBLConnection {

  private final String id;
  private final HttpClient httpClient;

  public DBLConnection(String id, HttpClient httpClient) {
    this.id = id;
    this.httpClient = httpClient;
  }

  public String getId() {
    return id;
  }

  public void invalidate() {
    // do something to invalidate this connection!
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }
}
