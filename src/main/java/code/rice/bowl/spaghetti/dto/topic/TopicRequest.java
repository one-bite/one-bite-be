package code.rice.bowl.spaghetti.dto.topic;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 용도
 * - 토픽 추가 요청
 * - 토픽 수정 요청
 */
@Getter
@NoArgsConstructor
public class TopicRequest {
    @NotBlank
    private String name;

    private String description;

    private int total;
}