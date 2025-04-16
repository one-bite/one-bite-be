package code.rice.bowl.spaghetti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserTopicResponse {
    private Long userId;
    private Long topicId;
}