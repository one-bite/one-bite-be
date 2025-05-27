package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.UserProblemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProblemHistoryRepository extends JpaRepository<UserProblemHistory, Long> {
    long countByUserUserIdAndProblemUserIsNull(Long userId);

    List<UserProblemHistory> findByUserUserId(Long userId);
    List<UserProblemHistory> findTop10ByUserUserIdOrderBySubmittedAtDesc(Long userId);
    List<UserProblemHistory> findByProblemProblemId(Long problemId);
}