package MakeUs.Moira.advice.exception;

public class UserException extends RuntimeException {
    public UserException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserException(String msg) {
        super(msg);
    }

    public UserException() {
        super();
    }
}
