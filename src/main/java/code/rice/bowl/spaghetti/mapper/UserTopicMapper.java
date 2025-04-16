package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.usertopic.UserTopicResponse;
import code.rice.bowl.spaghetti.entity.UserTopic;

public class UserTopicMapper {

    public static UserTopicResponse toDto(UserTopic userTopic) {
        return UserTopicResponse.builder()
                .userId(userTopic.getUser().getUserId())
                .topicId(userTopic.getTopic().getTopicId())
                .build();
    }
}