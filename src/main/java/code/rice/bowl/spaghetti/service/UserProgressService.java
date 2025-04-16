package code.rice.bowl.spaghetti.service;


import code.rice.bowl.spaghetti.dto.response.UserProgressResponse;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.entity.UserProgress;
import code.rice.bowl.spaghetti.entity.UserProgressId;
import code.rice.bowl.spaghetti.mapper.UserProgressMapper;
import code.rice.bowl.spaghetti.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProgressService {

    private final UserProgressRepository userProgressRepository;

    private final UserService userService;
    private final TopicService topicService;

    @Transactional
    public void increaseTopicCount(Long userId, Long topicId) {
        UserProgress userProgress = getUserProgress(userId, topicId);

        userProgress.setCompletedProblems(userProgress.getCompletedProblems() + 1);
    }

    /**
     * 사용자의 현재 모든 topic에 대한 진행도를 반환함.
     */
    public List<UserProgressResponse> getAllProgress(String email) {
        // 1. 사용자 조회
        User nowUser = userService.getUser(email);

        // 2. 현재 진행도 조회
        List<UserProgress> userProgresses = userProgressRepository.findByUser(nowUser);

        // 3. 리턴 값 처리.
        return userProgresses.stream()
                .map(UserProgressMapper::toUserProgressResponse)
                .collect(Collectors.toList());
    }

    public UserProgress getUserProgress(Long userId, Long topicId) {
        UserProgressId id = new UserProgressId(userId, topicId);

        return userProgressRepository.findById(id)
                .orElseGet(() -> {
                    UserProgress newTopic = new UserProgress();

                    newTopic.setUser(userService.getUser(userId));
                    newTopic.setTopic(topicService.getTopic(topicId));

                    userProgressRepository.save(newTopic);

                    return newTopic;
                });
    }
}
