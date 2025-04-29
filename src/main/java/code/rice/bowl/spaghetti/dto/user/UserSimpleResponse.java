package code.rice.bowl.spaghetti.dto.user;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 용도
 * - 배지 보유한 사람의 정보 요청시 제공하는 사용자 데이터
 */
@Getter
@Setter
@Builder
public class UserSimpleResponse {
    String name;
    String level;
}
