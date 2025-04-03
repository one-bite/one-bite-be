package code.rice.bowl.spaghetti.exception;


import org.springframework.http.HttpStatus;

/**
 * 미구현 서비스에 대한 에러, return for 501 status
 */
public class NotImplementedException extends BaseExceptionIF {
    @Override
    public String getError() {
        return "Not implemented";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_IMPLEMENTED;
    }

    public NotImplementedException(String msg) {
        super(msg);
    }
}