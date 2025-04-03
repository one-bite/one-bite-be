package code.rice.bowl.spaghetti.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Security config 필터 과정에서 발생 시키는 에러, 403
 * 이미 인증된 사용자가 다시 인증하려고 할 때 발생.
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        setResponse(response, accessDeniedException);
    }

    private void setResponse(
            HttpServletResponse response,
            AccessDeniedException authenticationException) throws IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "403 Forbidden");
        responseBody.put("message", authenticationException.getMessage());
        responseBody.put("timestamp", System.currentTimeMillis());
        responseBody.put("status", HttpServletResponse.SC_FORBIDDEN);

        response.getWriter().println(objectMapper.writeValueAsString(responseBody));

    }
}
