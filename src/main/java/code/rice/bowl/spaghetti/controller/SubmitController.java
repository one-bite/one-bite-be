package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.request.SubmitRequest;
import code.rice.bowl.spaghetti.dto.response.SubmitResponse;
import code.rice.bowl.spaghetti.service.GradingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
@RequiredArgsConstructor
@Tag(name = "Submit: 사용자 문제 채점")
public class SubmitController {

    private final GradingService gradingService;

    @PostMapping("/{problemId}")
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