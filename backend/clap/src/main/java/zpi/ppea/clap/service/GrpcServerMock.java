//package zpi.ppea.clap.service;
//
//import backend.clap.*;
//import com.google.protobuf.Empty;
//import io.grpc.stub.StreamObserver;
//import lombok.extern.slf4j.Slf4j;
//import org.lognet.springboot.grpc.GRpcService;
//
//@Slf4j
//@GRpcService
//public class GrpcServerMock extends SubmissionServiceGrpc.SubmissionServiceImplBase {
//
//    @Override
//    public void getAllSubmissions(Empty request, StreamObserver<SubmissionList> responseObserver) {
//        log.info("get all submissions");
//        SubmissionList mockList = SubmissionList.newBuilder().addSubmissions(
//                Submission.newBuilder()
//                        .setSubmissionId(1)
//                        .setName("name")
//                        .setDurationDays(5)
//                        .setYear(2023)
//                        .setRatingType("rating")
//                        .setRatingPoints(50)
//                        .build()).build();
//        responseObserver.onNext(mockList);
//        responseObserver.onCompleted();
//    }
//
//}
