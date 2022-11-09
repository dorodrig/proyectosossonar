package co.edu.sena.repository;

import co.edu.sena.domain.Achievement;
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
public class AchievementRepositoryWithBagRelationshipsImpl implements AchievementRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Achievement> fetchBagRelationships(Optional<Achievement> achievement) {
        return achievement.map(this::fetchCourses);
    }

    @Override
    public Page<Achievement> fetchBagRelationships(Page<Achievement> achievements) {
        return new PageImpl<>(
            fetchBagRelationships(achievements.getContent()),
            achievements.getPageable(),
            achievements.getTotalElements()
        );
    }

    @Override
    public List<Achievement> fetchBagRelationships(List<Achievement> achievements) {
        return Optional.of(achievements).map(this::fetchCourses).orElse(Collections.emptyList());
    }

    Achievement fetchCourses(Achievement result) {
        return entityManager
            .createQuery(
                "select achievement from Achievement achievement left join fetch achievement.courses where achievement is :achievement",
                Achievement.class
            )
            .setParameter("achievement", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Achievement> fetchCourses(List<Achievement> achievements) {
        return entityManager
            .createQuery(
                "select distinct achievement from Achievement achievement left join fetch achievement.courses where achievement in :achievements",
                Achievement.class
            )
            .setParameter("achievements", achievements)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
