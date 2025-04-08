package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "user_interests")
@IdClass(UserInterestId.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInterest {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}
