package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CeetUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CeetUser.class);
        CeetUser ceetUser1 = new CeetUser();
        ceetUser1.setId(1L);
        CeetUser ceetUser2 = new CeetUser();
        ceetUser2.setId(ceetUser1.getId());
        assertThat(ceetUser1).isEqualTo(ceetUser2);
        ceetUser2.setId(2L);
        assertThat(ceetUser1).isNotEqualTo(ceetUser2);
        ceetUser1.setId(null);
        assertThat(ceetUser1).isNotEqualTo(ceetUser2);
    }
}
