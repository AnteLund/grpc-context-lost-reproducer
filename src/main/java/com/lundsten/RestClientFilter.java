package com.lundsten;

import static com.lundsten.GrpcContextInterceptor.USER_CONTEXT_KEY;

import java.io.IOException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestClientFilter implements ClientRequestFilter {
  Logger logger = LoggerFactory.getLogger(RestClientFilter.class);

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    if(USER_CONTEXT_KEY.get() == null) {
      logger.error("User context key missing when calling [{}]", requestContext.getUri());
      throw new ProcessingException("User context key does not exist");
    }
    logger.error("User context key exists when calling [{}]", requestContext.getUri());
    requestContext.getHeaders().add("user", USER_CONTEXT_KEY.get());
  }
}
