package code.rice.bowl.spaghetti.repository;

import code.rice.bowl.spaghetti.dto.rank.RankSimpleResponse;
import code.rice.bowl.spaghetti.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {

    // 주어진 구간과 겹치는 범위가 있는지 확인.
    boolean existsByMinRatingLessThanEqualAndMaxRatingGreaterThanEqual(int max, int min);

    Optional<Rank> findByMinRatingLessThanEqualAndMaxRatingGreaterThan(int max, int min);

    // 이름 존재 여부 확인.
    boolean existsByName(String name);

    Optional<Rank> findByName(String name);

    @Query("SELECT new code.rice.bowl.spaghetti.dto.rank.RankSimpleResponse(l.rankId, l.name) FROM Rank l")
    List<RankSimpleResponse> findSimpleAll();
}
