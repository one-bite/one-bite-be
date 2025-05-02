package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.TodayProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodayProblemRepository extends JpaRepository<TodayProblem, Long> {
    Optional<TodayProblem> findByUser_IdAndProblem_Id(Long userId, Long problemId);
}
