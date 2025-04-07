package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.UserProblemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProblemHistoryRepository extends JpaRepository<UserProblemHistory, Long> {
}
