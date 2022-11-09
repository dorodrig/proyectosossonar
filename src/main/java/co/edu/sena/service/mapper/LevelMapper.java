package co.edu.sena.service.mapper;

import co.edu.sena.domain.Enrollment;
import co.edu.sena.domain.Level;
import co.edu.sena.service.dto.EnrollmentDTO;
import co.edu.sena.service.dto.LevelDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Level} and its DTO {@link LevelDTO}.
 */
@Mapper(componentModel = "spring")
public interface LevelMapper extends EntityMapper<LevelDTO, Level> {
    @Mapping(target = "enrollments", source = "enrollments", qualifiedByName = "enrollmentIdSet")
    LevelDTO toDto(Level s);

    @Mapping(target = "removeEnrollment", ignore = true)
    Level toEntity(LevelDTO levelDTO);

    @Named("enrollmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnrollmentDTO toDtoEnrollmentId(Enrollment enrollment);

    @Named("enrollmentIdSet")
    default Set<EnrollmentDTO> toDtoEnrollmentIdSet(Set<Enrollment> enrollment) {
        return enrollment.stream().map(this::toDtoEnrollmentId).collect(Collectors.toSet());
    }
}
