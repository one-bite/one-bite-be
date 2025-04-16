package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.response.UserTopicResponse;
import code.rice.bowl.spaghetti.service.UserTopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/user-topic")
@RequiredArgsConstructor
@Tag(name = "UserTopic")
public class UserTopicController {

    private final UserTopicService userTopicService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 토픽 조회")
    public ResponseEntity<List<UserTopicResponse>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userTopicService.findByUserId(userId));
    }

    // 필요 없을수도? 또는 단순히 사용자 수만 count 해서 조회해야 할 수도 있을듯.
    @GetMapping("/topic/{topicId}")
    @Operation(summary = "토픽 id로 사용자 조회")
    public ResponseEntity<List<UserTopicResponse>> getByTopicId(@PathVariable Long topicId) {
        return ResponseEntity.ok(userTopicService.findByTopicId(topicId));
    }
}
