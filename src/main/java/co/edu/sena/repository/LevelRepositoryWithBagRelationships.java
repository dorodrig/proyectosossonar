package co.edu.sena.repository;

import co.edu.sena.domain.Level;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface LevelRepositoryWithBagRelationships {
    Optional<Level> fetchBagRelationships(Optional<Level> level);

    List<Level> fetchBagRelationships(List<Level> levels);

    Page<Level> fetchBagRelationships(Page<Level> levels);
}
