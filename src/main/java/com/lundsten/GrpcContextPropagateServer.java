package com.lundsten;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@GrpcService
public class GrpcContextPropagateServer implements HelloService {

  @Inject
  @RestClient
  DummyRestClient restClient;

  @Override
  public Uni<HelloReply> sayHelloParallel(HelloRequest request) {
    return Uni.combine().all().unis(restClient.sayHello(), restClient.sayHello2())
        .asTuple()
        .map(tuple -> HelloReply.newBuilder()
            .setMessage(tuple.getItem1() + tuple.getItem2())
            .build());
  }

  @Override
  public Uni<HelloReply> sayHelloChained(HelloRequest request) {
    return restClient.sayHello()
        .chain(s -> restClient.sayHello2())
        .map(s -> HelloReply.newBuilder()
            .setMessage(s)
            .build());
  }
}
