package zpi.ppea.clap.service;

import backend.clap.HelloReply;
import backend.clap.HelloRequest;
import backend.clap.MyServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
public class GrpcServerMock extends MyServiceGrpc.MyServiceImplBase {

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
