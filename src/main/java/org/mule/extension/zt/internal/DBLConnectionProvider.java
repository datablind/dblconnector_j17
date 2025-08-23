package org.mule.extension.zt.internal;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.meta.ExternalLibraryType;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.annotation.ExternalLib;
import org.mule.runtime.api.lifecycle.Startable;
import org.mule.runtime.api.lifecycle.Stoppable;
import javax.inject.Inject;

import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpClientConfiguration;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.mule.runtime.http.api.domain.entity.ByteArrayHttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequestBuilder;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class (as it's name implies) provides connection instances and the funcionality to disconnect and validate those
 * connections.
 * <p>
 * All connection related parameters (values required in order to create a connection) must be
 * declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares that connections resolved by this provider
 * will be pooled and reused. There are other implementations like {@link CachedConnectionProvider} which lazily creates and
 * caches connections or simply {@link ConnectionProvider} if you want a new connection each time something requires one.
 */

public class DBLConnectionProvider implements PoolingConnectionProvider<DBLConnection>, Startable, Stoppable {

  @Inject
  private HttpService httpService;

  private volatile HttpClient httpClient;

  private final Logger LOGGER = LoggerFactory.getLogger(DBLConnectionProvider.class);

  @Override
  public DBLConnection connect() throws ConnectionException {
        // Create the HTTP client with a meaningful name for diagnostics
        HttpClientConfiguration.Builder builder = new HttpClientConfiguration.Builder();
        builder.setName("datablind-http-client");
        httpClient = httpService.getClientFactory().create(builder.build());
    
    return new DBLConnection("Test", httpClient);
  }

  @Override
  public void disconnect(DBLConnection connection) {
    try {
      connection.invalidate();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting [" + connection.getId() + "]: " + e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(DBLConnection connection) {
    return ConnectionValidationResult.success();
  }

  @Override
  public void start() throws MuleException {
    httpClient.start();
  }

  @Override
  public void stop() throws MuleException {
    httpClient.stop();
  }

}
