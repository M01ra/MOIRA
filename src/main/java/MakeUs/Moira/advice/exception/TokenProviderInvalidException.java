package MakeUs.Moira.advice.exception;

public class TokenProviderInvalidException extends RuntimeException {
    public TokenProviderInvalidException(String msg, Throwable t) {
        super(msg, t);
    }

    public TokenProviderInvalidException(String msg) {
        super(msg);
    }

    public TokenProviderInvalidException() {
        super();
    }
}
