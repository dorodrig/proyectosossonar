package co.edu.sena.domain;

import co.edu.sena.domain.enumeration.StateAchievement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Achievement.
 */
@Entity
@Table(name = "achievement")
public class Achievement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 500)
    @Column(name = "achievement_description", length = 500, nullable = false)
    private String achievementDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_achievement")
    private StateAchievement statusAchievement;

    @ManyToMany
    @JoinTable(
        name = "rel_achievement__course",
        joinColumns = @JoinColumn(name = "achievement_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonIgnoreProperties(value = { "areas", "achievements", "notes" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Achievement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAchievementDescription() {
        return this.achievementDescription;
    }

    public Achievement achievementDescription(String achievementDescription) {
        this.setAchievementDescription(achievementDescription);
        return this;
    }

    public void setAchievementDescription(String achievementDescription) {
        this.achievementDescription = achievementDescription;
    }

    public StateAchievement getStatusAchievement() {
        return this.statusAchievement;
    }

    public Achievement statusAchievement(StateAchievement statusAchievement) {
        this.setStatusAchievement(statusAchievement);
        return this;
    }

    public void setStatusAchievement(StateAchievement statusAchievement) {
        this.statusAchievement = statusAchievement;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Achievement courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Achievement addCourse(Course course) {
        this.courses.add(course);
        course.getAchievements().add(this);
        return this;
    }

    public Achievement removeCourse(Course course) {
        this.courses.remove(course);
        course.getAchievements().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Achievement)) {
            return false;
        }
        return id != null && id.equals(((Achievement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Achievement{" +
            "id=" + getId() +
            ", achievementDescription='" + getAchievementDescription() + "'" +
            ", statusAchievement='" + getStatusAchievement() + "'" +
            "}";
    }
}
