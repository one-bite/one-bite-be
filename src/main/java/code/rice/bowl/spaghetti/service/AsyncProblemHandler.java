package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncProblemHandler {
    private final AiService aiService;
    private final ProblemService problemService;
    private final ProblemRelationService problemRelationService;

    @Async
    @Transactional
    public void handleNewProblem(AiProblemRequest aiReq, Long parentProblemId) {
        try {
            ProblemRequest pr = aiService.generateProblemRequest(aiReq);
            ProblemResponse saved = problemService.create(pr);

            System.out.println(saved.getProblemId());
            problemRelationService.createRelation(parentProblemId, saved.getProblemId());
        } catch (Exception ex) {
            log.error("Failed to generate & save AI problem: parentId={}", parentProblemId, ex);
        }
    }
}
