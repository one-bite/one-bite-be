package code.rice.bowl.spaghetti.dto.problem;


import code.rice.bowl.spaghetti.utils.QuestionType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 용도
 * - 문제 추가 요청
 * - 문제 수정 요청
 */
@Getter
@Setter
@NoArgsConstructor
public class ProblemRequest {

    private Long categoryId;

    @NotNull
    private String title;

    private JsonNode description;

    private QuestionType questionType;

    private String hint;

    private String answer;

    @NotNull
    private int point = 10;

    private Long userId;

    // 숫자 ID 대신 문자열 코드 리스트 사용
    private List<String> topicCodes;
}