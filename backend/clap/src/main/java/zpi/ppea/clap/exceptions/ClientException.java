package zpi.ppea.clap.exceptions;

import lombok.Data;

@Data
public class ClientException extends RuntimeException {
    private final String refresh;

    public ClientException(Exception e, String refresh) {
        super(e);
        this.refresh = refresh;
    }
}
