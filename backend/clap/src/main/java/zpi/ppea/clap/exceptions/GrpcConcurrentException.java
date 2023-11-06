package zpi.ppea.clap.exceptions;

public class GrpcConcurrentException extends RuntimeException {
    public GrpcConcurrentException(String message) {
        super(message);
    }
}
