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
        if (categoryRepository.count() == 0) {
            categoryRepository.save(
                    Category.builder()
                            .categoryName("Python")
                            .description("파이썬")
                            .total(0)
                            .build());
        }
    }
}
