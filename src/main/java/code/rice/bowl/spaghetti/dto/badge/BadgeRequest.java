package code.rice.bowl.spaghetti.dto.badge;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 용도
 * - 뱃지 추가 요청
 * - 뱃지 수정 요청
 */
@Getter
@NoArgsConstructor
public class BadgeRequest {
    @NotBlank
    private String name;

    private String description;

    private String condition;

    private String imageUrl;
}