package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.BadgeDto;
import code.rice.bowl.spaghetti.dto.response.BadgeResponse;
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

    public static void updateEntity(Badge badge, BadgeDto dto) {
        badge.setName(dto.getName());
        badge.setDescription(dto.getDescription());
        badge.setCondition(dto.getCondition());
        badge.setImageUrl(dto.getImageUrl());
    }
}