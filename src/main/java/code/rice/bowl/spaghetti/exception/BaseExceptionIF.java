package code.rice.bowl.spaghetti.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseExceptionIF extends RuntimeException {
    public abstract String getError();
    public abstract HttpStatus getStatus();

    public BaseExceptionIF(String message) {
        super(message);
    }
}
