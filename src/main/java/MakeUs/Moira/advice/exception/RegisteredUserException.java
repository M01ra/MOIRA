package MakeUs.Moira.advice.exception;


public class RegisteredUserException extends RuntimeException {
    public RegisteredUserException(String msg, Throwable t) {
        super(msg, t);
    }

    public RegisteredUserException(String msg) {
        super(msg);
    }

    public RegisteredUserException() {
        super();
    }
}
