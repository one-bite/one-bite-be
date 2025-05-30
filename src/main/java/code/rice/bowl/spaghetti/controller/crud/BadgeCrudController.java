package code.rice.bowl.spaghetti.controller.crud;

import code.rice.bowl.spaghetti.dto.badge.BadgeRequest;
import code.rice.bowl.spaghetti.dto.badge.BadgeResponse;
import code.rice.bowl.spaghetti.service.BadgeService;
import code.rice.bowl.spaghetti.service.UserBadgeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/badges")
@RequiredArgsConstructor
@Tag(name = "CRUD: Badge (뱃지)")
public class BadgeCrudController {

    private final BadgeService badgeService;
    private final UserBadgeService userBadgeService;

    // 추가
    @PostMapping
    public ResponseEntity<BadgeResponse> create(@RequestBody BadgeRequest dto) {
        return ResponseEntity.ok(badgeService.create(dto));
    }

    // 조회
    @GetMapping
    public ResponseEntity<List<BadgeResponse>> findAll() {
        return ResponseEntity.ok(badgeService.findAll());
    }

    // 조회 (단일 개체)
    @GetMapping("/{id}")
    public ResponseEntity<BadgeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(badgeService.findById(id));
    }

    // 해당 뱃지를 보유하고 있는 사용자를 조회
    @GetMapping("/owner/{id}")
    public ResponseEntity<?> findBadgeOwner(@PathVariable Long id) {
        return ResponseEntity.ok(userBadgeService.getBadgesOwner(id));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<BadgeResponse> update(@PathVariable Long id, @RequestBody BadgeRequest dto) {
        return ResponseEntity.ok(badgeService.update(id, dto));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        badgeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
