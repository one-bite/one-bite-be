package code.rice.bowl.spaghetti.dto.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 용도
 * - 사용자 정보 업데이트 (현재 : 닉네임 변경)
 */
@Getter
@Setter
public class UserPatchRequest {
    private String nickname;
}
