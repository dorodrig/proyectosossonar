package co.edu.sena.repository;

import co.edu.sena.domain.Level;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class LevelRepositoryWithBagRelationshipsImpl implements LevelRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Level> fetchBagRelationships(Optional<Level> level) {
        return level.map(this::fetchEnrollments);
    }

    @Override
    public Page<Level> fetchBagRelationships(Page<Level> levels) {
        return new PageImpl<>(fetchBagRelationships(levels.getContent()), levels.getPageable(), levels.getTotalElements());
    }

    @Override
    public List<Level> fetchBagRelationships(List<Level> levels) {
        return Optional.of(levels).map(this::fetchEnrollments).orElse(Collections.emptyList());
    }

    Level fetchEnrollments(Level result) {
        return entityManager
            .createQuery("select level from Level level left join fetch level.enrollments where level is :level", Level.class)
            .setParameter("level", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Level> fetchEnrollments(List<Level> levels) {
        return entityManager
            .createQuery("select distinct level from Level level left join fetch level.enrollments where level in :levels", Level.class)
            .setParameter("levels", levels)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
