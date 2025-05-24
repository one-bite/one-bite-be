package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.ai.*;
import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.exception.InternalServerError;
import code.rice.bowl.spaghetti.utils.QuestionType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
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

        // 2. AI 서버에 보낼 요청 DTO
        AiServerProblemRequest serverDto = new AiServerProblemRequest(
                descString,
                dto.getTopics(),
                dto.getQuestionType()
        );

        // 3. AI 서버 호출 → AiProblemResponse 사용
        AiProblemResponse aiResp = restTemplate.postForObject(
                aiBaseUrl + "/generate-question",
                serverDto,
                AiProblemResponse.class
        );
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
        req.setQuestionType(QuestionType.valueOf(aiResp.getQuestionType()));
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

        // 2) AI 서버에 JSON 바디로 요청
        AiCommentaryRequest req = new AiCommentaryRequest(payload);
        AiCommentaryResponse resp = restTemplate.postForObject(
                aiBaseUrl + "/commentary",
                req,
                AiCommentaryResponse.class
        );

        if (resp == null || resp.getCommentary().isBlank()) {
            throw new InternalServerError("AI 서버 해설 생성 실패");
        }
        return resp.getCommentary();
    }


    public AiProblemResponse generateProblemWithCommentary(AiProblemRequest dto) {
        // (a) description(JsonNode) → String 직렬화
        StringBuilder sb = new StringBuilder(dto.getDescription().path("question").asText());
        JsonNode opts = dto.getDescription().path("options");
        if (opts.isArray()) {
            sb.append(" Options: ");
            for (int i = 0; i < opts.size(); i++) {
                sb.append(opts.get(i).asText());
                if (i < opts.size()-1) sb.append(", ");
            }
        }
        String descString = sb.toString();

        // (b) AI 문제 생성 요청
        AiServerProblemRequest req = new AiServerProblemRequest(
                descString, dto.getTopics(), dto.getQuestionType()
        );
        AiProblemResponse aiResp = restTemplate.postForObject(
                aiBaseUrl + "/generate-question", req, AiProblemResponse.class
        );
        if (aiResp == null) throw new InternalServerError("AI 서버 문제 생성 실패");

        // (c) AI 해설 생성 요청 (원본 설명 JSON 문자열로)
        String commentary = restTemplate.postForObject(
                aiBaseUrl + "/commentary",
                aiResp.getDescription().toString(),
                String.class
        );
        if (commentary == null || commentary.isBlank()) {
            throw new InternalServerError("AI 서버 해설 생성 실패");
        }

        // (d) 해설을 DTO에 세팅 후 반환
        aiResp.setCommentary(commentary);
        return aiResp;
    }

    /**
     * 비동기 문제 요청 및 후속 관계 생성
     */
    @Async
    public CompletableFuture<ProblemRequest> generateProblemRequestAsync(AiProblemRequest dto) {
        return CompletableFuture.completedFuture(generateProblemRequest(dto));
    }
}
