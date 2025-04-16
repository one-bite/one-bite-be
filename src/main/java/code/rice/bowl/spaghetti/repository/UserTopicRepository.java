package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.UserTopic;
import code.rice.bowl.spaghetti.entity.UserTopicId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic, UserTopicId> {
    List<UserTopic> findByUserUserId(Long userId);
    List<UserTopic> findByTopicTopicId(Long topicId);
}