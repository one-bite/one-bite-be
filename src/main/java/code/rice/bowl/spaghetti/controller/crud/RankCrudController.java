package code.rice.bowl.spaghetti.controller.crud;

import code.rice.bowl.spaghetti.dto.rank.RankRequest;
import code.rice.bowl.spaghetti.dto.rank.RankSimpleResponse;
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
@RequestMapping("db/rank")
@Tag(name = "CRUD: Rank (사용자 티어)")
public class RankCrudController {

   private final RankService rankService;

    // 랭크 추가
    @PostMapping("")
    ResponseEntity<?> create(@RequestBody RankRequest dto) {
        rankService.create(dto);

        return ResponseEntity.ok(new SimpleOkResponse("ok"));
    }

    // 랭크 수정
    @PutMapping("/{id}")
    ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody RankRequest dto) {
        rankService.update(id, dto);
        return ResponseEntity.ok(new SimpleOkResponse("ok"));
    }

    // 랭크 삭제
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        rankService.delete(id);

        return ResponseEntity.ok(new SimpleOkResponse("ok"));
    }

    // 랭크 전체 조회
    @GetMapping("")
    @Operation(summary = "전체 level id, name 만 조회 함.")
    ResponseEntity<?> selectAll() {
        List<RankSimpleResponse> result = rankService.selectAllSimple();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> select(@PathVariable Long id) {
        return ResponseEntity.ok(rankService.select(id));
    }


}
