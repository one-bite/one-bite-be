package code.rice.bowl.spaghetti.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTopicId implements Serializable {
    private Long user;
    private Long topic;
}
