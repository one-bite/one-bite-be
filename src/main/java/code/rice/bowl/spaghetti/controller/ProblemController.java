package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.dto.request.SubmitRequest;
import code.rice.bowl.spaghetti.dto.response.SimpleOkResponse;
import code.rice.bowl.spaghetti.service.ChallengeService;
import code.rice.bowl.spaghetti.service.ProblemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problem")
@Tag(name = "About Problem")
public class ProblemController {

    private final ProblemService problemService;
    private final ChallengeService challengeService;

    // 문제 정보 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getProblemDetail(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemDetail(id));
    }

    @GetMapping("/challenge")
    public ResponseEntity<?> startChallenge(@AuthenticationPrincipal(expression = "username") String email) {
        return ResponseEntity.ok(challengeService.startChallenge(email));
    }

    @PostMapping("/challenge")
    public ResponseEntity<?> submitChallenge(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody SubmitRequest request
    ) {
        return ResponseEntity.ok(challengeService.challengeGrading(email, request));
    }
}
