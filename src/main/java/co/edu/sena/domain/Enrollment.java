package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Enrollment.
 */
@Entity
@Table(name = "enrollment")
public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "registration_number", length = 50, nullable = false)
    private String registrationNumber;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @JsonIgnoreProperties(value = { "enrollment", "user" }, allowSetters = true)
    @OneToOne(mappedBy = "enrollment")
    private Student student;

    @ManyToMany(mappedBy = "enrollments")
    @JsonIgnoreProperties(value = { "enrollments", "areas" }, allowSetters = true)
    private Set<Level> levels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enrollment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public Enrollment registrationNumber(String registrationNumber) {
        this.setRegistrationNumber(registrationNumber);
        return this;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public Enrollment startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        if (this.student != null) {
            this.student.setEnrollment(null);
        }
        if (student != null) {
            student.setEnrollment(this);
        }
        this.student = student;
    }

    public Enrollment student(Student student) {
        this.setStudent(student);
        return this;
    }

    public Set<Level> getLevels() {
        return this.levels;
    }

    public void setLevels(Set<Level> levels) {
        if (this.levels != null) {
            this.levels.forEach(i -> i.removeEnrollment(this));
        }
        if (levels != null) {
            levels.forEach(i -> i.addEnrollment(this));
        }
        this.levels = levels;
    }

    public Enrollment levels(Set<Level> levels) {
        this.setLevels(levels);
        return this;
    }

    public Enrollment addLevel(Level level) {
        this.levels.add(level);
        level.getEnrollments().add(this);
        return this;
    }

    public Enrollment removeLevel(Level level) {
        this.levels.remove(level);
        level.getEnrollments().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enrollment)) {
            return false;
        }
        return id != null && id.equals(((Enrollment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enrollment{" +
            "id=" + getId() +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", startDate='" + getStartDate() + "'" +
            "}";
    }
}
