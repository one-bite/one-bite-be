package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

}
