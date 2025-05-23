package code.rice.bowl.spaghetti.dto.problem;

import code.rice.bowl.spaghetti.entity.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 용도
 * - 문제 ID, 제목, 점수 정보만 제공
 */
@Getter
@Builder
@AllArgsConstructor
public class ProblemSimpleResponse {
    private Long problemId;
    private String title;
    private List<Topic> topics;  // 문제 점수
}