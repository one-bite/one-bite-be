package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.badge.BadgeRequest;
import code.rice.bowl.spaghetti.dto.badge.BadgeResponse;
import code.rice.bowl.spaghetti.entity.Badge;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
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

    public BadgeResponse create(BadgeRequest dto) {
        Badge badge = BadgeMapper.toEntity(dto);
        return BadgeMapper.toDto(badgeRepository.save(badge));
    }

    public List<BadgeResponse> findAll() {
        return badgeRepository.findAll().stream()
                .map(BadgeMapper::toDto)
                .collect(Collectors.toList());
    }

    public BadgeResponse findById(Long id) {
        return BadgeMapper.toDto(getBadge(id));
    }

    public BadgeResponse update(Long id, BadgeRequest dto) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Badge not found"));

        badge.setName(dto.getName());
        badge.setDescription(dto.getDescription());
        badge.setCondition(dto.getCondition());
        badge.setImageUrl(dto.getImageUrl());

        return BadgeMapper.toDto(badgeRepository.save(badge));
    }

    public void delete(Long id) {
        badgeRepository.deleteById(id);
    }

    public Badge getBadge(Long id) {
        return badgeRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Badge not found"));
    }
}