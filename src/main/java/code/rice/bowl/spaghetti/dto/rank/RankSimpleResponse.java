package code.rice.bowl.spaghetti.dto.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 용도
 * - 랭크 이름과 ID 만 조회
 */
@AllArgsConstructor
@Getter
public class RankSimpleResponse {
    private Long id;
    private String name;
}
