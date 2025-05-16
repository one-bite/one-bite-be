package code.rice.bowl.spaghetti;

import code.rice.bowl.spaghetti.entity.*;
import code.rice.bowl.spaghetti.repository.*;
import code.rice.bowl.spaghetti.utils.QuestionType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RankRepository rankRepository;
    private final ProblemRepository problemRepository;
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        // 1) Rank 초기화
        if (rankRepository.count() == 0) {
            rankRepository.save(Rank.builder()
                    .name("test")
                    .minRating(0)
                    .maxRating(100)
                    .build());
        }

        // 2) Category 초기화
        Category defaultCat = categoryRepository.findByCategoryName("Python")
                .orElseGet(() -> categoryRepository.save(
                        Category.builder()
                                .categoryName("Python")
                                .description("파이썬")
                                .total(0)
                                .build()));

        // 3) User 초기화
        User testUser = userRepository.findByEmail("test@example.com")
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email("test@example.com")
                                .username("테스트 유저")
                                .points(0)
                                .rating(0)
                                .isNew(true)
                                .build()));

        // 4) Problem 초기화
        if (problemRepository.count() == 0) {
            JsonNode desc = mapper.readTree(
                    "{\"question\":\"반복문은?\",\"options\":[\"1. if문\",\"2. for문\"]}");
            Problem p = Problem.builder()
                    .title("Test Problem")
                    .description(desc)
                    .answer("2")
                    .point(100)
                    .questionType(QuestionType.MULTIPLE_CHOICE)
                    .category(defaultCat)
                    .build();
            problemRepository.save(p);

            // 5) Course 초기화
            Course c = courseRepository.save(Course.builder().problem(p).build());
            // User.courseId 업데이트
            testUser.setCourseId(c.getCourseId());
            userRepository.save(testUser);
        }

        // 6) 기존 레코드의 Null 카테고리/코스 채우기(이미 존재하던 데이터용)
        problemRepository.findAll().stream()
                .filter(pp -> pp.getCategory() == null)
                .forEach(pp -> {
                    pp.setCategory(defaultCat);
                    problemRepository.save(pp);
                });
        userRepository.findAll().stream()
                .filter(u -> u.getCourseId() == 1)
                .forEach(u -> {
                    u.setCourseId(1L); // 기본 코스 ID
                    userRepository.save(u);
                });

        if (topicRepository.count() == 0) {
            topicRepository.save(Topic.builder()
                    .code("LOOP") // 고유 코드 값
                    .name("반복문")
                    .description("Loop 문에 대한 기본 문제 모음")
                    .total(0)
                    .build());
            topicRepository.save(Topic.builder()
                    .code("COND")
                    .name("조건문")
                    .description("조건문(if/switch) 문제 모음")
                    .total(0)
                    .build());
            topicRepository.save(Topic.builder()
                    .code("FUNC")
                    .name("함수")
                    .description("함수 정의 및 호출 문제 모음")
                    .total(0)
                    .build());
        }
    }
}
