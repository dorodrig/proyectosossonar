package co.edu.sena.service.impl;

import co.edu.sena.domain.Achievement;
import co.edu.sena.repository.AchievementRepository;
import co.edu.sena.service.AchievementService;
import co.edu.sena.service.dto.AchievementDTO;
import co.edu.sena.service.mapper.AchievementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Achievement}.
 */
@Service
@Transactional
public class AchievementServiceImpl implements AchievementService {

    private final Logger log = LoggerFactory.getLogger(AchievementServiceImpl.class);

    private final AchievementRepository achievementRepository;

    private final AchievementMapper achievementMapper;

    public AchievementServiceImpl(AchievementRepository achievementRepository, AchievementMapper achievementMapper) {
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
    }

    @Override
    public AchievementDTO save(AchievementDTO achievementDTO) {
        log.debug("Request to save Achievement : {}", achievementDTO);
        Achievement achievement = achievementMapper.toEntity(achievementDTO);
        achievement = achievementRepository.save(achievement);
        return achievementMapper.toDto(achievement);
    }

    @Override
    public AchievementDTO update(AchievementDTO achievementDTO) {
        log.debug("Request to save Achievement : {}", achievementDTO);
        Achievement achievement = achievementMapper.toEntity(achievementDTO);
        achievement = achievementRepository.save(achievement);
        return achievementMapper.toDto(achievement);
    }

    @Override
    public Optional<AchievementDTO> partialUpdate(AchievementDTO achievementDTO) {
        log.debug("Request to partially update Achievement : {}", achievementDTO);

        return achievementRepository
            .findById(achievementDTO.getId())
            .map(existingAchievement -> {
                achievementMapper.partialUpdate(existingAchievement, achievementDTO);

                return existingAchievement;
            })
            .map(achievementRepository::save)
            .map(achievementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AchievementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Achievements");
        return achievementRepository.findAll(pageable).map(achievementMapper::toDto);
    }

    public Page<AchievementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return achievementRepository.findAllWithEagerRelationships(pageable).map(achievementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AchievementDTO> findOne(Long id) {
        log.debug("Request to get Achievement : {}", id);
        return achievementRepository.findOneWithEagerRelationships(id).map(achievementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Achievement : {}", id);
        achievementRepository.deleteById(id);
    }
}
