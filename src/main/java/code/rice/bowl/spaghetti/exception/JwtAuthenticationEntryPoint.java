package code.rice.bowl.spaghetti.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Security Config 필터 과정에서 발생 시키는 에러, 401
 * 사용자가 인증되지 않았는데 인증이 필요한 서비스에 접근한 경우.
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        setResponse(response, authException);
    }

    private void setResponse(
            HttpServletResponse response,
            AuthenticationException authenticationException) throws IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "401 Unauthorized");
        responseBody.put("message", authenticationException.getMessage());
        responseBody.put("timestamp", System.currentTimeMillis());
        responseBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().println(objectMapper.writeValueAsString(responseBody));

    }
}
