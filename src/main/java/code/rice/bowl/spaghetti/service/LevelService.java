package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.level.LevelDto;
import code.rice.bowl.spaghetti.dto.level.LevelResponse;
import code.rice.bowl.spaghetti.dto.level.LevelSimpleResponse;
import code.rice.bowl.spaghetti.entity.Level;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.exception.NotFoundException;
import code.rice.bowl.spaghetti.mapper.LevelMapper;
import code.rice.bowl.spaghetti.repository.LevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelService {
    private final LevelRepository levelRepository;

    /**
     * level db에 저장.
     *
     * @param levelDto 저장할 객체
     */
    public void create(LevelDto levelDto) {
        // 1. 범위 겹침 확인.
        boolean dupRange = levelRepository.existsByMinRatingLessThanEqualAndMaxRatingGreaterThanEqual(
                levelDto.getMaxRating(),
                levelDto.getMinRating());

        if (dupRange) {
            throw new InvalidRequestException("level : rating 범위 겹침.");
        }

        // 2. 이름 중복 확인.
        if (levelRepository.existsByName(levelDto.getName())) {
            throw new InvalidRequestException("level : 이름 중복.");
        }

        // 3. db에 데이터 저장.
        try {
            levelRepository.save(LevelMapper.dtoToEntity(levelDto));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRequestException("level : 이릉이 중복 됨.");
        } catch (Exception e) {
            throw new InvalidRequestException("level : check your request.");
        }
    }

    /**
     * level 데이터 수정.
     *
     * @param id        수정할 level id.
     * @param levelDto     수정 정보.
     */
    public void update(Long id, LevelDto levelDto) {
        Level oldLevel = selectById(id);

        oldLevel.setName(levelDto.getName());
        oldLevel.setMinRating(levelDto.getMinRating());
        oldLevel.setMaxRating(levelDto.getMaxRating());

        levelRepository.save(oldLevel);
    }

    /**
     * level 데이터 삭제
     *
     * @param id    삭제할 level id
     */
    @Transactional
    public void delete(Long id) {
        levelRepository.deleteById(id);
    }

    /**
     * 모든 level 객체 조회
     *
     * @return Level 객체에서 name, id 만 추출하여 전달.
     */
    public List<LevelSimpleResponse> selectAllSimple() {
        return levelRepository.findSimpleAll();
    }

    /**
     * LevelResponse 객체 조회
     *
     * @param id    조회할 level id
     * @return      조회한 level response
     */
    public LevelResponse select(Long id) {
        return LevelMapper.dtoToLevelResponse(selectById(id));
    }

    /**
     * Level 객체 조회
     *
     * @param id    조회할 level id
     * @return      조회한 Level 객체
     * @throws InvalidRequestException 유효하지 않는 level id 인 경우
     */
    private Level selectById(Long id) {
        return levelRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("level: " + id + " is not exists."));
    }

    /**
     * 사용자의 rating 기준에 맞는 level 조회.
     *
     * @param rating    현재 사용자 rating
     * @return          현재 사용자 level.
     */
    public Level getUserLevel(int rating) {
        return levelRepository.findByMinRatingLessThanEqualAndMaxRatingGreaterThan(0, 0)
                .orElseThrow(() -> new NotFoundException("Level : not found"));
    }
}
