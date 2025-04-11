package code.rice.bowl.spaghetti;

import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.repository.ProblemRepository;
import code.rice.bowl.spaghetti.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;

    ObjectMapper mapper = new ObjectMapper();
    JsonNode json;

    {
        try {
            json = mapper.readTree("{\"question\":\"반복문은?\", \"options\": [\"1. if문\", \"2. for문\"]}");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                    .email("test@example.com")
                    .username("테스트 유저")
                    .points(0)
                    .rating(0)
                    .build());
        }

        if (problemRepository.count() == 0) {
            problemRepository.save(Problem.builder()
                    .title("Test Problem")
                    .description(json)
                    .answer("2")
                    .score(100)
                    .questionType(Problem.QuestionType.multiple_choice)
                    .difficulty(Problem.DifficultyLevel.EASY)
                    .build());

        }
    }
}
