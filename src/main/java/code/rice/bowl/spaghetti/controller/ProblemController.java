package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemSimpleResponse;
import code.rice.bowl.spaghetti.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/problems")
@RequiredArgsConstructor
@Tag(name = "CRUD: Problem (문제)")
public class ProblemController {

    private final ProblemService problemService;

    // 추가
    @PostMapping
    @Operation(summary = "문제 추가")
    public ResponseEntity<ProblemResponse> create(@RequestBody ProblemRequest dto) {
        return ResponseEntity.ok(problemService.create(dto));
    }

    // 조회
    @GetMapping
    @Operation(summary = "전체 문제 id, 제목, 점수만 조회")
    public ResponseEntity<List<ProblemSimpleResponse>> findAllSimple() {
        return ResponseEntity.ok(problemService.findAll());
    }

    // 조회 (단일 개체)
    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.findById(id));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<ProblemResponse> update(@PathVariable Long id, @RequestBody ProblemRequest dto) {
        return ResponseEntity.ok(problemService.update(id, dto));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        problemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
