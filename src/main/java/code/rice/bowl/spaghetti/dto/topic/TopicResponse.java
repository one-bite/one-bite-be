package code.rice.bowl.spaghetti.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TopicResponse {
    private Long topicId;
    private String name;
    private String description;
    private int total;
}