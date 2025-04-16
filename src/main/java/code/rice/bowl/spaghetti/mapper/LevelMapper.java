package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.level.LevelDto;
import code.rice.bowl.spaghetti.dto.level.LevelResponse;
import code.rice.bowl.spaghetti.entity.Level;



public class LevelMapper {

    public static LevelDto toDto(Level user) {
        return LevelDto.builder()
                .name(user.getName())
                .maxRating(user.getMaxRating())
                .minRating(user.getMinRating())
                .build();
    }

    public static Level dtoToEntity(LevelDto dto) {
        return Level.builder()
                .name(dto.getName())
                .minRating(dto.getMinRating())
                .maxRating(dto.getMaxRating())
                .build();
    }

    public static LevelResponse dtoToLevelResponse(Level level) {
        return LevelResponse.builder()
                .id(level.getLevelId())
                .name(level.getName())
                .minRating(level.getMinRating())
                .maxRating(level.getMaxRating())
                .build();
    }
}
