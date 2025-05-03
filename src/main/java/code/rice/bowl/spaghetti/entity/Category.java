package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @Column(length = 500)
    private String description;

    /**
     * 이 카테고리에 속한 문제의 총 개수
     */
    @Column(nullable = false)
    private int total;

    /**
     * 카테고리와 문제 간의 일대다 관계
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Problem> problems = new ArrayList<>();
}