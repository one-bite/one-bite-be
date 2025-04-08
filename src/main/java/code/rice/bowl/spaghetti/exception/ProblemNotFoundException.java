package code.rice.bowl.spaghetti.exception;

import org.springframework.http.HttpStatus;

public class ProblemNotFoundException extends BaseExceptionIF {

    public ProblemNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getError() {
        return "Problem not found";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
