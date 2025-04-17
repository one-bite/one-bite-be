package code.rice.bowl.spaghetti.service;


import code.rice.bowl.spaghetti.dto.userprogress.UserProgressResponse;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.entity.UserProgress;
import code.rice.bowl.spaghetti.entity.UserProgressId;
import code.rice.bowl.spaghetti.mapper.UserProgressMapper;
import code.rice.bowl.spaghetti.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProgressService {

    private final UserProgressRepository userProgressRepository;

    private final UserService userService;
    private final TopicService topicService;

    /**
     * 해당 토픽의 문제를 해결할 경우, 해당 사용자의 토픽 해결 수 증가.
     * @param userId    문제 해결한 사용자 id
     * @param topicId   해결한 문제의 토픽 id
     */
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

    /**
     * 사용자의 현재 토픽에 대한 진행도 반환.
     * - 문제를 풀 경우에만 호출 됨.
     *
     * @param userId    사용자 id
     * @param topicId   토픽 id
     * @return UserProgress
     */
    public UserProgress getUserProgress(Long userId, Long topicId) {
        UserProgressId id = new UserProgressId(userId, topicId);

        // 조회 후 데이터가 없으면 데이터를 생성함. 기본 값을 0으로 초기화.
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
