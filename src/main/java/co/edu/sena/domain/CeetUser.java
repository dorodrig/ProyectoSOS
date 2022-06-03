package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CeetUser.
 */
@Entity
@Table(name = "ceet_user")
public class CeetUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "phone", length = 10, nullable = false)
    private String phone;

    @NotNull
    @Size(max = 80)
    @Column(name = "address", length = 80, nullable = false)
    private String address;

    @NotNull
    @Size(max = 20)
    @Column(name = "document_number", length = 20, nullable = false)
    private String documentNumber;

    @NotNull
    @Size(max = 10)
    @Column(name = "document_type", length = 10, nullable = false)
    private String documentType;

    @JsonIgnoreProperties(value = { "enrollment", "ceetUser" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Student student;

    @JsonIgnoreProperties(value = { "ceetUser", "courses" }, allowSetters = true)
    @OneToOne(mappedBy = "ceetUser")
    private Teacher teacher;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CeetUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return this.phone;
    }

    public CeetUser phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public CeetUser address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocumentNumber() {
        return this.documentNumber;
    }

    public CeetUser documentNumber(String documentNumber) {
        this.setDocumentNumber(documentNumber);
        return this;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public CeetUser documentType(String documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public CeetUser student(Student student) {
        this.setStudent(student);
        return this;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public void setTeacher(Teacher teacher) {
        if (this.teacher != null) {
            this.teacher.setCeetUser(null);
        }
        if (teacher != null) {
            teacher.setCeetUser(this);
        }
        this.teacher = teacher;
    }

    public CeetUser teacher(Teacher teacher) {
        this.setTeacher(teacher);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CeetUser)) {
            return false;
        }
        return id != null && id.equals(((CeetUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CeetUser{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            "}";
    }
}
