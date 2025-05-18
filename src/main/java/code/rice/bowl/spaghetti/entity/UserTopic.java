package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_interests")
@IdClass(UserTopicId.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTopic {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}
