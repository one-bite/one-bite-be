package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.Admin;
import code.rice.bowl.spaghetti.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
}
