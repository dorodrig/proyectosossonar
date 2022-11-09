package co.edu.sena.service.mapper;

import co.edu.sena.domain.Enrollment;
import co.edu.sena.service.dto.EnrollmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enrollment} and its DTO {@link EnrollmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnrollmentMapper extends EntityMapper<EnrollmentDTO, Enrollment> {}
