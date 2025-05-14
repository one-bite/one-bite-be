package code.rice.bowl.spaghetti.dto.problem;

import code.rice.bowl.spaghetti.entity.Problem.QuestionType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 문제 상세를 요청할 때 사용.
 */
@Setter
@Getter
@Builder
public class ProblemDetailResponse {
    Long problemId;
    QuestionType questionType;
    String title;
    JsonNode description;
    String answer;
    int point;
}
