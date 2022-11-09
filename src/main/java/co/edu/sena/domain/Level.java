package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Level.
 */
@Entity
@Table(name = "level")
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "name_curse", length = 10, nullable = false)
    private String nameCurse;

    @NotNull
    @Size(max = 10)
    @Column(name = "course_acronym", length = 10, nullable = false)
    private String courseAcronym;

    @ManyToMany
    @JoinTable(
        name = "rel_level__enrollment",
        joinColumns = @JoinColumn(name = "level_id"),
        inverseJoinColumns = @JoinColumn(name = "enrollment_id")
    )
    @JsonIgnoreProperties(value = { "student", "levels" }, allowSetters = true)
    private Set<Enrollment> enrollments = new HashSet<>();

    @ManyToMany(mappedBy = "levels")
    @JsonIgnoreProperties(value = { "levels", "courses" }, allowSetters = true)
    private Set<Area> areas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Level id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameCurse() {
        return this.nameCurse;
    }

    public Level nameCurse(String nameCurse) {
        this.setNameCurse(nameCurse);
        return this;
    }

    public void setNameCurse(String nameCurse) {
        this.nameCurse = nameCurse;
    }

    public String getCourseAcronym() {
        return this.courseAcronym;
    }

    public Level courseAcronym(String courseAcronym) {
        this.setCourseAcronym(courseAcronym);
        return this;
    }

    public void setCourseAcronym(String courseAcronym) {
        this.courseAcronym = courseAcronym;
    }

    public Set<Enrollment> getEnrollments() {
        return this.enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public Level enrollments(Set<Enrollment> enrollments) {
        this.setEnrollments(enrollments);
        return this;
    }

    public Level addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.getLevels().add(this);
        return this;
    }

    public Level removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
        enrollment.getLevels().remove(this);
        return this;
    }

    public Set<Area> getAreas() {
        return this.areas;
    }

    public void setAreas(Set<Area> areas) {
        if (this.areas != null) {
            this.areas.forEach(i -> i.removeLevel(this));
        }
        if (areas != null) {
            areas.forEach(i -> i.addLevel(this));
        }
        this.areas = areas;
    }

    public Level areas(Set<Area> areas) {
        this.setAreas(areas);
        return this;
    }

    public Level addArea(Area area) {
        this.areas.add(area);
        area.getLevels().add(this);
        return this;
    }

    public Level removeArea(Area area) {
        this.areas.remove(area);
        area.getLevels().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Level)) {
            return false;
        }
        return id != null && id.equals(((Level) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Level{" +
            "id=" + getId() +
            ", nameCurse='" + getNameCurse() + "'" +
            ", courseAcronym='" + getCourseAcronym() + "'" +
            "}";
    }
}
