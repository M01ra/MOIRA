package MakeUs.Moira.advice.exception;

public class S3Exception extends RuntimeException{
    public S3Exception(String msg, Throwable t){
        super(msg, t);
    }

    public S3Exception(String msg){
        super(msg);
    }

    public S3Exception(){
        super();
    }
}
