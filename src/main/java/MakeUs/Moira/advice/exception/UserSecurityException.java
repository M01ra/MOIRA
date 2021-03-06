package MakeUs.Moira.advice.exception;

public class UserSecurityException extends RuntimeException {
    public UserSecurityException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserSecurityException(String msg) {
        super(msg);
    }

    public UserSecurityException() {
        super();
    }
}
