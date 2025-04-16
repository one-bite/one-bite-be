package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.badge.BadgeDto;
import code.rice.bowl.spaghetti.dto.badge.BadgeResponse;
import code.rice.bowl.spaghetti.entity.Badge;

public class BadgeMapper {

    public static Badge toEntity(BadgeDto dto) {
        return Badge.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .condition(dto.getCondition())
                .imageUrl(dto.getImageUrl())
                .build();
    }

    public static BadgeResponse toDto(Badge badge) {
        return BadgeResponse.builder()
                .badgeId(badge.getBadgeId())
                .name(badge.getName())
                .description(badge.getDescription())
                .condition(badge.getCondition())
                .imageUrl(badge.getImageUrl())
                .build();
    }
}