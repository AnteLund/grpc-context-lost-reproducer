package com.lundsten;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GrpcContextInterceptor implements ServerInterceptor {

  public static final Context.Key<String> USER_CONTEXT_KEY =
      Context.key("grpc-user");

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
      Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
    return Contexts.interceptCall(Context.current().withValue(USER_CONTEXT_KEY, "grpc_call_user"), serverCall, metadata, serverCallHandler);
  }
}
