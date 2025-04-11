package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.BadgeDto;
import code.rice.bowl.spaghetti.dto.response.BadgeResponse;
import code.rice.bowl.spaghetti.entity.Badge;
import code.rice.bowl.spaghetti.mapper.BadgeMapper;
import code.rice.bowl.spaghetti.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    public BadgeResponse create(BadgeDto dto) {
        Badge badge = BadgeMapper.toEntity(dto);
        return BadgeMapper.toDto(badgeRepository.save(badge));
    }

    public List<BadgeResponse> findAll() {
        return badgeRepository.findAll().stream()
                .map(BadgeMapper::toDto)
                .collect(Collectors.toList());
    }

    public BadgeResponse findById(Long id) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge not found"));
        return BadgeMapper.toDto(badge);
    }

    public BadgeResponse update(Long id, BadgeDto dto) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge not found"));
        BadgeMapper.updateEntity(badge, dto);
        return BadgeMapper.toDto(badgeRepository.save(badge));
    }

    public void delete(Long id) {
        badgeRepository.deleteById(id);
    }
}