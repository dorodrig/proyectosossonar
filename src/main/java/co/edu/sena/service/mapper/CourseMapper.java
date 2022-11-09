package co.edu.sena.service.mapper;

import co.edu.sena.domain.Area;
import co.edu.sena.domain.Course;
import co.edu.sena.service.dto.AreaDTO;
import co.edu.sena.service.dto.CourseDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {
    @Mapping(target = "areas", source = "areas", qualifiedByName = "areaIdSet")
    CourseDTO toDto(Course s);

    @Mapping(target = "removeArea", ignore = true)
    Course toEntity(CourseDTO courseDTO);

    @Named("areaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AreaDTO toDtoAreaId(Area area);

    @Named("areaIdSet")
    default Set<AreaDTO> toDtoAreaIdSet(Set<Area> area) {
        return area.stream().map(this::toDtoAreaId).collect(Collectors.toSet());
    }
}
