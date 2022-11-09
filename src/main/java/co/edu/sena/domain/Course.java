package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "name_course", length = 30, nullable = false)
    private String nameCourse;

    @ManyToMany
    @JoinTable(name = "rel_course__area", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "area_id"))
    @JsonIgnoreProperties(value = { "levels", "courses" }, allowSetters = true)
    private Set<Area> areas = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private Set<Achievement> achievements = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameCourse() {
        return this.nameCourse;
    }

    public Course nameCourse(String nameCourse) {
        this.setNameCourse(nameCourse);
        return this;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

    public Set<Area> getAreas() {
        return this.areas;
    }

    public void setAreas(Set<Area> areas) {
        this.areas = areas;
    }

    public Course areas(Set<Area> areas) {
        this.setAreas(areas);
        return this;
    }

    public Course addArea(Area area) {
        this.areas.add(area);
        area.getCourses().add(this);
        return this;
    }

    public Course removeArea(Area area) {
        this.areas.remove(area);
        area.getCourses().remove(this);
        return this;
    }

    public Set<Achievement> getAchievements() {
        return this.achievements;
    }

    public void setAchievements(Set<Achievement> achievements) {
        if (this.achievements != null) {
            this.achievements.forEach(i -> i.removeCourse(this));
        }
        if (achievements != null) {
            achievements.forEach(i -> i.addCourse(this));
        }
        this.achievements = achievements;
    }

    public Course achievements(Set<Achievement> achievements) {
        this.setAchievements(achievements);
        return this;
    }

    public Course addAchievement(Achievement achievement) {
        this.achievements.add(achievement);
        achievement.getCourses().add(this);
        return this;
    }

    public Course removeAchievement(Achievement achievement) {
        this.achievements.remove(achievement);
        achievement.getCourses().remove(this);
        return this;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.removeCourse(this));
        }
        if (notes != null) {
            notes.forEach(i -> i.addCourse(this));
        }
        this.notes = notes;
    }

    public Course notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public Course addNote(Note note) {
        this.notes.add(note);
        note.getCourses().add(this);
        return this;
    }

    public Course removeNote(Note note) {
        this.notes.remove(note);
        note.getCourses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", nameCourse='" + getNameCourse() + "'" +
            "}";
    }
}
