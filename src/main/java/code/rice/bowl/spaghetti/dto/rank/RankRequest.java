package code.rice.bowl.spaghetti.dto.rank;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 용도
 * - 랭크 추가 요청
 * - 랭크 변경 요청
 */
@Getter
@Setter
@Builder
public class RankRequest {
    @NotNull
    String name;
    @NotNull
    int minRating;
    @NotNull
    int maxRating;
}
