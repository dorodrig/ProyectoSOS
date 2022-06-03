package co.edu.sena.service.mapper;

import co.edu.sena.domain.CeetUser;
import co.edu.sena.domain.Student;
import co.edu.sena.service.dto.CeetUserDTO;
import co.edu.sena.service.dto.StudentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CeetUser} and its DTO {@link CeetUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface CeetUserMapper extends EntityMapper<CeetUserDTO, CeetUser> {
    @Mapping(target = "student", source = "student", qualifiedByName = "studentId")
    CeetUserDTO toDto(CeetUser s);

    @Named("studentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentDTO toDtoStudentId(Student student);
}
