package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    long countByUserIsNull();

    Optional<Problem> findByTitle(String title);
}
