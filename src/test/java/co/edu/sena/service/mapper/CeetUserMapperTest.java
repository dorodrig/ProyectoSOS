package co.edu.sena.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CeetUserMapperTest {

    private CeetUserMapper ceetUserMapper;

    @BeforeEach
    public void setUp() {
        ceetUserMapper = new CeetUserMapperImpl();
    }
}
