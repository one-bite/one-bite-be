package code.rice.bowl.spaghetti.exception;

import org.springframework.http.HttpStatus;

/**
 * 사용자의 잘 못된 요청에 대한 에러, return for 400 status
 */
public class InvalidRequestException extends BaseExceptionIF {
    public InvalidRequestException(String message) {
        super(message);
    }

    @Override
    public String getError() {
        return "Invalid request";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
