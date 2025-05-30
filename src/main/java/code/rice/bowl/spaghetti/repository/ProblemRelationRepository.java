package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.ProblemRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProblemRelationRepository extends JpaRepository<ProblemRelation, Long> {
    /** 부모 문제로부터 파생된 AI 문제 조회 */
    List<ProblemRelation> findAllByParentProblem_ProblemId(Long parentProblemId);

    Optional<Object> findByParentProblem_ProblemIdAndChildProblem_ProblemId(Long parentId, Long childId);
}