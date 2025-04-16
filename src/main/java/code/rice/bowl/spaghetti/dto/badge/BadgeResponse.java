package code.rice.bowl.spaghetti.dto.badge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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