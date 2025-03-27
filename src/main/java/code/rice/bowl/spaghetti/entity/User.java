package code.rice.bowl.spaghetti.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Getter
@Entity
@EntityScan
@Builder
@AllArgsConstructor
@Table(name = "user_info")
public class User {
    @Id
    private String email;
}
