package code.rice.bowl.spaghetti.entity;

import code.rice.bowl.spaghetti.utils.JsonNodeConverter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ai_generated_problems")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiGeneratedProblem {

    public enum DifficultyLevel {
        EASY("초급"),
        MEDIUM("중급"),
        HARD("고급");

        private final String koreanLabel;

        DifficultyLevel(String koreanLabel) {
            this.koreanLabel = koreanLabel;
        }
    }

    public enum QuestionType {
        multiple_choice, short_answer, true_false
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Convert(converter = JsonNodeConverter.class)
    private JsonNode description;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;

    private String hint;

    private String answer;

    @Convert(converter = JsonNodeConverter.class)
    private JsonNode features;

    @Column(nullable = false)
    private int score;
}
