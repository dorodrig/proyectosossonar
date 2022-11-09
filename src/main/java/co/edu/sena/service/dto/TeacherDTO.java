package co.edu.sena.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.edu.sena.domain.Teacher} entity.
 */
public class TeacherDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String name;

    @NotNull
    @Size(max = 10)
    private String lastName;

    @NotNull
    @Size(max = 10)
    private String phone;

    @NotNull
    @Size(max = 80)
    private String address;

    @NotNull
    @Size(max = 20)
    private String documentNumber;

    @NotNull
    @Size(max = 10)
    private String documentType;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeacherDTO)) {
            return false;
        }

        TeacherDTO teacherDTO = (TeacherDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teacherDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeacherDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
