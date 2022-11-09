package co.edu.sena.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.edu.sena.domain.Level} entity.
 */
public class LevelDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String nameCurse;

    @NotNull
    @Size(max = 10)
    private String courseAcronym;

    private Set<EnrollmentDTO> enrollments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameCurse() {
        return nameCurse;
    }

    public void setNameCurse(String nameCurse) {
        this.nameCurse = nameCurse;
    }

    public String getCourseAcronym() {
        return courseAcronym;
    }

    public void setCourseAcronym(String courseAcronym) {
        this.courseAcronym = courseAcronym;
    }

    public Set<EnrollmentDTO> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<EnrollmentDTO> enrollments) {
        this.enrollments = enrollments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LevelDTO)) {
            return false;
        }

        LevelDTO levelDTO = (LevelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, levelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LevelDTO{" +
            "id=" + getId() +
            ", nameCurse='" + getNameCurse() + "'" +
            ", courseAcronym='" + getCourseAcronym() + "'" +
            ", enrollments=" + getEnrollments() +
            "}";
    }
}
