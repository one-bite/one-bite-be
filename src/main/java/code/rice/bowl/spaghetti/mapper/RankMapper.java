package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.level.LevelRequest;
import code.rice.bowl.spaghetti.dto.level.LevelResponse;
import code.rice.bowl.spaghetti.entity.Rank;



public class RankMapper {

    public static LevelRequest toDto(Rank user) {
        return LevelRequest.builder()
                .name(user.getName())
                .maxRating(user.getMaxRating())
                .minRating(user.getMinRating())
                .build();
    }

    public static Rank dtoToEntity(LevelRequest dto) {
        return Rank.builder()
                .name(dto.getName())
                .minRating(dto.getMinRating())
                .maxRating(dto.getMaxRating())
                .build();
    }

    public static LevelResponse dtoToLevelResponse(Rank rank) {
        return LevelResponse.builder()
                .id(rank.getRankId())
                .name(rank.getName())
                .minRating(rank.getMinRating())
                .maxRating(rank.getMaxRating())
                .build();
    }
}
