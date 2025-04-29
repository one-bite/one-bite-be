package code.rice.bowl.spaghetti.dto.userprogress;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 용도
 * - 현재 사용자의 특정 토픽에 대한 진행도 정보를 요청할 때 사용.
 */
@Setter
@Getter
@Builder
public class UserProgressResponse {
    String topicName;
    int solveCount;
    int totalProblem;
}
