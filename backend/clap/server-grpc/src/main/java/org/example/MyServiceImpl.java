package org.example;


import io.grpc.stub.StreamObserver;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.example.HelloRequest;
import net.devh.boot.grpc.example.HelloReply;
import net.devh.boot.grpc.example.MyServiceGrpc;

import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class MyServiceImpl extends MyServiceGrpc.MyServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello ==> " + request.getName())
                .build();
        log.info("Received: {}", request);
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
