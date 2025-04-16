package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.dto.level.LevelSimpleResponse;
import code.rice.bowl.spaghetti.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    // 주어진 구간과 겹치는 범위가 있는지 확인.
    boolean existsByMinRatingLessThanEqualAndMaxRatingGreaterThanEqual(int max, int min);

    Optional<Level> findByMinRatingLessThanEqualAndMaxRatingGreaterThan(int max, int min);

    // 이름 존재 여부 확인.
    boolean existsByName(String name);

    Optional<Level> findByName(String name);

    @Query("SELECT new code.rice.bowl.spaghetti.dto.level.LevelSimpleResponse(l.levelId, l.name) FROM Level l")
    List<LevelSimpleResponse> findSimpleAll();
}
