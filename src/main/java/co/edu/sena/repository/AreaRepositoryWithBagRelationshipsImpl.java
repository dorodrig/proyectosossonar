package co.edu.sena.repository;

import co.edu.sena.domain.Area;
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
public class AreaRepositoryWithBagRelationshipsImpl implements AreaRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Area> fetchBagRelationships(Optional<Area> area) {
        return area.map(this::fetchLevels);
    }

    @Override
    public Page<Area> fetchBagRelationships(Page<Area> areas) {
        return new PageImpl<>(fetchBagRelationships(areas.getContent()), areas.getPageable(), areas.getTotalElements());
    }

    @Override
    public List<Area> fetchBagRelationships(List<Area> areas) {
        return Optional.of(areas).map(this::fetchLevels).orElse(Collections.emptyList());
    }

    Area fetchLevels(Area result) {
        return entityManager
            .createQuery("select area from Area area left join fetch area.levels where area is :area", Area.class)
            .setParameter("area", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Area> fetchLevels(List<Area> areas) {
        return entityManager
            .createQuery("select distinct area from Area area left join fetch area.levels where area in :areas", Area.class)
            .setParameter("areas", areas)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
