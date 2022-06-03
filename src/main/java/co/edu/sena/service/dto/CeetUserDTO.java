package co.edu.sena.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.edu.sena.domain.CeetUser} entity.
 */
public class CeetUserDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String phone;

    @NotNull
    @Size(max = 80)
    private String address;

    @NotNull
    @Size(max = 20)
    private String documentNumber;

    @NotNull
    @Size(max = 10)
    private String documentType;

    private StudentDTO student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CeetUserDTO)) {
            return false;
        }

        CeetUserDTO ceetUserDTO = (CeetUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ceetUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CeetUserDTO{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", student=" + getStudent() +
            "}";
    }
}
