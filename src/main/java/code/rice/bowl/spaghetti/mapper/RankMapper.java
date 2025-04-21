package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.rank.RankRequest;
import code.rice.bowl.spaghetti.dto.rank.RankResponse;
import code.rice.bowl.spaghetti.entity.Rank;



public class RankMapper {

    public static RankRequest toDto(Rank user) {
        return RankRequest.builder()
                .name(user.getName())
                .maxRating(user.getMaxRating())
                .minRating(user.getMinRating())
                .build();
    }

    public static Rank dtoToEntity(RankRequest dto) {
        return Rank.builder()
                .name(dto.getName())
                .minRating(dto.getMinRating())
                .maxRating(dto.getMaxRating())
                .build();
    }

    public static RankResponse dtoToLevelResponse(Rank rank) {
        return RankResponse.builder()
                .id(rank.getRankId())
                .name(rank.getName())
                .minRating(rank.getMinRating())
                .maxRating(rank.getMaxRating())
                .build();
    }
}
