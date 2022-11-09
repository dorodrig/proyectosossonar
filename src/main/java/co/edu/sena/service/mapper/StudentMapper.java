package co.edu.sena.service.mapper;

import co.edu.sena.domain.Enrollment;
import co.edu.sena.domain.Student;
import co.edu.sena.domain.User;
import co.edu.sena.service.dto.EnrollmentDTO;
import co.edu.sena.service.dto.StudentDTO;
import co.edu.sena.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Student} and its DTO {@link StudentDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {
    @Mapping(target = "enrollment", source = "enrollment", qualifiedByName = "enrollmentId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    StudentDTO toDto(Student s);

    @Named("enrollmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnrollmentDTO toDtoEnrollmentId(Enrollment enrollment);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
