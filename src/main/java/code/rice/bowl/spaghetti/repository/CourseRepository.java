package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
//    Optional<Course> findById(Long id);
}
