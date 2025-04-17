package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.user.UserCurrentResponse;
import code.rice.bowl.spaghetti.dto.user.UserSimpleResponse;
import code.rice.bowl.spaghetti.entity.User;

public class UserMapper {

    public static UserCurrentResponse toCurrentUser(User user) {
        return UserCurrentResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .rating(user.getRating())
                .points(user.getPoints())
                .level(user.getRank().getName())
                .build();
    }

    public static UserSimpleResponse toUserSimple(User user) {
        return UserSimpleResponse.builder()
                .name(user.getUsername())
                .level(user.getRank().getName())
                .build();
    }
}
