package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.ai.AiProblemResponse;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.entity.AiGeneratedProblem;
import code.rice.bowl.spaghetti.repository.AiGeneratedProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiGeneratedProblemService {

    private final AiGeneratedProblemRepository aiRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // 단순 호출용

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public void requestAndSave(User user, Problem originalProblem) {
        try {
            // AI 서버에 GET or POST 요청
            AiProblemResponse aiResponse = restTemplate.postForObject(
                    aiServerUrl,
                    buildRequest(user, originalProblem),
                    AiProblemResponse.class
            );

            if (aiResponse == null) {
                log.warn("AI 서버로부터 응답이 없습니다.");
                return;
            }

            AiGeneratedProblem aiProblem = AiGeneratedProblem.builder()
                    .user(user)
                    .title(aiResponse.getTitle())
                    .description(aiResponse.getDescription())
                    .questionType(AiGeneratedProblem.QuestionType.valueOf(aiResponse.getQuestionType()))
                    .difficulty(AiGeneratedProblem.DifficultyLevel.valueOf(aiResponse.getDifficulty()))
                    .hint(aiResponse.getHint())
                    .answer(aiResponse.getAnswer())
                    .features(aiResponse.getFeatures())
                    .score(aiResponse.getScore())
                    .build();

            aiRepository.save(aiProblem);
        } catch (Exception e) {
            log.error("AI 문제 생성 중 오류 발생", e);
        }
    }

    private Object buildRequest(User user, Problem problem) {
        //
        return new Object() {
            public final String email = user.getEmail();
            public final Long originalProblemId = problem.getProblemId();
        };
    }
}
