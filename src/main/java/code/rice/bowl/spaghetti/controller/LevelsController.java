package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.level.LevelRequest;
import code.rice.bowl.spaghetti.dto.level.LevelSimpleResponse;
import code.rice.bowl.spaghetti.dto.response.SimpleOkResponse;
import code.rice.bowl.spaghetti.service.RankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("db/levels")
@Tag(name = "CRUD: Levels (레벨)")
public class LevelsController {

   private final RankService rankService;

    // 레벨 추가
    @PostMapping("")
    ResponseEntity<?> create(@RequestBody LevelRequest dto) {
        rankService.create(dto);

        return ResponseEntity.ok(new SimpleOkResponse("ok"));
    }

    // 레벨 수정
    @PutMapping("/{id}")
    ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody LevelRequest dto) {
        rankService.update(id, dto);
        return ResponseEntity.ok(new SimpleOkResponse("ok"));
    }

    // 레벨 삭제
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        rankService.delete(id);

        return ResponseEntity.ok(new SimpleOkResponse("ok"));
    }

    // 레벨 전체 조회
    @GetMapping("")
    @Operation(summary = "전체 level id, name 만 조회 함.")
    ResponseEntity<?> selectAll() {
        List<LevelSimpleResponse> result = rankService.selectAllSimple();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> select(@PathVariable Long id) {
        return ResponseEntity.ok(rankService.select(id));
    }


}
