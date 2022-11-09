package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "qualification", length = 10, nullable = false)
    private String qualification;

    @NotNull
    @Size(max = 10)
    @Column(name = "period", length = 10, nullable = false)
    private String period;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @ManyToMany
    @JoinTable(name = "rel_note__course", joinColumns = @JoinColumn(name = "note_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnoreProperties(value = { "areas", "achievements", "notes" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Note id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQualification() {
        return this.qualification;
    }

    public Note qualification(String qualification) {
        this.setQualification(qualification);
        return this;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getPeriod() {
        return this.period;
    }

    public Note period(String period) {
        this.setPeriod(period);
        return this;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public Note startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Note courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Note addCourse(Course course) {
        this.courses.add(course);
        course.getNotes().add(this);
        return this;
    }

    public Note removeCourse(Course course) {
        this.courses.remove(course);
        course.getNotes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return id != null && id.equals(((Note) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", qualification='" + getQualification() + "'" +
            ", period='" + getPeriod() + "'" +
            ", startDate='" + getStartDate() + "'" +
            "}";
    }
}
