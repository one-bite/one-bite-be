package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.user.UserPatchRequest;
import code.rice.bowl.spaghetti.dto.response.SimpleOkResponse;
import code.rice.bowl.spaghetti.service.UserBadgeService;
import code.rice.bowl.spaghetti.service.UserProgressService;
import code.rice.bowl.spaghetti.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 모든 요청이 로그인을 요구하기에 db 에서 분리.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserBadgeService userBadgeService;
    private final UserProgressService userProgressService;

    // 현재 로그인한 사용자가 자신의 정보를 요청할 때
    @GetMapping("")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal(expression = "username") String email) {
        return ResponseEntity.ok(userService.getUserAllInfo(email));
    }

    // 현재 로그인한 사용자의 획득한 모든 뱃지의 정보 요청
    @GetMapping("/badges")
    public ResponseEntity<?> getBadges(@AuthenticationPrincipal(expression = "username") String email) {
        return ResponseEntity.ok(userBadgeService.getUserBadges(email));
    }

    // 현재 로그인한 사용자의 각 토픽의 진행도 정보 요청
    @GetMapping("/topic-progress")
    public ResponseEntity<?> getTopicProgress(@AuthenticationPrincipal(expression = "username") String email) {
        return ResponseEntity.ok(userProgressService.getAllProgress(email));
    }

    // 그 외 일반적으로 타인의 정보를 요청할 때 -> 나중에 필요할 듯.
//    @GetMapping("/{email}")
//    public ResponseEntity<?> getOtherInfo(@PathVariable String email) {
//        return ResponseEntity.ok(new SimpleOkResponse("none"));
//    }

    // 이름 변경.
    @PatchMapping("")
    public ResponseEntity<?> modifyName(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody UserPatchRequest update) {
        userService.updateNickname(email, update);

        return ResponseEntity.ok(new SimpleOkResponse("ok"));
    }

    // 회원 탈퇴
    @DeleteMapping("")
    public ResponseEntity<?> delete(@AuthenticationPrincipal(expression = "username") String email) {
        userService.delete(email);

        return ResponseEntity.ok(new SimpleOkResponse("ok"));
    }
}
