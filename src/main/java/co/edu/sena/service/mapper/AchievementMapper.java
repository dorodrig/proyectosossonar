package co.edu.sena.service.mapper;

import co.edu.sena.domain.Achievement;
import co.edu.sena.domain.Course;
import co.edu.sena.service.dto.AchievementDTO;
import co.edu.sena.service.dto.CourseDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Achievement} and its DTO {@link AchievementDTO}.
 */
@Mapper(componentModel = "spring")
public interface AchievementMapper extends EntityMapper<AchievementDTO, Achievement> {
    @Mapping(target = "courses", source = "courses", qualifiedByName = "courseIdSet")
    AchievementDTO toDto(Achievement s);

    @Mapping(target = "removeCourse", ignore = true)
    Achievement toEntity(AchievementDTO achievementDTO);

    @Named("courseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourseDTO toDtoCourseId(Course course);

    @Named("courseIdSet")
    default Set<CourseDTO> toDtoCourseIdSet(Set<Course> course) {
        return course.stream().map(this::toDtoCourseId).collect(Collectors.toSet());
    }
}
