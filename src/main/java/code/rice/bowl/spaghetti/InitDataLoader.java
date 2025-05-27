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
            String[] names = {
                    "Bronze 5",
                    "Bronze 4",
                    "Bronze 3",
                    "Bronze 2",
                    "Bronze 1",
                    "Silver 5",
                    "Silver 4",
                    "Silver 3",
                    "Silver 2",
                    "Silver 1",
                    "Gold 5",
                    "Gold 4",
                    "Gold 3",
                    "Gold 2",
                    "Gold 1",
                    "Diamond 5",
                    "Diamond 4",
                    "Diamond 3",
                    "Diamond 2",
                    "Diamond 1",
                    "Master 5",
                    "Master 4",
                    "Master 3",
                    "Master 2",
                    "Master 1",
                    "Challenger"
            };

            rankRepository.save(Rank.builder()
                    .name("Unranked")
                    .minRating(0)
                    .maxRating(0)
                    .build()
            );

            int s = 1;
            for (String n: names) {
                rankRepository.save(Rank.builder()
                        .name(n)
                        .minRating(s)
                        .maxRating(s + 99)
                        .build()
                );
                s += 100;
            }
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
