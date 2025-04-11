package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.request.SubmitRequest;
import code.rice.bowl.spaghetti.dto.response.SubmitResponse;
import code.rice.bowl.spaghetti.service.GradingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
@RequiredArgsConstructor
@Tag(name = "Submit")
public class SubmitController {

    private final GradingService gradingService;

    // 채점
    @PostMapping("/{problemId}")
    @Operation(summary = "사용자가 제출한 문제 채점 및 결과 반환")
    public ResponseEntity<SubmitResponse> solveProblem(
            @PathVariable Long problemId,
            @RequestBody SubmitRequest request
    ) {
        SubmitResponse result = gradingService.grade(
                problemId,
                request.getUserId(),
                request.getAnswer(),
                request.getSolveTime()
        );
        return ResponseEntity.ok(result);
    }
}