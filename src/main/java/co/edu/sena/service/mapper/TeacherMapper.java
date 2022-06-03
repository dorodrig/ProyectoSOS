package co.edu.sena.service.mapper;

import co.edu.sena.domain.CeetUser;
import co.edu.sena.domain.Course;
import co.edu.sena.domain.Teacher;
import co.edu.sena.service.dto.CeetUserDTO;
import co.edu.sena.service.dto.CourseDTO;
import co.edu.sena.service.dto.TeacherDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Teacher} and its DTO {@link TeacherDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeacherMapper extends EntityMapper<TeacherDTO, Teacher> {
    @Mapping(target = "ceetUser", source = "ceetUser", qualifiedByName = "ceetUserId")
    @Mapping(target = "courses", source = "courses", qualifiedByName = "courseIdSet")
    TeacherDTO toDto(Teacher s);

    @Mapping(target = "removeCourse", ignore = true)
    Teacher toEntity(TeacherDTO teacherDTO);

    @Named("ceetUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CeetUserDTO toDtoCeetUserId(CeetUser ceetUser);

    @Named("courseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourseDTO toDtoCourseId(Course course);

    @Named("courseIdSet")
    default Set<CourseDTO> toDtoCourseIdSet(Set<Course> course) {
        return course.stream().map(this::toDtoCourseId).collect(Collectors.toSet());
    }
}
