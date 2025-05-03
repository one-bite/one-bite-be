package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "topics")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    /**
     * 자동증분 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "topic_code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    private int total;
}
