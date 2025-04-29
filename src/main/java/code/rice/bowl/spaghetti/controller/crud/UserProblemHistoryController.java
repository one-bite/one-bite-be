package code.rice.bowl.spaghetti.controller.crud;

import code.rice.bowl.spaghetti.dto.userproblemhistory.UserProblemHistoryResponse;
import code.rice.bowl.spaghetti.dto.userproblemhistory.UserProblemHistorySummaryResponse;
import code.rice.bowl.spaghetti.service.UserProblemHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/history")
@RequiredArgsConstructor
@Tag(name = "UserProblemHistory")
public class UserProblemHistoryController {

    private final UserProblemHistoryService historyService;

    // 사용자의 문제 풀이 전체 기록 조회
    @GetMapping("/user/{userId}")
    @Operation(summary = "문제풀이 기록 조회")
    public ResponseEntity<List<UserProblemHistoryResponse>> getUserHistories(@PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getHistoriesByUserId(userId));
    }

    // 프로필 화면 통계 제공 (푼 문제 수, 맞은 문제 수, 정답률)
    @GetMapping("/user/{userId}/summary")
    @Operation(summary = "특정 사용자의 문제 풀이 요약 통계 조회")
    public ResponseEntity<UserProblemHistorySummaryResponse> getUserSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getSummaryByUserId(userId));
    }

    // 프로필 또는 대시보드 개제용으로 제작. 나중에 넣을수도?
//    @GetMapping("/user/{userId}/recent")
//    @Operation(summary = "사용자의 문제풀이 기록 조회 (최근 10개)")
//    public ResponseEntity<List<UserProblemHistoryResponse>> getRecentUserHistories(@PathVariable Long userId) {
//        return ResponseEntity.ok(historyService.getRecentHistoriesByUserId(userId));
//    }
}
