package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.userproblemhistory.UserProblemHistoryResponse;
import code.rice.bowl.spaghetti.dto.userproblemhistory.UserProblemHistorySummaryResponse;
import code.rice.bowl.spaghetti.entity.UserProblemHistory;
import code.rice.bowl.spaghetti.mapper.UserProblemHistoryMapper;
import code.rice.bowl.spaghetti.repository.UserProblemHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProblemHistoryService {

    private final UserService userService;

    private final UserProblemHistoryRepository historyRepository;

    /**
     * 사용자 이메일 바탕으로 사용자 제출 기록 조회.
     * @param email 사용자 이메일
     * @return  제출 기록.
     */
    public List<UserProblemHistoryResponse> getHistoriesByUser(String email) {
        return getHistoriesByUser(userService.getUser(email).getUserId());
    }

    /**
     * 사용자 아이디 바탕으로 사요자 제출 기록 조회.
     * @param userId 사용자 id
     * @return  제출 기록.
     */
    public List<UserProblemHistoryResponse> getHistoriesByUser(Long userId) {
        return historyRepository.findByUserUserId(userId).stream()
                .map(UserProblemHistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserProblemHistoryResponse> getRecentHistoriesByUserId(Long userId) {
        return historyRepository.findTop10ByUserUserIdOrderBySubmittedAtDesc(userId).stream()
                .map(UserProblemHistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserProblemHistoryResponse> getHistoriesByProblemId(Long problemId) {
        return historyRepository.findByProblemProblemId(problemId).stream()
                .map(UserProblemHistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserProblemHistorySummaryResponse getSummaryByUserId(Long userId) {
        List<UserProblemHistory> histories = historyRepository.findByUserUserId(userId);

        int total = histories.size();
        int correct = (int) histories.stream().filter(UserProblemHistory::getIsCorrect).count();
        double avgTime = histories.stream()
                .mapToInt(UserProblemHistory::getSolveTime)
                .average()
                .orElse(0);
        double accuracy = total == 0 ? 0 : (correct * 100.0 / total);

        return new UserProblemHistorySummaryResponse(total, correct, accuracy, avgTime);
    }
}