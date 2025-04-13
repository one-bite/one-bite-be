package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Entity
@Table(name = "badges")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeId;

    @Column(nullable = false)
    private String name;

    private String description;

    // 뱃지 획득 조건
    private String condition;

    private String imageUrl;
}
