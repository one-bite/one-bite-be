package code.rice.bowl.spaghetti.dto.rank;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 용도
 * - 랭크 전체 데이터 조회
 */
@Getter
@Setter
@Builder
public class RankResponse {
    Long id;
    String name;
    int minRating;
    int maxRating;
}
