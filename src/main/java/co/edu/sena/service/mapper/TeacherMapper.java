package co.edu.sena.service.mapper;

import co.edu.sena.domain.Teacher;
import co.edu.sena.domain.User;
import co.edu.sena.service.dto.TeacherDTO;
import co.edu.sena.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Teacher} and its DTO {@link TeacherDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeacherMapper extends EntityMapper<TeacherDTO, Teacher> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    TeacherDTO toDto(Teacher s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
