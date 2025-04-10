package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.LevelDto;
import code.rice.bowl.spaghetti.entity.Level;



public class LevelMapper {

    public static LevelDto toDto(Level user) {
        return LevelDto.builder()
                .name(user.getName())
                .maxRating(user.getMaxRating())
                .minRating(user.getMinRating())
                .build();
    }

    public static Level toEntity(LevelDto dto) {
        return Level.builder()
                .name(dto.getName())
                .minRating(dto.getMinRating())
                .maxRating(dto.getMaxRating())
                .build();
    }
}
