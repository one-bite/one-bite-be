package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
import code.rice.bowl.spaghetti.dto.ai.AiProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.exception.InternalServerError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ProblemService problemService;
    private final ProblemRelationService problemRelationService;

    @Value("${ai.server.url}")
    private String aiBaseUrl;

    /**
     * 오답 시 호출하여 AI 문제 생성 및 저장, 부모-자식 관계 기록 수행
     * @param dto parentProblemId, description, topics, difficulty, questionType, userId, categoryId, count
     */
    public ProblemResponse generateProblem(AiProblemRequest dto) {
        // 1) AI 서버 호출
        AiProblemResponse aiResp = restTemplate.postForObject(
                aiBaseUrl + "/generate-question", dto, AiProblemResponse.class);
        if (aiResp == null) {
            throw new InternalServerError("AI server returned null response");
        }

        // 2) AI 응답 → ProblemRequest 매핑
        ProblemRequest req = new ProblemRequest();
        req.setUserId(dto.getUserId());
        req.setCategoryId(dto.getCategoryId());
        req.setTopicCodes(List.of(dto.getTopics().toArray(new String[0])));
        req.setTitle(aiResp.getTitle());
        try {
            JsonNode desc = objectMapper.readTree(dto.getDescription());
            req.setDescription(desc);
        } catch (JsonProcessingException e) {
            throw new InternalServerError("Invalid description format");
        }
        req.setQuestionType(Problem.QuestionType.valueOf(dto.getQuestionType()));
        req.setHint(aiResp.getHint());
        req.setAnswer(aiResp.getAnswer());
        req.setPoint(dto.getCount());

        // 3) 문제 저장
        ProblemResponse saved = problemService.create(req);

        // 4) 부모-자식 관계 기록
        problemRelationService.createRelation(dto.getParentProblemId(), saved.getProblemId());

        return saved;
    }
}