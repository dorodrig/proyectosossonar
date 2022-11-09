package co.edu.sena.service;

import co.edu.sena.service.dto.AreaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.sena.domain.Area}.
 */
public interface AreaService {
    /**
     * Save a area.
     *
     * @param areaDTO the entity to save.
     * @return the persisted entity.
     */
    AreaDTO save(AreaDTO areaDTO);

    /**
     * Updates a area.
     *
     * @param areaDTO the entity to update.
     * @return the persisted entity.
     */
    AreaDTO update(AreaDTO areaDTO);

    /**
     * Partially updates a area.
     *
     * @param areaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AreaDTO> partialUpdate(AreaDTO areaDTO);

    /**
     * Get all the areas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AreaDTO> findAll(Pageable pageable);

    /**
     * Get all the areas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AreaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" area.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AreaDTO> findOne(Long id);

    /**
     * Delete the "id" area.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
