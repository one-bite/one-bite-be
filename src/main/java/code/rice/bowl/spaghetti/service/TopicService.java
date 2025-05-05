package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.topic.TopicRequest;
import code.rice.bowl.spaghetti.dto.topic.TopicResponse;
import code.rice.bowl.spaghetti.entity.Topic;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.mapper.TopicMapper;
import code.rice.bowl.spaghetti.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Topic topic = getTopic(id);
        return TopicMapper.toDto(topic);
    }

    public TopicResponse update(Long id, TopicRequest dto) {
        Topic topic = getTopic(id);

        topic.setCode(dto.getCode());     // 코드 업데이트
        topic.setName(dto.getName());
        topic.setDescription(dto.getDescription());
        topic.setTotal(dto.getTotal());

        return TopicMapper.toDto(topicRepository.save(topic));
    }

    @Transactional
    public void delete(Long id) {
        topicRepository.deleteById(id);
    }

    public Topic getTopic(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Topic not found"));
    }
}
