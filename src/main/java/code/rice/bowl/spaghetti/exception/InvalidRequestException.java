package code.rice.bowl.spaghetti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 사용자의 잘 못된 요청에 대한 에러, return for 400 status
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String msg) {
        super(msg);
    }
}
