package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.BadgeDto;
import code.rice.bowl.spaghetti.dto.response.BadgeResponse;
import code.rice.bowl.spaghetti.service.BadgeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/badges")
@RequiredArgsConstructor
@Tag(name = "CRUD: Badge (뱃지)")
public class BadgeController {

    private final BadgeService badgeService;

    @PostMapping
    public ResponseEntity<BadgeResponse> create(@RequestBody BadgeDto dto) {
        return ResponseEntity.ok(badgeService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<BadgeResponse>> findAll() {
        return ResponseEntity.ok(badgeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadgeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(badgeService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BadgeResponse> update(@PathVariable Long id, @RequestBody BadgeDto dto) {
        return ResponseEntity.ok(badgeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        badgeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
