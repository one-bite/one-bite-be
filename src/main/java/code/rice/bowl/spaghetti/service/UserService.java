package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.user.UserPatchRequest;
import code.rice.bowl.spaghetti.dto.user.UserCurrentResponse;
import code.rice.bowl.spaghetti.dto.user.UserRankResponse;
import code.rice.bowl.spaghetti.entity.Streak;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.exception.NotFoundException;
import code.rice.bowl.spaghetti.mapper.UserMapper;
import code.rice.bowl.spaghetti.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RankService rankService;

    /**
     * 로그인한 사용자의 자세한 정보 조회
     *
     * @param email     조회할 계정 정보
     * @return          조회한 계정 정보.
     */
    public UserCurrentResponse getUserAllInfo(String email) {
        User current = getUser(email);

        return UserMapper.toCurrentUser(current);
    }

    /**
     * 사용자 정보 일부 업데이트
     *
     * @param email     업데이트할 사용자 이메일 정보.
     * @param request   업데이트할 사용자 정보들.
     */
    public void updateNickname(String email, UserPatchRequest request) {
        User current = getUser(email);

        // 0. 변경하려고 하는 이름에 대한 필터링 처리.
        if (request.getNickname().matches(".*[^a-zA-Z0-9가-힣].*")) {
            throw new InvalidRequestException("User: Special characters are not allowed in the name.");
        }

        // 1. 이름 중복 여부 확인.
        if (userRepository.existsByUsername(request.getNickname())) {
            throw new InvalidRequestException("User: nickname duplication");
        }

        // 2. 변경 사항이는 지 여부
        if (current.getUsername().equals(request.getNickname())) {
            return;
        }
        current.setUsername(request.getNickname());
        userRepository.save(current);
    }

    /**
     * 사용자 이메일로 사용자 정보 조회하기
     * 새로운 회원인 경우 DB에 추가 후 리턴.
     *
     * @param email 사용자 이메일
     * @return      User
     */
    public User loadOrCreate(String email) {
        try {
            // 기존 회원 조회
            // 회원 정보 X -> InvalidRequestException 발생.
            return getUser(email);
        } catch (Exception e) {
            // 새로운 회원 생성.
            User newUser = User.builder()
                    .email(email)
                    .username(email.split("@")[0])
                    .points(0)
                    .rating(0)
                    .rank(rankService.getUserRank(0))
                    .isNew(true)
                    .build();

            newUser.setStreak(new Streak());

            userRepository.save(newUser);

            return newUser;
        }
    }

    /**
     * 사용자의 랭크 정보 조회
     */
    public UserRankResponse getUserRank(String email) {
        User now = getUser(email);

        return UserRankResponse.builder()
                .name(now.getRank().getName())
                .point(now.getRating())
                .build();
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void delete(String email) {
        userRepository.deleteByEmail(email);
    }

    /**
     * 사용자 객체 조회
     *
     * @param email     조회할 사용자 객체.
     * @return          조회된 상요자 객체 정보.
     */
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user : user not found"));
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user : user not found"));
    }
}
