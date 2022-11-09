package co.edu.sena.service;

import co.edu.sena.service.dto.TeacherDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.sena.domain.Teacher}.
 */
public interface TeacherService {
    /**
     * Save a teacher.
     *
     * @param teacherDTO the entity to save.
     * @return the persisted entity.
     */
    TeacherDTO save(TeacherDTO teacherDTO);

    /**
     * Updates a teacher.
     *
     * @param teacherDTO the entity to update.
     * @return the persisted entity.
     */
    TeacherDTO update(TeacherDTO teacherDTO);

    /**
     * Partially updates a teacher.
     *
     * @param teacherDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TeacherDTO> partialUpdate(TeacherDTO teacherDTO);

    /**
     * Get all the teachers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TeacherDTO> findAll(Pageable pageable);

    /**
     * Get the "id" teacher.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TeacherDTO> findOne(Long id);

    /**
     * Delete the "id" teacher.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
