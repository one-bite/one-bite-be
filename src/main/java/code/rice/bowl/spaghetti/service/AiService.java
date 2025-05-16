package code.rice.bowl.spaghetti.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import org.checkerframework.checker.units.qual.t;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
import code.rice.bowl.spaghetti.dto.ai.AiProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.entity.Topic;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.entity.Category;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.mapper.ProblemMapper;
import code.rice.bowl.spaghetti.repository.CategoryRepository;
import code.rice.bowl.spaghetti.repository.TopicRepository;
import code.rice.bowl.spaghetti.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepo;
    private final TopicRepository topicRepo;
    private final CategoryRepository categoryRepository;
    private final ProblemService problemService;

    @Value("${ai.server.url}")
    private String aiBaseUrl;

    public ProblemResponse generateProblem(AiProblemRequest dto, Long ownerId, Long topicId, Long categoryId) {

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

    }
}
