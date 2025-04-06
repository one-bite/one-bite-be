package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.request.SolveRequest;
import code.rice.bowl.spaghetti.dto.response.SolveResponse;
import code.rice.bowl.spaghetti.service.GradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/problem")
@RequiredArgsConstructor
public class ProblemController {

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

    @PostMapping("/make")
    public ResponseEntity<?> addProblem() {
        return ResponseEntity.ok("test world");
    }

}
