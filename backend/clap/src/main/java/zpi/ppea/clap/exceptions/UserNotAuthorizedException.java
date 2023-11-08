package zpi.ppea.clap.exceptions;

public class UserNotAuthorizedException extends ClientException {
    public UserNotAuthorizedException(Exception e, String refresh) {
        super(e, refresh);
    }
}
