package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.Topic;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.entity.UserProgress;
import code.rice.bowl.spaghetti.entity.UserProgressId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProgressRepository extends JpaRepository<UserProgress, UserProgressId> {
    List<UserProgress> findByUser(User user);
    List<UserProgress> findByTopic(Topic topic);
}
