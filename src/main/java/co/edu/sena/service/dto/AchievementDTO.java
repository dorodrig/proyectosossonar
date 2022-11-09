package co.edu.sena.service.dto;

import co.edu.sena.domain.enumeration.StateAchievement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.edu.sena.domain.Achievement} entity.
 */
public class AchievementDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String achievementDescription;

    private StateAchievement statusAchievement;

    private Set<CourseDTO> courses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAchievementDescription() {
        return achievementDescription;
    }

    public void setAchievementDescription(String achievementDescription) {
        this.achievementDescription = achievementDescription;
    }

    public StateAchievement getStatusAchievement() {
        return statusAchievement;
    }

    public void setStatusAchievement(StateAchievement statusAchievement) {
        this.statusAchievement = statusAchievement;
    }

    public Set<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseDTO> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AchievementDTO)) {
            return false;
        }

        AchievementDTO achievementDTO = (AchievementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, achievementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AchievementDTO{" +
            "id=" + getId() +
            ", achievementDescription='" + getAchievementDescription() + "'" +
            ", statusAchievement='" + getStatusAchievement() + "'" +
            ", courses=" + getCourses() +
            "}";
    }
}
