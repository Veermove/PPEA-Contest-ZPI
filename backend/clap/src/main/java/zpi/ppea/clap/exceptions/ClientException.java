package zpi.ppea.clap.exceptions;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private final String refresh;

    public ClientException(Exception e, String refresh) {
        super(e);
        this.refresh = refresh;
    }
}
