package code.rice.bowl.spaghetti.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 미구현 서비스에 대한 에러, return for 501 status
 */
@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
public class NotImplementedException extends RuntimeException{
    public NotImplementedException(String msg) {
        super(msg);
    }
}