package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.request.UserPatchRequest;
import code.rice.bowl.spaghetti.dto.response.BadgeResponse;
import code.rice.bowl.spaghetti.dto.response.CurrentUserResponse;
import code.rice.bowl.spaghetti.entity.Badge;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.entity.UserBadge;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.mapper.BadgeMapper;
import code.rice.bowl.spaghetti.mapper.UserMapper;
import code.rice.bowl.spaghetti.repository.UserBadgeRepository;
import code.rice.bowl.spaghetti.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 로그인한 사용자의 자세한 정보 조회
     *
     * @param email     조회할 계정 정보
     * @return          조회한 계정 정보.
     */
    public CurrentUserResponse getUserAllInfo(String email) {
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
                .orElseThrow(() -> new InvalidRequestException("user : check your login info"));
    }
}
