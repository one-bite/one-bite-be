package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.response.UserProblemHistoryResponse;
import code.rice.bowl.spaghetti.dto.response.UserProblemHistorySummaryResponse;
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

    private final UserProblemHistoryRepository historyRepository;

    public List<UserProblemHistoryResponse> getHistoriesByUserId(Long userId) {
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