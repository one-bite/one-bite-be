package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.ai.*;
import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.exception.InternalServerError;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.utils.QuestionType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;
    private final ProblemRelationService problemRelationService;
    private final AuthService authService;

    @Value("${ai.server.url}")
    private String aiBaseUrl;

    /**
     * AI 서버로부터 문제를 생성해 ProblemRequest 형태로 반환
     */
    public ProblemRequest generateProblemRequest(AiProblemRequest dto) {
        // 1. description(JsonNode) → String 으로 변환 for AI server
        JsonNode inputDesc = dto.getDescription();
        StringBuilder sb = new StringBuilder(inputDesc.path("question").asText());
        JsonNode opts = inputDesc.path("options");
        if (opts.isArray()) {
            sb.append(" Options: ");
            for (int i = 0; i < opts.size(); i++) {
                sb.append(opts.get(i).asText());
                if (i < opts.size() - 1) sb.append(", ");
            }
        }
        String descString = sb.toString();
        String topicsJoined = String.join(",", dto.getTopics());

        // 2. AI 서버에 보낼 요청 DTO
        AiServerProblemRequest serverDto = new AiServerProblemRequest(
                descString,
                topicsJoined,
                dto.getQuestionType()
        );

        // 3. AI 서버 호출
        AiProblemResponse aiResp;
        try {
            aiResp = restTemplate.postForObject(
                    aiBaseUrl + "/generate-question",
                    serverDto,
                    AiProblemResponse.class
            );
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                throw new InvalidRequestException("API 일일 사용량을 초과했습니다. 나중에 다시 시도하세요. (no_token)");
            }
            // 그 외
            throw new InternalServerError("AI 서버 호출 중 오류 발생: " + ex.getStatusCode());
        }

        if (aiResp == null) {
            throw new InternalServerError("AI server returned null response");
        }

        // 4. ProblemRequest 빌드
        ProblemRequest req = new ProblemRequest();
        req.setUserId(authService.getCurrentUserId());
        req.setCategoryId(1L);
        req.setTopicCodes(List.copyOf(dto.getTopics()));
        req.setTitle(aiResp.getTitle());
        req.setDescription(aiResp.getDescription());
        req.setQuestionType(QuestionType.toType(aiResp.getQuestionType()));
        req.setHint(aiResp.getHint());
        req.setAnswer(aiResp.getAnswer());
        req.setPoint(aiResp.getPoint());

        return req;
    }

    /**
     * 문제 저장 이후에 부모-자식 관계를 기록
     */
    public void registerRelation(Long parentId, Long childId) {
        problemRelationService.createRelation(parentId, childId);
    }

    /**
     * AI 서버에 해설 요청
     */
    public String generateCommentary(JsonNode description) {
        // 1) JSON → String 직렬화
        String payload = description.toString();
        AiCommentaryRequest req = new AiCommentaryRequest(payload);

        // 2) AI 서버에 JSON 바디로 요청
        AiCommentaryResponse resp;
        try {
            resp = restTemplate.postForObject(
                    aiBaseUrl + "/commentary",
                    req,
                    AiCommentaryResponse.class
            );
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                throw new InvalidRequestException("토큰 없습니다");
            }
            throw new InternalServerError("AI 서버 호출 중 오류 발생: " + ex.getStatusCode());
        }

        if (resp == null || resp.getCommentary().isBlank()) {
            throw new InternalServerError("AI 서버 해설 생성 실패");
        }
        return resp.getCommentary();
    }

    /**
     * AI 서버로부터 문제와 해설까지 같이 받는 간단 버전 (서비스 레이어용)
     */
    public AiProblemResponse generateProblemWithCommentary(AiProblemRequest dto) {
        // (a) description(JsonNode) -> String 직렬화
        StringBuilder sb = new StringBuilder(dto.getDescription().path("question").asText());
        JsonNode opts = dto.getDescription().path("options");
        if (opts.isArray()) {
            sb.append(" Options: ");
            for (int i = 0; i < opts.size(); i++) {
                sb.append(opts.get(i).asText());
                if (i < opts.size() - 1) sb.append(", ");
            }
        }
        String descString = sb.toString();
        String topicsJoined = String.join(",", dto.getTopics());
        AiServerProblemRequest req = new AiServerProblemRequest(
                descString, topicsJoined, dto.getQuestionType()
        );

        AiProblemResponse aiResp;
        try {
            aiResp = restTemplate.postForObject(
                    aiBaseUrl + "/generate-question", req, AiProblemResponse.class
            );
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                throw new InvalidRequestException("API 일일 사용량을 초과했습니다. 나중에 다시 시도하세요. (no_token)");
            }
            throw new InternalServerError("AI 서버 호출 중 오류 발생: " + ex.getStatusCode());
        }

        if (aiResp == null) {
            throw new InternalServerError("AI 서버 문제 생성 실패");
        }

        aiResp.setCommentary("");
        return aiResp;
    }

    /**
     * 비동기 처리
     */
    @Async
    public CompletableFuture<ProblemRequest> generateProblemRequestAsync(AiProblemRequest dto) {
        return CompletableFuture.completedFuture(generateProblemRequest(dto));
    }

    @Async
    public CompletableFuture<String> generateCommentaryAsync(JsonNode description) {
        String commentary = generateCommentary(description);
        return CompletableFuture.completedFuture(commentary);
    }
}
