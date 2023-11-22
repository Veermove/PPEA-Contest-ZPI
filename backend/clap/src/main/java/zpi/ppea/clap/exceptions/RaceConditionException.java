package zpi.ppea.clap.exceptions;

public class RaceConditionException extends ClientException {
    public RaceConditionException(Exception e, String refresh) {
        super(e, refresh);
    }
}
