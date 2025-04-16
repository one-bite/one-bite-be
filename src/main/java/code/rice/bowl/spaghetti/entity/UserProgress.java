package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "user_progress")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProgress {

    @EmbeddedId
    @Builder.Default
    private UserProgressId progressId = new UserProgressId();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
        this.progressId.setUserKey(user.getUserId());
    }

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public void setTopic(Topic topic) {
        this.topic = topic;
        this.progressId.setTopicKey(topic.getTopicId());
    }

    @Setter
    private int completedProblems;
}
