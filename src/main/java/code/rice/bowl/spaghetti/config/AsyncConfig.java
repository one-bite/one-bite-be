package code.rice.bowl.spaghetti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    // 비동기 처리시 SecurityContext 전파용 실행기
    @Bean(name = "securityExecutor")
    public AsyncTaskExecutor securityExecutor() {
        ThreadPoolTaskExecutor delegate = new ThreadPoolTaskExecutor();

        // 기본 스레드 수 설정.
        delegate.setCorePoolSize(3);
        // 최대 스레드 수 설정.
        delegate.setMaxPoolSize(6);
        delegate.initialize();

        return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
    }

    @Override
    public AsyncTaskExecutor getAsyncExecutor() {
        return securityExecutor();
    }
}
