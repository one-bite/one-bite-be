package code.rice.bowl.spaghetti.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.service.AiService;

/**
 * AI 백엔드로 요청을 전달하는 컨트롤러
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiProblemController {
    private final AiService aiService;

    @PostMapping("/problem")
    @ResponseStatus(HttpStatus.CREATED)
    public ProblemResponse createAiProblem(
            @RequestBody AiProblemRequest aiDto,
            @RequestParam Long ownerId,
            @RequestParam Long topicId,
            @RequestParam Long categoryId) {

        return aiService.generateProblem(aiDto, ownerId, topicId, categoryId);
    }
}
