package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.badge.BadgeResponse;
import code.rice.bowl.spaghetti.dto.user.UserSimpleResponse;
import code.rice.bowl.spaghetti.entity.Badge;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.entity.UserBadge;
import code.rice.bowl.spaghetti.mapper.BadgeMapper;
import code.rice.bowl.spaghetti.mapper.UserMapper;
import code.rice.bowl.spaghetti.repository.UserBadgeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserBadgeService {

    private final UserService userService;
    private final BadgeService badgeService;
    private final UserBadgeRepository userBadgeRepository;

    /**
     * 사용자 뱃지 횟득 처리 함수. (TODO: 파라미터 아직 확정 X)
     */
    public void ownBadges(User user, Badge badge) {
        userBadgeRepository.save(UserBadge.builder()
                        .user(user)
                        .badge(badge)
                        .build());
    }

    /**
     * 뱃지를 보유한 사람정보 조회
     * @param id    badge id
     */
    public List<UserSimpleResponse> getBadgesOwner(Long id) {
        // 1. 뱃지 조회.
        Badge badge = badgeService.getBadge(id);

        // 2. 뱃지 소유자 조회.
        List<UserBadge> users = badge.getUserBadges();

        // 3. 사용자 정보만 필터링하여 조회.
        List<UserSimpleResponse> res = new ArrayList<>();

        for (UserBadge user: users) {
            res.add(UserMapper.toUserSimple(user.getUser()));
        }

        return res;
    }

    /**
     * 사용자가 보유하고 있는 모든 뱃지 조회
     */
    public List<BadgeResponse> getUserBadges(String email) {
        // 1. 사용자 조회.
        User user = userService.getUser(email);

        // 2. 사용자 뱃지 조회.
        List<UserBadge> badges = user.getUserBadges();

        // 3. 뱃지 정보만 필터링하여 처리.
        List<BadgeResponse> res = new ArrayList<>();

        for (UserBadge badge: badges) {
            res.add(BadgeMapper.toDto(badge.getBadge()));
        }

        return res;
    }

}
