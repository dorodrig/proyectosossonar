package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "attendant_name", length = 30, nullable = false)
    private String attendantName;

    @NotNull
    @Size(max = 20)
    @Column(name = "kin", length = 20, nullable = false)
    private String kin;

    @JsonIgnoreProperties(value = { "student", "levels" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Enrollment enrollment;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Student id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttendantName() {
        return this.attendantName;
    }

    public Student attendantName(String attendantName) {
        this.setAttendantName(attendantName);
        return this;
    }

    public void setAttendantName(String attendantName) {
        this.attendantName = attendantName;
    }

    public String getKin() {
        return this.kin;
    }

    public Student kin(String kin) {
        this.setKin(kin);
        return this;
    }

    public void setKin(String kin) {
        this.kin = kin;
    }

    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Student enrollment(Enrollment enrollment) {
        this.setEnrollment(enrollment);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Student user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", attendantName='" + getAttendantName() + "'" +
            ", kin='" + getKin() + "'" +
            "}";
    }
}
