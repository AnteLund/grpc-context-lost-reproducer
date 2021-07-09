package com.lundsten;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(WireMockConfiguration.class)
public class ReactiveGreetingResourceTest {

  @GrpcClient
  HelloService helloClient;

  @Test
  public void testHelloEndpointParallel() {
    stubHelloEndpoints();

    //Parallel call to RestClient. This works as the context are passed to both calls in parallel or something.
    helloClient
        .sayHelloParallel(HelloRequest.newBuilder().setName("name").build())
        .subscribe()
        .withSubscriber(
            UniAssertSubscriber.create())
        .awaitItem()
        .assertCompleted();

  }

  @Test
  public void testHelloEndpointChain() {
    stubHelloEndpoints();

    //Chained call, only the first rest request pass. When second request is sent the context is lost.
    //The exception is thrown by RestClientFilter, as the context key is missing.
    helloClient
        .sayHelloChained(HelloRequest.newBuilder().setName("name").build()).subscribe()
        .withSubscriber(UniAssertSubscriber.create())
        .awaitItem()
        .assertCompleted();

  }

  private void stubHelloEndpoints() {
    stubFor(get(urlEqualTo("/hello"))
        .willReturn(
            aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("hello1")
        ));
    stubFor(get(urlEqualTo("/hello2"))
        .willReturn(
            aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("hello2")
        ));
  }

}