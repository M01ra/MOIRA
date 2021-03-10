package MakeUs.Moira.advice.exception;

public class AccessTokenInvalidException extends RuntimeException {
    public AccessTokenInvalidException(String msg, Throwable t) {
        super(msg, t);
    }

    public AccessTokenInvalidException(String msg) {
        super(msg);
    }

    public AccessTokenInvalidException() {
        super();
    }
}
