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
@Table(name = "problems")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Problem {

    @Getter
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
    private Long problemId;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

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
