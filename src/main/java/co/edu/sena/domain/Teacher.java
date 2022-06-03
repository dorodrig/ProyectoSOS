package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Teacher.
 */
@Entity
@Table(name = "teacher")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "student", "teacher" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CeetUser ceetUser;

    @ManyToMany
    @JoinTable(
        name = "rel_teacher__course",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonIgnoreProperties(value = { "areas", "achievements", "notes", "teachers" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Teacher id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CeetUser getCeetUser() {
        return this.ceetUser;
    }

    public void setCeetUser(CeetUser ceetUser) {
        this.ceetUser = ceetUser;
    }

    public Teacher ceetUser(CeetUser ceetUser) {
        this.setCeetUser(ceetUser);
        return this;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Teacher courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Teacher addCourse(Course course) {
        this.courses.add(course);
        course.getTeachers().add(this);
        return this;
    }

    public Teacher removeCourse(Course course) {
        this.courses.remove(course);
        course.getTeachers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Teacher)) {
            return false;
        }
        return id != null && id.equals(((Teacher) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Teacher{" +
            "id=" + getId() +
            "}";
    }
}
