package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.user.UserPatchRequest;
import code.rice.bowl.spaghetti.dto.response.SimpleOkResponse;
import code.rice.bowl.spaghetti.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 모든 요청이 로그인을 요구하기에 db 에서 분리.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "About User")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserBadgeService userBadgeService;
    private final UserProgressService userProgressService;
    private final TodayProblemService todayProblemService;
    private final StreakService streakService;
    private final UserProblemHistoryService userProblemHistoryService;

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

    // 사용자의 오늘의 문제 조회.
    @GetMapping("/today")
    public ResponseEntity<?> getTodayProblems(@AuthenticationPrincipal(expression = "username") String email) {
        return ResponseEntity.ok(todayProblemService.getUserTodayProblems(email));
    }

    // 사용자의 스트릭 정보를 요청.
    @GetMapping("/streak")
    public ResponseEntity<?> getStreak(@AuthenticationPrincipal(expression = "username") String email) {
        return ResponseEntity.ok(streakService.getStreak(email));
    }

    // 사용자의 제출 기록을 요청.
    @GetMapping("/history")
    public ResponseEntity<?> getSubmitHistory(@AuthenticationPrincipal(expression = "username") String email) {
        return ResponseEntity.ok(userProblemHistoryService.getHistoriesByUser(email));
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
