package code.rice.bowl.spaghetti.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 용도
 * - 토픽 조회 시 반환하는 객체
 */
@Getter
@Builder
@AllArgsConstructor
public class TopicResponse {
    private Long topicId;
    private String code;
    private String name;
    private String description;
    private int total;
}