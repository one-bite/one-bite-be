package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserProgressId {

    private Long userId;
    private Long topicId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserProgressId other = (UserProgressId) obj;

        return Objects.equals(userId, other.userId)
                && Objects.equals(topicId, other.topicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, topicId);
    }
}
