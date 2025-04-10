package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.request.TopicRequest;
import code.rice.bowl.spaghetti.dto.response.TopicResponse;
import code.rice.bowl.spaghetti.entity.Topic;
import code.rice.bowl.spaghetti.mapper.TopicMapper;
import code.rice.bowl.spaghetti.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicResponse create(TopicRequest dto) {
        Topic topic = TopicMapper.toEntity(dto);
        return TopicMapper.toDto(topicRepository.save(topic));
    }

    public List<TopicResponse> findAll() {
        return topicRepository.findAll().stream()
                .map(TopicMapper::toDto)
                .collect(Collectors.toList());
    }

    public TopicResponse findById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        return TopicMapper.toDto(topic);
    }

    public TopicResponse update(Long id, TopicRequest dto) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        TopicMapper.updateEntity(topic, dto);
        return TopicMapper.toDto(topicRepository.save(topic));
    }

    public void delete(Long id) {
        topicRepository.deleteById(id);
    }
}