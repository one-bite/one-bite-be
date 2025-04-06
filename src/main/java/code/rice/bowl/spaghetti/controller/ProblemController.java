package code.rice.bowl.spaghetti.controller;


import code.rice.bowl.spaghetti.dto.request.CreateProblemRequest;
import code.rice.bowl.spaghetti.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problem")
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/{problemId}/submit")
    public ResponseEntity<?> solveProblem() {
        return ResponseEntity.ok("test world");
    }

    @PostMapping("/")
    @Operation(
            summary = "문제 등록"
    )
    public ResponseEntity<?> addProblem(@RequestBody CreateProblemRequest newProblem) {
        return ResponseEntity.ok(problemService.createProblem(newProblem));
    }

}
