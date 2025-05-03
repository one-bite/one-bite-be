package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.entity.Course;
import code.rice.bowl.spaghetti.exception.NotFoundException;
import code.rice.bowl.spaghetti.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course Not found : " + id));
    }
}
