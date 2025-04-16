package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.Badge;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
}
