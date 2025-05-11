package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.entity.Course;
import code.rice.bowl.spaghetti.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * 조회한 문제가 없더라도 에러 발생 X.
     * @param id 조회할 문제 번호.
     * @return  Course or None.
     */
    public Optional<Course> getCourseNullable(Long id) {
        return courseRepository.findById(id);
    }
}
