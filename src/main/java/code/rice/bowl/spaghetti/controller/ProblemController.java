package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.ProblemDto;
import code.rice.bowl.spaghetti.dto.response.ProblemResponse;
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

    @PostMapping
    @Operation(summary = "문제 추가")
    public ResponseEntity<ProblemResponse> create(@RequestBody ProblemDto dto) {
        return ResponseEntity.ok(problemService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProblemResponse>> findAll() {
        return ResponseEntity.ok(problemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProblemResponse> update(@PathVariable Long id, @RequestBody ProblemDto dto) {
        return ResponseEntity.ok(problemService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        problemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
