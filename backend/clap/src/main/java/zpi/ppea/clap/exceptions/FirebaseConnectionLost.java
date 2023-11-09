package zpi.ppea.clap.exceptions;

public class FirebaseConnectionLost extends ClientException {
    public FirebaseConnectionLost(Exception e, String refresh) {
        super(e, refresh);
    }
}
