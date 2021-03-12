package MakeUs.Moira.advice.exception;


public class AuthEntryPointException extends RuntimeException {

    public AuthEntryPointException(String msg) {
        super(msg);
    }


}
//        int errorCode = Integer.parseInt(code);
//
//        if (errorCode == CustomJwtException.NO_TOKEN_REQUEST_ERROR.getCode())
//            this.errorMsg = CustomJwtException.NO_TOKEN_REQUEST_ERROR.getMessage();
//        else if (errorCode == CustomJwtException.INVALID_SIGN_TOKEN_ERROR.getCode())
//            this.errorMsg = CustomJwtException.INVALID_SIGN_TOKEN_ERROR.getMessage();
//        else if (errorCode == CustomJwtException.NO_EXPIRATION_ERROR.getCode())
//            this.errorMsg = CustomJwtException.NO_EXPIRATION_ERROR.getMessage();
//        else if (errorCode == CustomJwtException.EXPIRED_TOKEN_ERROR.getCode())
//            this.errorMsg = CustomJwtException.EXPIRED_TOKEN_ERROR.getMessage()

//    public String getErrorMsg() {
//        return this.errorMsg;
//    }

