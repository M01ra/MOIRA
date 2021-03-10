package MakeUs.Moira.advice.exception;

public class ProjectException extends RuntimeException{
    public ProjectException(String msg, Throwable t){
        super(msg, t);
    }

    public ProjectException(String msg){
        super(msg);
    }

    public ProjectException(){
        super();
    }
}
