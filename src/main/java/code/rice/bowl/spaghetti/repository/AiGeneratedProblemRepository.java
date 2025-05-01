package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.AiGeneratedProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiGeneratedProblemRepository extends JpaRepository<AiGeneratedProblem, Long> {
}
