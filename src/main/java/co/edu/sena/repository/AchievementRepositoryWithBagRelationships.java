package co.edu.sena.repository;

import co.edu.sena.domain.Achievement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AchievementRepositoryWithBagRelationships {
    Optional<Achievement> fetchBagRelationships(Optional<Achievement> achievement);

    List<Achievement> fetchBagRelationships(List<Achievement> achievements);

    Page<Achievement> fetchBagRelationships(Page<Achievement> achievements);
}
