package code.rice.bowl.spaghetti.entity;

import code.rice.bowl.spaghetti.utils.JsonNodeConverter;
import code.rice.bowl.spaghetti.utils.QuestionType;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "problems")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "problem_topic",
            joinColumns = @JoinColumn(name = "problem_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_code")
    )
    @Builder.Default
    private List<Topic> topics = new ArrayList<>();

    /**
     * 문제를 생성한 사용자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Convert(converter = JsonNodeConverter.class)
    private JsonNode description;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    private String hint;

    private String answer;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 10")
    @Builder.Default
    private int point = 10;

}
