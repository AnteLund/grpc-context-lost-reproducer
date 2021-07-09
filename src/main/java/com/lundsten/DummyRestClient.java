package com.lundsten;

import io.smallrye.mutiny.Uni;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "hello-api")
@RegisterProvider(RestClientFilter.class)
public interface DummyRestClient {

  @GET
  @Path("/hello")
  Uni<String> sayHello();

  @GET
  @Path("/hello2")
  Uni<String> sayHello2();
}
