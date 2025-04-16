package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.topic.TopicDto;
import code.rice.bowl.spaghetti.dto.topic.TopicResponse;
import code.rice.bowl.spaghetti.service.TopicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/topics")
@RequiredArgsConstructor
@Tag(name = "CRUD: Topic (주제)")
public class TopicController {

    private final TopicService topicService;

    // 추가
    @PostMapping
    public ResponseEntity<TopicResponse> create(@RequestBody TopicDto dto) {
        return ResponseEntity.ok(topicService.create(dto));
    }

    // 조회
    @GetMapping
    public ResponseEntity<List<TopicResponse>> findAll() {
        return ResponseEntity.ok(topicService.findAll());
    }

    // 조회 (단일 개체)
    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.findById(id));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<TopicResponse> update(@PathVariable Long id, @RequestBody TopicDto dto) {
        return ResponseEntity.ok(topicService.update(id, dto));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        topicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
