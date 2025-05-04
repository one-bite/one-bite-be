package code.rice.bowl.spaghetti.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

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

    @Value("${ai.base.url}")
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

        ProblemRequest problemReq = restTemplate.postForObject(url, dto, ProblemRequest.class);

        problemReq.setUserId(ownerId);
        problemReq.setCategoryId(categoryId);
        problemReq.setTopicCodes(List.of(topic.getCode()));

        return problemService.create(problemReq);
    }
}
