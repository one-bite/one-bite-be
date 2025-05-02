package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder
public class Course {

    @Id
    private Long courseId;

    @OneToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;
}
