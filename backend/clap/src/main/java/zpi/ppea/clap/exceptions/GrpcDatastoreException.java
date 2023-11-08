package zpi.ppea.clap.exceptions;

public class GrpcDatastoreException extends ClientException {
    public GrpcDatastoreException(Exception e, String refresh) {
        super(e, refresh);
    }
}
