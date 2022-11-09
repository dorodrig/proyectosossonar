package co.edu.sena.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.edu.sena.domain.Area} entity.
 */
public class AreaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String areaName;

    private Set<LevelDTO> levels = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Set<LevelDTO> getLevels() {
        return levels;
    }

    public void setLevels(Set<LevelDTO> levels) {
        this.levels = levels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaDTO)) {
            return false;
        }

        AreaDTO areaDTO = (AreaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, areaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AreaDTO{" +
            "id=" + getId() +
            ", areaName='" + getAreaName() + "'" +
            ", levels=" + getLevels() +
            "}";
    }
}
