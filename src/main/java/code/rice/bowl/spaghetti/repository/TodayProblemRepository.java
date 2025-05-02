package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.TodayProblem;
import code.rice.bowl.spaghetti.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TodayProblemRepository extends JpaRepository<TodayProblem, Long> {
    Optional<TodayProblem> findByUser_UserIdAndProblem_ProblemId(Long userId, Long problemId);
}
