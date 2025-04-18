package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.user.CurrentUserResponse;
import code.rice.bowl.spaghetti.dto.user.UserSimpleResponse;
import code.rice.bowl.spaghetti.entity.User;

public class UserMapper {

    public static CurrentUserResponse toCurrentUser(User user) {
        return CurrentUserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .rating(user.getRating())
                .points(user.getPoints())
                .level(user.getLevel().getName())
                .build();
    }

    public static UserSimpleResponse toUserSimple(User user) {
        return UserSimpleResponse.builder()
                .name(user.getUsername())
                .level(user.getLevel().getName())
                .build();
    }
}
