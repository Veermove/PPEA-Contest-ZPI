package zpi.ppea.clap.exceptions;

public class NoAccessToResource extends ClientException {
    public NoAccessToResource(Exception e, String refresh) {
        super(e, refresh);
    }
}
