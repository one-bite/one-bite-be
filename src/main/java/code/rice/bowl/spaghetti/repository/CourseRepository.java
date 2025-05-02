package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
