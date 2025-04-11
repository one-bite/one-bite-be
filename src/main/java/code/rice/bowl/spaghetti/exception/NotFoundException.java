package code.rice.bowl.spaghetti.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseExceptionIF {

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public String getError() {
        return "not found";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
