package org.example;


import net.devh.boot.grpc.client.inject.GrpcClient;

import net.devh.boot.grpc.example.MyServiceGrpc;
import net.devh.boot.grpc.example.HelloRequest;
import org.springframework.stereotype.Service;

@Service
public class FoobarService {

    @GrpcClient("myService")
    MyServiceGrpc.MyServiceBlockingStub myServiceStub;

    public String receiveGreeting(String name) {
        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();
        return myServiceStub.sayHello(request).getMessage();
    }

}
