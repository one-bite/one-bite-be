package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.problem.ProblemDetailResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemStatsResponse;
import code.rice.bowl.spaghetti.dto.problem.CommentaryRequest;
import code.rice.bowl.spaghetti.dto.problem.CommentaryResponse;
import code.rice.bowl.spaghetti.dto.request.SubmitRequest;
import code.rice.bowl.spaghetti.service.AiService;
import code.rice.bowl.spaghetti.service.ChallengeService;
import code.rice.bowl.spaghetti.service.ProblemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    private final AiService aiService;

    /** 문제 정보 상세 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<ProblemDetailResponse> getProblemDetail(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemDetail(id));
    }

    /** 오늘의 챌린지 시작 */
    @GetMapping("/challenge")
    public ResponseEntity<?> startChallenge(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        return ResponseEntity.ok(challengeService.startChallenge(email));
    }

    /** 챌린지 문제 채점 */
    @PostMapping("/challenge")
    public ResponseEntity<?> submitChallenge(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody @Valid SubmitRequest request
    ) {
        return ResponseEntity.ok(challengeService.challengeGrading(email, request));
    }

    /** 나의 문제 통계 조회 */
    @GetMapping("/stats")
    public ResponseEntity<ProblemStatsResponse> stats() {
        return ResponseEntity.ok(problemService.getMyProblemStats());
    }

    /**
     * AI 해설 생성/조회
     */
    @PostMapping("/commentary")
    public ResponseEntity<CommentaryResponse> postCommentary(
            @RequestBody @Valid CommentaryRequest req
    ) {
        String commentary;

        if (req.getId() != null && req.getId() > 0) {
            // DB 조회
            var problem = problemService.getProblem(req.getId());

            // 기존에 저장된 해설이 있으면 그대로, 없으면 새로 생성 후 저장
            if (problem.getCommentary() != null && !problem.getCommentary().isBlank()) {
                commentary = problem.getCommentary();
            } else {
                commentary = aiService.generateCommentary(req.getDescription());
                problemService.updateCommentary(req.getId(), commentary);
            }
        } else {
            // id == 0인 경우: DB 저장 없이 AI 해설 생성만
            commentary = aiService.generateCommentary(req.getDescription());
        }

        return ResponseEntity.ok(new CommentaryResponse(commentary));
    }
}
