package code.rice.bowl.spaghetti.exception;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseExceptionIF {

    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getError() {
        return "User not found";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
