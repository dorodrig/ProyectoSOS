package co.edu.sena.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link co.edu.sena.domain.Teacher} entity.
 */
public class TeacherDTO implements Serializable {

    private Long id;

    private CeetUserDTO ceetUser;

    private Set<CourseDTO> courses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CeetUserDTO getCeetUser() {
        return ceetUser;
    }

    public void setCeetUser(CeetUserDTO ceetUser) {
        this.ceetUser = ceetUser;
    }

    public Set<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseDTO> courses) {
        this.courses = courses;
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
            ", ceetUser=" + getCeetUser() +
            ", courses=" + getCourses() +
            "}";
    }
}
