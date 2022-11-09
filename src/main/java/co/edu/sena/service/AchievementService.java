package co.edu.sena.service;

import co.edu.sena.service.dto.AchievementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.sena.domain.Achievement}.
 */
public interface AchievementService {
    /**
     * Save a achievement.
     *
     * @param achievementDTO the entity to save.
     * @return the persisted entity.
     */
    AchievementDTO save(AchievementDTO achievementDTO);

    /**
     * Updates a achievement.
     *
     * @param achievementDTO the entity to update.
     * @return the persisted entity.
     */
    AchievementDTO update(AchievementDTO achievementDTO);

    /**
     * Partially updates a achievement.
     *
     * @param achievementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AchievementDTO> partialUpdate(AchievementDTO achievementDTO);

    /**
     * Get all the achievements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AchievementDTO> findAll(Pageable pageable);

    /**
     * Get all the achievements with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AchievementDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" achievement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AchievementDTO> findOne(Long id);

    /**
     * Delete the "id" achievement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
