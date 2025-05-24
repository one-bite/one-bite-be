package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
import code.rice.bowl.spaghetti.dto.ai.AiProblemResponse;
import code.rice.bowl.spaghetti.service.AiService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AI 기반 문제 생성 요청 API
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "AI Problem Generation")
public class AiProblemController {

    private final AiService aiService;

    @PostMapping("/problem")
    public ResponseEntity<AiProblemResponse> createAiProblem(
            @RequestBody @Valid AiProblemRequest dto
    ) {
        AiProblemResponse aiResp = aiService.generateProblemWithCommentary(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(aiResp);
    }
}