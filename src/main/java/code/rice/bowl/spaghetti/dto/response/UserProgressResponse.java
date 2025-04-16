package code.rice.bowl.spaghetti.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserProgressResponse {
    String topicName;
    int solveCount;
    int totalProblem;
}
