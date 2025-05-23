package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.service.AiService;
import code.rice.bowl.spaghetti.service.ProblemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 기반 문제 생성 요청 API
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "AI Problem Generation")
public class AiProblemController {

    private final AiService aiService;
    private final ProblemService problemService;

    @PostMapping("/problem")
    @ResponseStatus(HttpStatus.CREATED)
    public ProblemResponse createAiProblem(@RequestBody AiProblemRequest aiDto) {
        // 1. AI에게 문제 생성 요청 → ProblemRequest 반환
        ProblemRequest problemReq = aiService.generateProblemRequest(aiDto);

        // 2. 문제 저장 → 생성된 문제 ID 등을 포함한 응답 생성
        return problemService.create(problemReq);
    }
}
