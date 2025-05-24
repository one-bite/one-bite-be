package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.rank.RankRequest;
import code.rice.bowl.spaghetti.dto.rank.RankResponse;
import code.rice.bowl.spaghetti.dto.rank.RankSimpleResponse;
import code.rice.bowl.spaghetti.entity.Rank;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.exception.NotFoundException;
import code.rice.bowl.spaghetti.mapper.RankMapper;
import code.rice.bowl.spaghetti.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;

    /**
     * Rank db에 저장.
     *
     * @param rankRequest 저장할 객체
     */
    public void create(RankRequest rankRequest) {
        // 1. 범위 겹침 확인.
        boolean dupRange = rankRepository.existsByMinRatingLessThanEqualAndMaxRatingGreaterThanEqual(
                rankRequest.getMaxRating(),
                rankRequest.getMinRating());

        if (dupRange) {
            throw new InvalidRequestException("Rank : rating 범위 겹침.");
        }

        // 2. 이름 중복 확인.
        if (rankRepository.existsByName(rankRequest.getName())) {
            throw new InvalidRequestException("Rank : 이름 중복.");
        }

        // 3. db에 데이터 저장.
        try {
            rankRepository.save(RankMapper.dtoToEntity(rankRequest));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRequestException("Rank : 이릉이 중복 됨.");
        } catch (Exception e) {
            throw new InvalidRequestException("Rank : check your request.");
        }
    }

    /**
     * Rank 데이터 수정.
     *
     * @param id        수정할 Rank id.
     * @param rankRequest     수정 정보.
     */
    public void update(Long id, RankRequest rankRequest) {
        Rank oldRank = selectById(id);

        oldRank.setName(rankRequest.getName());
        oldRank.setMinRating(rankRequest.getMinRating());
        oldRank.setMaxRating(rankRequest.getMaxRating());

        rankRepository.save(oldRank);
    }

    /**
     * Rank 데이터 삭제
     *
     * @param id    삭제할 Rank id
     */
    @Transactional
    public void delete(Long id) {
        rankRepository.deleteById(id);
    }

    /**
     * 모든 Rank 객체 조회
     *
     * @return Rank 객체에서 name, id 만 추출하여 전달.
     */
    public List<RankSimpleResponse> selectAllSimple() {
        return rankRepository.findSimpleAll();
    }

    /**
     * RankResponse 객체 조회
     *
     * @param id    조회할 Rank id
     * @return      조회한 Rank response
     */
    public RankResponse selectRankResponse(Long id) {
        return RankMapper.dtoToRankResponse(selectById(id));
    }

    /**
     * Rank 객체 조회
     *
     * @param id    조회할 Rank id
     * @return      조회한 Rank 객체
     * @throws InvalidRequestException 유효하지 않는 Rank id 인 경우
     */
    private Rank selectById(Long id) {
        return rankRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Rank: " + id + " is not exists."));
    }

    /**
     * 사용자의 rating 기준에 맞는 Rank 조회.
     *
     * @param rating    현재 사용자 rating
     * @return          현재 사용자 Rank.
     */
    public Rank getUserRank(int rating) {
        return rankRepository.findByMinRatingLessThanEqualAndMaxRatingGreaterThanEqual(rating, rating)
                .orElseThrow(() -> new NotFoundException("Rank : not found"));
    }
}
