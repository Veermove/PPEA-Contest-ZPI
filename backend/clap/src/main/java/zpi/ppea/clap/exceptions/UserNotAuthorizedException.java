package zpi.ppea.clap.exceptions;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException(String msg) {
        super(msg);
    }
}
