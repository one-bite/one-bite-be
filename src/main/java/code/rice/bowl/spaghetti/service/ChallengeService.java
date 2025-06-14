package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.ChallengeDto;
import code.rice.bowl.spaghetti.dto.request.SubmitRequest;
import code.rice.bowl.spaghetti.dto.response.ChallengeProblemResponse;
import code.rice.bowl.spaghetti.dto.response.ChallengeSubmitResponse;
import code.rice.bowl.spaghetti.dto.response.SubmitResponse;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ProblemService problemService;
    private final RedisService redisService;
    private final UserService userService;

    private static final int HP = 3;
    private static final String USER_KEY_PREFIX = "user-";

    public ChallengeProblemResponse startChallenge(String email) {

        // 0. 사용자 정보 조회.
        User user = userService.getUser(email);

        String userKey = USER_KEY_PREFIX + user.getEmail();

        // 1. 사용자 현재 정보를 redis 등록 여부 확인.
        // 등록 되어 있지 않으면 등록.
        if (!redisService.hasKey(userKey)) {
            redisService.setValue(userKey, String.format("%d-%d-%d", HP, 0, 0));

        }

        // 2. 사용자가 푼 문제 조회.
        ChallengeDto nowUser = ChallengeDto.fromString(redisService.getValue(userKey));

        // 3. 더 풀 문제가 있는지 조회.
        if (nowUser.getSolved().size() + 1 == problemService.totalProblem()) {
            // 모든 문제가 풀렸을 경우.
            return null;
        }

        // 4. 랜덤으로 문제를 조회하여 전달.
        long toSolve = -1;

        // 무한 루프 방지를 위하여 특정 횟수만큼 랜덤으로 생성 시도.
        int randCnt = 1000;
        for (int i = 0; i < randCnt; i++) {
            long tmp = ThreadLocalRandom.current().nextLong(1, problemService.totalProblem()) + 1;

            if (!nowUser.getSolved().contains(tmp)) {
                toSolve = tmp;
                break;
            }
        }

        // 랜덤 생성에 실패한 경우 -> 순차적으로 안 푼 문제 하나씩 전달.
        if (toSolve == -1) {
            for (long i = 1; i <= problemService.totalProblem(); i++) {
                if (!nowUser.getSolved().contains(i)) {
                    toSolve = i;
                    break;
                }
            }
        }

        // 5. 문제 및 상태 반환.
        return ChallengeProblemResponse.builder()
                .leftChance(nowUser.getHp())
                .score(nowUser.getScore())
                .problem(problemService.getProblemDetail(toSolve))
                .build();
    }

    public ChallengeSubmitResponse challengeGrading(String email, SubmitRequest request) {
        // 0. 사용자 정보 조회.
        User user = userService.getUser(email);
        String userKey = USER_KEY_PREFIX + user.getEmail();

        // 1. 사용자 존재 여부 확인.
        if (!redisService.hasKey(userKey)) {
            throw new InvalidRequestException("You are currently not in Challenge Mode.");
        }

        // 2. 문제 정보 조회
        Problem problem = problemService.getProblem(request.getProblemId());

        // 3. 현재 챌린지 상태 정보 조회 및 문제 풀이 기록..
        ChallengeDto nowUser = ChallengeDto.fromString(redisService.getValue(userKey));
        nowUser.getSolved().add(problem.getProblemId());

        // 4. 장답 체크.
        boolean isCorrect = normalize(request.getAnswer())
                .equals(normalize(problem.getAnswer()));

        if (isCorrect) {
            nowUser.setScore(nowUser.getScore() + problem.getPoint());
        } else {
            nowUser.setHp(nowUser.getHp() - 1);
        }

        // 5. 종료 여부 체크
        if (nowUser.getHp() <= 0) {
            // 레이팅과 뱃지 업데이트
            userService.updateRating(email, nowUser.getScore());

            // 이번 트라이 정보 삭제
            redisService.delete(userKey);

            // 결과 리턴.
            return ChallengeSubmitResponse.builder()
                    .correct(isCorrect)
                    .score(nowUser.getScore())
                    .gameOver(true)
                    .build();
        }

        // 6. 현재 상태 업데이트
        redisService.setValue(userKey, nowUser.toString());

        return ChallengeSubmitResponse.builder()
                .correct(isCorrect)
                .score(nowUser.getScore())
                .gameOver(false)
                .build();

    }

    private String normalize(String str) {
        return str == null ? "" : str.trim().toLowerCase();
    }
}
