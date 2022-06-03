package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.CeetUser;
import co.edu.sena.repository.CeetUserRepository;
import co.edu.sena.service.dto.CeetUserDTO;
import co.edu.sena.service.mapper.CeetUserMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CeetUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CeetUserResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ceet-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CeetUserRepository ceetUserRepository;

    @Autowired
    private CeetUserMapper ceetUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCeetUserMockMvc;

    private CeetUser ceetUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CeetUser createEntity(EntityManager em) {
        CeetUser ceetUser = new CeetUser()
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .documentNumber(DEFAULT_DOCUMENT_NUMBER)
            .documentType(DEFAULT_DOCUMENT_TYPE);
        return ceetUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CeetUser createUpdatedEntity(EntityManager em) {
        CeetUser ceetUser = new CeetUser()
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .documentType(UPDATED_DOCUMENT_TYPE);
        return ceetUser;
    }

    @BeforeEach
    public void initTest() {
        ceetUser = createEntity(em);
    }

    @Test
    @Transactional
    void createCeetUser() throws Exception {
        int databaseSizeBeforeCreate = ceetUserRepository.findAll().size();
        // Create the CeetUser
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);
        restCeetUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceetUserDTO)))
            .andExpect(status().isCreated());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeCreate + 1);
        CeetUser testCeetUser = ceetUserList.get(ceetUserList.size() - 1);
        assertThat(testCeetUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCeetUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCeetUser.getDocumentNumber()).isEqualTo(DEFAULT_DOCUMENT_NUMBER);
        assertThat(testCeetUser.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void createCeetUserWithExistingId() throws Exception {
        // Create the CeetUser with an existing ID
        ceetUser.setId(1L);
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        int databaseSizeBeforeCreate = ceetUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCeetUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceetUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = ceetUserRepository.findAll().size();
        // set the field null
        ceetUser.setPhone(null);

        // Create the CeetUser, which fails.
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        restCeetUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceetUserDTO)))
            .andExpect(status().isBadRequest());

        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = ceetUserRepository.findAll().size();
        // set the field null
        ceetUser.setAddress(null);

        // Create the CeetUser, which fails.
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        restCeetUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceetUserDTO)))
            .andExpect(status().isBadRequest());

        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = ceetUserRepository.findAll().size();
        // set the field null
        ceetUser.setDocumentNumber(null);

        // Create the CeetUser, which fails.
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        restCeetUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceetUserDTO)))
            .andExpect(status().isBadRequest());

        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ceetUserRepository.findAll().size();
        // set the field null
        ceetUser.setDocumentType(null);

        // Create the CeetUser, which fails.
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        restCeetUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceetUserDTO)))
            .andExpect(status().isBadRequest());

        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCeetUsers() throws Exception {
        // Initialize the database
        ceetUserRepository.saveAndFlush(ceetUser);

        // Get all the ceetUserList
        restCeetUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ceetUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)));
    }

    @Test
    @Transactional
    void getCeetUser() throws Exception {
        // Initialize the database
        ceetUserRepository.saveAndFlush(ceetUser);

        // Get the ceetUser
        restCeetUserMockMvc
            .perform(get(ENTITY_API_URL_ID, ceetUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ceetUser.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.documentNumber").value(DEFAULT_DOCUMENT_NUMBER))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingCeetUser() throws Exception {
        // Get the ceetUser
        restCeetUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCeetUser() throws Exception {
        // Initialize the database
        ceetUserRepository.saveAndFlush(ceetUser);

        int databaseSizeBeforeUpdate = ceetUserRepository.findAll().size();

        // Update the ceetUser
        CeetUser updatedCeetUser = ceetUserRepository.findById(ceetUser.getId()).get();
        // Disconnect from session so that the updates on updatedCeetUser are not directly saved in db
        em.detach(updatedCeetUser);
        updatedCeetUser
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .documentType(UPDATED_DOCUMENT_TYPE);
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(updatedCeetUser);

        restCeetUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ceetUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ceetUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeUpdate);
        CeetUser testCeetUser = ceetUserList.get(ceetUserList.size() - 1);
        assertThat(testCeetUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCeetUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCeetUser.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testCeetUser.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCeetUser() throws Exception {
        int databaseSizeBeforeUpdate = ceetUserRepository.findAll().size();
        ceetUser.setId(count.incrementAndGet());

        // Create the CeetUser
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCeetUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ceetUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ceetUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCeetUser() throws Exception {
        int databaseSizeBeforeUpdate = ceetUserRepository.findAll().size();
        ceetUser.setId(count.incrementAndGet());

        // Create the CeetUser
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeetUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ceetUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCeetUser() throws Exception {
        int databaseSizeBeforeUpdate = ceetUserRepository.findAll().size();
        ceetUser.setId(count.incrementAndGet());

        // Create the CeetUser
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeetUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceetUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCeetUserWithPatch() throws Exception {
        // Initialize the database
        ceetUserRepository.saveAndFlush(ceetUser);

        int databaseSizeBeforeUpdate = ceetUserRepository.findAll().size();

        // Update the ceetUser using partial update
        CeetUser partialUpdatedCeetUser = new CeetUser();
        partialUpdatedCeetUser.setId(ceetUser.getId());

        restCeetUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCeetUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCeetUser))
            )
            .andExpect(status().isOk());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeUpdate);
        CeetUser testCeetUser = ceetUserList.get(ceetUserList.size() - 1);
        assertThat(testCeetUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCeetUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCeetUser.getDocumentNumber()).isEqualTo(DEFAULT_DOCUMENT_NUMBER);
        assertThat(testCeetUser.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCeetUserWithPatch() throws Exception {
        // Initialize the database
        ceetUserRepository.saveAndFlush(ceetUser);

        int databaseSizeBeforeUpdate = ceetUserRepository.findAll().size();

        // Update the ceetUser using partial update
        CeetUser partialUpdatedCeetUser = new CeetUser();
        partialUpdatedCeetUser.setId(ceetUser.getId());

        partialUpdatedCeetUser
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .documentType(UPDATED_DOCUMENT_TYPE);

        restCeetUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCeetUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCeetUser))
            )
            .andExpect(status().isOk());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeUpdate);
        CeetUser testCeetUser = ceetUserList.get(ceetUserList.size() - 1);
        assertThat(testCeetUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCeetUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCeetUser.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testCeetUser.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCeetUser() throws Exception {
        int databaseSizeBeforeUpdate = ceetUserRepository.findAll().size();
        ceetUser.setId(count.incrementAndGet());

        // Create the CeetUser
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCeetUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ceetUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ceetUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCeetUser() throws Exception {
        int databaseSizeBeforeUpdate = ceetUserRepository.findAll().size();
        ceetUser.setId(count.incrementAndGet());

        // Create the CeetUser
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeetUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ceetUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCeetUser() throws Exception {
        int databaseSizeBeforeUpdate = ceetUserRepository.findAll().size();
        ceetUser.setId(count.incrementAndGet());

        // Create the CeetUser
        CeetUserDTO ceetUserDTO = ceetUserMapper.toDto(ceetUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeetUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ceetUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CeetUser in the database
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCeetUser() throws Exception {
        // Initialize the database
        ceetUserRepository.saveAndFlush(ceetUser);

        int databaseSizeBeforeDelete = ceetUserRepository.findAll().size();

        // Delete the ceetUser
        restCeetUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, ceetUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CeetUser> ceetUserList = ceetUserRepository.findAll();
        assertThat(ceetUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
