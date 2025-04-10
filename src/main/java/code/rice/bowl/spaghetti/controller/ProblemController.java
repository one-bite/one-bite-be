package code.rice.bowl.spaghetti.controller;


import code.rice.bowl.spaghetti.dto.request.CreateProblemRequest;
import code.rice.bowl.spaghetti.dto.request.SolveRequest;
import code.rice.bowl.spaghetti.dto.response.SolveResponse;
import code.rice.bowl.spaghetti.service.GradingService;
import code.rice.bowl.spaghetti.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;
    private final GradingService gradingService;

    @PostMapping("/{problemId}/submit")
    public ResponseEntity<SolveResponse> solveProblem(
            @PathVariable Long problemId,
            @RequestBody SolveRequest request
    ) {
        SolveResponse result = gradingService.grade(
                problemId,
                request.getUserId(),
                request.getAnswer(),
                request.getSolveTime()
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/")
    @Operation(
            summary = "문제 등록"
    )
    public ResponseEntity<?> addProblem(@RequestBody CreateProblemRequest newProblem) {
        return ResponseEntity.ok(problemService.createProblem(newProblem));
    }

}
