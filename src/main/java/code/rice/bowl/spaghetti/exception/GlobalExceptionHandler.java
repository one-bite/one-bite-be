package code.rice.bowl.spaghetti.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseExceptionIF.class)
    public ResponseEntity<?> handleBaseException(BaseExceptionIF e) {
        Map<String, Object> response = new HashMap<>();

        response.put("error", e.getError());
        response.put("message", e.getMessage());
        response.put("status", e.getStatus().value());

        return new ResponseEntity<>(response, e.getStatus());
    }
}
