package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
}
