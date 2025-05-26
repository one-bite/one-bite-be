package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.request.SubmitRequest;
import code.rice.bowl.spaghetti.dto.response.SubmitResponse;
import code.rice.bowl.spaghetti.service.GradingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("/submit")
@RequiredArgsConstructor
@Tag(name = "Submit")
public class SubmitController {

    private final GradingService gradingService;

    // 오늘의 문제 채점
    @PostMapping("/today")
    @Operation(summary = "사용자가 제출한 문제 채점 및 결과 반환")
    public Callable<ResponseEntity<SubmitResponse>> solveProblem(
            @RequestBody SubmitRequest request
    ) {
        return () -> {
            SubmitResponse result = gradingService.gradeTodayProblem(request);
            return ResponseEntity.ok(result);
        };
    }

}
