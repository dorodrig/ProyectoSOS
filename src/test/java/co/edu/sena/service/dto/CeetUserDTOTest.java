package co.edu.sena.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CeetUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CeetUserDTO.class);
        CeetUserDTO ceetUserDTO1 = new CeetUserDTO();
        ceetUserDTO1.setId(1L);
        CeetUserDTO ceetUserDTO2 = new CeetUserDTO();
        assertThat(ceetUserDTO1).isNotEqualTo(ceetUserDTO2);
        ceetUserDTO2.setId(ceetUserDTO1.getId());
        assertThat(ceetUserDTO1).isEqualTo(ceetUserDTO2);
        ceetUserDTO2.setId(2L);
        assertThat(ceetUserDTO1).isNotEqualTo(ceetUserDTO2);
        ceetUserDTO1.setId(null);
        assertThat(ceetUserDTO1).isNotEqualTo(ceetUserDTO2);
    }
}
