package code.rice.bowl.spaghetti.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.units.qual.t;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;

import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
import code.rice.bowl.spaghetti.dto.ai.AiProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.exception.InternalServerError;
import code.rice.bowl.spaghetti.utils.QuestionType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ProblemRelationService problemRelationService;

    @Value("${ai.server.url}")
    private String aiBaseUrl;

    /**
     * AI 서버로부터 문제를 생성해 ProblemRequest 형태로 반환
     */
    public ProblemRequest generateProblemRequest(AiProblemRequest dto) {
        // 1. AI 서버 요청
        AiProblemResponse aiResp = restTemplate.postForObject(
                aiBaseUrl + "/generate-question", dto, AiProblemResponse.class);
        if (aiResp == null) {
            throw new InternalServerError("AI server returned null response");
        }

        userRepo.findById(ownerId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));
        Topic topic = topicRepo.findById(topicId)
                .orElseThrow(() -> new InvalidRequestException("Topic not found"));
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new InvalidRequestException("Topic not found"));
        // ai api 호출
        String url = aiBaseUrl + "/generate-question";
        log.info("AI 호출 URL: {}", url);
        try {
            ProblemRequest problemReq = restTemplate.postForObject(url, dto, ProblemRequest.class);

            if (problemReq == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "서버 응답이 없습니다");
            }
            problemReq.setUserId(ownerId);
            problemReq.setCategoryId(categoryId);
            problemReq.setTopicCodes(List.of(topic.getCode()));

            return problemService.create(problemReq);
        } catch (HttpClientErrorException.NotFound ex) {
            // 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AI 서버에서 해당 리소스를 찾을 수 없습니다", ex);
        } catch (HttpClientErrorException.BadRequest ex) {
            // 400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 AI 요청 파라미터", ex);
        } catch (HttpClientErrorException ex) {
            // 그 외 4xx
            throw new ResponseStatusException(ex.getStatusCode(), "AI 서버 오류: " + ex.getResponseBodyAsString(), ex);
        } catch (RestClientException ex) {
            // 네트워크 오류
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI 서버 호출 실패", ex);
        }

        // 2. 응답 변환
        ProblemRequest req = new ProblemRequest();
        req.setUserId(dto.getUserId());
        req.setCategoryId(dto.getCategoryId());
        req.setTopicCodes(List.copyOf(dto.getTopics()));
        req.setTitle(aiResp.getTitle());
        try {
            JsonNode desc = objectMapper.readTree(dto.getDescription());
            req.setDescription(desc);
        } catch (JsonProcessingException e) {
            throw new InternalServerError("Invalid description format");
        }
        req.setQuestionType(QuestionType.valueOf(dto.getQuestionType()));
        req.setHint(aiResp.getHint());
        req.setAnswer(aiResp.getAnswer());
        req.setPoint(dto.getCount());

        return req;
    }

    /**
     * AI 서버에 문제 해설 요청
     */
    public String generateCommentary(Problem problem) {
        String response = restTemplate.postForObject(
                aiBaseUrl + "/commentary",
                problem.getDescription().toString(),
                String.class
        );

        if (response == null || response.isBlank()) {
            throw new InternalServerError("cannot create commentary from AI Server");
        }

        return response;
    }

    /**
     * 비동기 문제 요청 및 후속 관계 생성
     */
    @Async
    public CompletableFuture<ProblemRequest> generateProblemRequestAsync(AiProblemRequest dto) {
        ProblemRequest req = generateProblemRequest(dto);
        return CompletableFuture.completedFuture(req);
    }

    /**
     * 문제 저장 이후에 부모-자식 관계를 기록
     */
    public void registerRelation(Long parentId, Long childId) {
        problemRelationService.createRelation(parentId, childId);
    }
}