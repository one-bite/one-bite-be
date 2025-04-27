package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.topic.TopicRequest;
import code.rice.bowl.spaghetti.dto.topic.TopicResponse;
import code.rice.bowl.spaghetti.entity.Topic;

public class TopicMapper {

    public static Topic toEntity(TopicRequest dto) {
        return Topic.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .total(dto.getTotal())
                .build();
    }

    public static TopicResponse toDto(Topic topic) {
        return TopicResponse.builder()
                .topicId(topic.getTopicId())
                .name(topic.getName())
                .description(topic.getDescription())
                .total(topic.getTotal())
                .build();
    }
}