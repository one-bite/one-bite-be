package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.usertopic.UserTopicResponse;
import code.rice.bowl.spaghetti.mapper.UserTopicMapper;
import code.rice.bowl.spaghetti.repository.UserTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTopicService {

    private final UserTopicRepository userTopicRepository;

    public List<UserTopicResponse> findByUserId(Long userId) {
        return userTopicRepository.findByUserUserId(userId).stream()
                .map(UserTopicMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserTopicResponse> findByTopicId(Long topicId) {
        return userTopicRepository.findByTopicTopicId(topicId).stream()
                .map(UserTopicMapper::toDto)
                .collect(Collectors.toList());
    }
}