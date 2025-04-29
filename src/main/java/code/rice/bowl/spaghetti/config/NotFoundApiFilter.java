package code.rice.bowl.spaghetti.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * 서버에 요청한 API 존재 여부 확인.
 */
@Component
public class NotFoundApiFilter extends OncePerRequestFilter {

    private final Set<String> allowSet = Set.of(
            "/oauth",
            "/db",
            "/submit",
            "/problem",
            // swagger 관련
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-ui.html",
            "/configuration",
            "/swagger-resources",
            "/webjars",
            // test 용도
            "/test"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 현재 요청 api
        String path = request.getRequestURI();

        if (!isAllowed(path)) {
            // 없는 api 인 경우 -> 404 반환.

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"404\", \"message\": \"API Not Found\"}");
            return; // 필터 체인 중단
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAllowed(String path) {
        for (String allow: allowSet) {
            if (path.startsWith(allow))
                return true;
        }

        return path.equals("/");
    }
}
