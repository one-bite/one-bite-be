package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.problem.ProblemDetailResponse;
import code.rice.bowl.spaghetti.dto.user.UserTodayProblemResponse;
import code.rice.bowl.spaghetti.entity.*;
import code.rice.bowl.spaghetti.exception.NotFoundException;
import code.rice.bowl.spaghetti.mapper.ProblemMapper;
import code.rice.bowl.spaghetti.repository.TodayProblemRepository;
import code.rice.bowl.spaghetti.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodayProblemService {

    private final TodayProblemRepository todayProblemRepository;

    private final UserService userService;
    private final CourseService courseService;

    // 오늘의 문제로 코스 문제와 AI 문제 개수 설정.
    private final static long COURSE_PROBLEM_CNT = 10;
    private final static long AI_PROBLEM_CNT = 3;

    /**
     * 오늘 문제에 대하여 제출 여부를 설정함.
     *
     * @param userId    사용자 id.
     * @param problemId 오늘 해결한 문제 id.
     */
    @Transactional
    public void setSubmit(Long userId, Long problemId) {
        TodayProblem todayProblem = todayProblemRepository.findByUser_UserIdAndProblem_ProblemId(userId, problemId)
                .orElseThrow(() -> new NotFoundException("not today problem"));

        // 해당 문제 ID에 대하여
        todayProblem.setSubmitYN(true);

        // 해당 문제의 생성자 확인.
        Problem p = todayProblem.getProblem();

        // AI가 만든 문제인 경우.
        // 제출 기록이 있으므로 NotSolve 테이블에서 삭제.
        if (p.getUser() != null) {
            todayProblem.getUser().getNotSolveAiProblems().removeIf(c -> c.getProblem().equals(p));
        }

        // todayProblemRepository.save(todayProblem);
    }

    /**
     * 사용자의 오늘 풀어할 문제들을 반환. (없는 경우 -> 생성)
     * 
     * @param email 사용자 이메일
     * @return 풀어야 하는 문제 리스트및 풀이 여부.
     */
    public UserTodayProblemResponse getUserTodayProblems(String email) {
        User now = userService.getUser(email);

        // 0. 오늘의 문제를 다 풀었지는 지 확인. (이미 스티릭 달성 여부 확인)
        if (now.getStreak().getUpdatedAt().equals(DateUtils.today())) {
            return null;
        }

        List<TodayProblem> todayProblems;

        // 1. 오늘에 풀어야 하는 문제 필요한 경우. (생성)
        if (allSolve(now.getUserId())) {
            todayProblems = createTodayProblems(now);
        } else {
            todayProblems = now.getTodayProblems();
        }

        List<Boolean> isSolved = new ArrayList<>();
        List<ProblemDetailResponse> problems = new ArrayList<>();

        for (TodayProblem tp : todayProblems) {
            isSolved.add(tp.isSubmitYN());
            problems.add(ProblemMapper.toDetailDto(tp.getProblem()));
        }

        return UserTodayProblemResponse.builder()
                .problemStatus(isSolved)
                .problemList(problems)
                .build();
    }

    public UserTodayProblemResponse getUserTodayLogs(String email) {
        User now = userService.getUser(email);

        List<TodayProblem> todayProblems;

        if (now.getTodayProblems().isEmpty()) {
            todayProblems = createTodayProblems(now);
        } else {
            todayProblems =  now.getTodayProblems();
        }

        List<Boolean> isSolved = new ArrayList<>();
        List<ProblemDetailResponse> problems = new ArrayList<>();

        for (TodayProblem tp : todayProblems) {
            isSolved.add(tp.isSubmitYN());
            problems.add(ProblemMapper.toDetailDto(tp.getProblem()));
        }

        return UserTodayProblemResponse.builder()
                .problemStatus(isSolved)
                .problemList(problems)
                .build();
    }

    /**
     * 오늘의 문제에 대하여 모두 해결 여부를 나타냄.
     * 
     * @param userId 사용자 id
     * @return true (모든 문제에 대한 제출 기록 존재), false (한 문제이라도 제출 기록이 없음)
     */
    @Transactional
    public boolean allSolve(Long userId) {
        User user = userService.getUser(userId);

        // 0. 이미 스트릭이 증가 되었다면 오늘의 문제 다 풀었음을 의미.
        if (user.getStreak().getUpdatedAt().equals(DateUtils.today())) {
            return true;
        }

        List<TodayProblem> problems = user.getTodayProblems();

        // 한 문제이라도 제출 기록이 없으면 false.
        for (TodayProblem t : problems) {
            if (!t.isSubmitYN()) {
                return false;
            }
        }

        return true;
    }

    // 현재 사용자의 상태에 맞게 오늘 풀어야 하는 문제 데이터를 추가함.
    @Transactional
    private List<TodayProblem> createTodayProblems(User user) {

        if (allSolve(user.getUserId())) {
            // 모든 문제를 풀었으면 자동으로 해당 삭제.
            user.getTodayProblems().clear();
            user.setCourseId(user.getCourseId() + COURSE_PROBLEM_CNT);
        }

        long start = user.getCourseId();
        List<TodayProblem> userProblems = new ArrayList<>();

        // 코스 문제 가져오기.
        for (long i = start; i < start + COURSE_PROBLEM_CNT; i++) {
            Optional<Course> course = courseService.getCourseNullable(i);

            // 코스가 존재 X -> 모든 코스 문제를 풀었음을 의미.
            if (course.isEmpty())
                break;

            Problem p = course.get().getProblem();

            // AI가 생성한 문제(user != null)는 오늘 문제에 포함하지 않음
            if (p.getUser() != null) {
                continue;
            }

            userProblems.add(TodayProblem.builder()
                    .user(user)
                    .problem(p)
                    .build());
        }

        // 사용자가 만든 문제 중에서 AI 문제를 가져옴.
        List<NotSolveProblem> aiNotSolve = user.getNotSolveAiProblems();

        if (!aiNotSolve.isEmpty()) {
            for (int i = 0; i < Math.min(AI_PROBLEM_CNT, aiNotSolve.size()); i++) {
                userProblems.add(TodayProblem.builder()
                        .user(user)
                        .problem(aiNotSolve.get(i).getProblem())
                        .build());
            }
        }

        // 최종 데이터를 테이블에 추가.
        todayProblemRepository.saveAll(userProblems);
        return userProblems;
    }

}
