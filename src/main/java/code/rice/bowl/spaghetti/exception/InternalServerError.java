package code.rice.bowl.spaghetti.exception;

import org.springframework.http.HttpStatus;

/**
 * 서버에서 에러가 발생한 경우의 리턴, 500
 */
public class InternalServerError extends BaseExceptionIF {
    public InternalServerError(String message) {
        super(message);
    }

    @Override
    public String getError() {
        return "Server error";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
