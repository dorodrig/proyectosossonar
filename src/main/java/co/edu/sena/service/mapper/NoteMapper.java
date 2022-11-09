package co.edu.sena.service.mapper;

import co.edu.sena.domain.Course;
import co.edu.sena.domain.Note;
import co.edu.sena.service.dto.CourseDTO;
import co.edu.sena.service.dto.NoteDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {
    @Mapping(target = "courses", source = "courses", qualifiedByName = "courseIdSet")
    NoteDTO toDto(Note s);

    @Mapping(target = "removeCourse", ignore = true)
    Note toEntity(NoteDTO noteDTO);

    @Named("courseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourseDTO toDtoCourseId(Course course);

    @Named("courseIdSet")
    default Set<CourseDTO> toDtoCourseIdSet(Set<Course> course) {
        return course.stream().map(this::toDtoCourseId).collect(Collectors.toSet());
    }
}
