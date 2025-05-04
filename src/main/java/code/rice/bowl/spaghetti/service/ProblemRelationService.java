package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.entity.ProblemRelation;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.repository.ProblemRelationRepository;
import code.rice.bowl.spaghetti.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemRelationService {

    private final ProblemRelationRepository relationRepository;
    private final ProblemRepository problemRepository;

    /**
     * 부모 문제 ID와 자식 문제 ID를 받아 관계를 생성 저장
     * @param parentId 부모 문제 ID
     * @param childId 자식 문제 ID
     */
    @Transactional
    public void createRelation(Long parentId, Long childId) {
        // 부모 문제와 자식 문제가 존재하는지 검사
        relationRepository.findByParentProblem_ProblemIdAndChildProblem_ProblemId(parentId, childId)
                .ifPresent(r -> {
                    throw new InvalidRequestException("Relation already exists");
                });

        relationRepository.save(
                ProblemRelation.builder()
                        .parentProblem(problemRepository.findById(parentId)
                                .orElseThrow(() -> new InvalidRequestException("Parent problem not found: " + parentId)))
                        .childProblem(problemRepository.findById(childId)
                                .orElseThrow(() -> new InvalidRequestException("Child problem not found: " + childId)))
                        .build()
        );
    }

}
