package code.rice.bowl.spaghetti.dto.badge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 용도
 * - Badge 조회 시 반환하는 객체
 */
@Getter
@Builder
@AllArgsConstructor
public class BadgeResponse {
    private Long badgeId;
    private String name;
    private String description;
    private String condition;
    private String imageUrl;
}
