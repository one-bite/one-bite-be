package code.rice.bowl.spaghetti.dto.problem;

import code.rice.bowl.spaghetti.entity.Problem.QuestionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * 용도
 * - 문제 추가 요청
 * - 문제 수정 요청
 */
@Getter
@NoArgsConstructor
public class ProblemRequest {

    @NotNull
    private Long categoryId; // 기존 topicId -> categoryId로 변경

    @NotNull
    private String title;

    private JsonNode description;

    private QuestionType questionType;

    private String hint;

    private String answer;

    @NotNull
    private int point = 10;

    private Long userId;

    private Long[] topicIds;
}