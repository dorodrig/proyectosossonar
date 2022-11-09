package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Area.
 */
@Entity
@Table(name = "area")
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "area_name", length = 30, nullable = false)
    private String areaName;

    @ManyToMany
    @JoinTable(name = "rel_area__level", joinColumns = @JoinColumn(name = "area_id"), inverseJoinColumns = @JoinColumn(name = "level_id"))
    @JsonIgnoreProperties(value = { "enrollments", "areas" }, allowSetters = true)
    private Set<Level> levels = new HashSet<>();

    @ManyToMany(mappedBy = "areas")
    @JsonIgnoreProperties(value = { "areas", "achievements", "notes" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Area id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public Area areaName(String areaName) {
        this.setAreaName(areaName);
        return this;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Set<Level> getLevels() {
        return this.levels;
    }

    public void setLevels(Set<Level> levels) {
        this.levels = levels;
    }

    public Area levels(Set<Level> levels) {
        this.setLevels(levels);
        return this;
    }

    public Area addLevel(Level level) {
        this.levels.add(level);
        level.getAreas().add(this);
        return this;
    }

    public Area removeLevel(Level level) {
        this.levels.remove(level);
        level.getAreas().remove(this);
        return this;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        if (this.courses != null) {
            this.courses.forEach(i -> i.removeArea(this));
        }
        if (courses != null) {
            courses.forEach(i -> i.addArea(this));
        }
        this.courses = courses;
    }

    public Area courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Area addCourse(Course course) {
        this.courses.add(course);
        course.getAreas().add(this);
        return this;
    }

    public Area removeCourse(Course course) {
        this.courses.remove(course);
        course.getAreas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Area)) {
            return false;
        }
        return id != null && id.equals(((Area) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Area{" +
            "id=" + getId() +
            ", areaName='" + getAreaName() + "'" +
            "}";
    }
}
