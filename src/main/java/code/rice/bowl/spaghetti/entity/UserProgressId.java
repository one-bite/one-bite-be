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

    private Long userKey;
    private Long topicKey;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserProgressId other = (UserProgressId) obj;

        return Objects.equals(userKey, other.userKey)
                && Objects.equals(topicKey, other.topicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userKey, topicKey);
    }
}
