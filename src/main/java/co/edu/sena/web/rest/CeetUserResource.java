package co.edu.sena.web.rest;

import co.edu.sena.repository.CeetUserRepository;
import co.edu.sena.service.CeetUserService;
import co.edu.sena.service.dto.CeetUserDTO;
import co.edu.sena.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.edu.sena.domain.CeetUser}.
 */
@RestController
@RequestMapping("/api")
public class CeetUserResource {

    private final Logger log = LoggerFactory.getLogger(CeetUserResource.class);

    private static final String ENTITY_NAME = "ceetUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CeetUserService ceetUserService;

    private final CeetUserRepository ceetUserRepository;

    public CeetUserResource(CeetUserService ceetUserService, CeetUserRepository ceetUserRepository) {
        this.ceetUserService = ceetUserService;
        this.ceetUserRepository = ceetUserRepository;
    }

    /**
     * {@code POST  /ceet-users} : Create a new ceetUser.
     *
     * @param ceetUserDTO the ceetUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ceetUserDTO, or with status {@code 400 (Bad Request)} if the ceetUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ceet-users")
    public ResponseEntity<CeetUserDTO> createCeetUser(@Valid @RequestBody CeetUserDTO ceetUserDTO) throws URISyntaxException {
        log.debug("REST request to save CeetUser : {}", ceetUserDTO);
        if (ceetUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new ceetUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CeetUserDTO result = ceetUserService.save(ceetUserDTO);
        return ResponseEntity
            .created(new URI("/api/ceet-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ceet-users/:id} : Updates an existing ceetUser.
     *
     * @param id the id of the ceetUserDTO to save.
     * @param ceetUserDTO the ceetUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ceetUserDTO,
     * or with status {@code 400 (Bad Request)} if the ceetUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ceetUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ceet-users/{id}")
    public ResponseEntity<CeetUserDTO> updateCeetUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CeetUserDTO ceetUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CeetUser : {}, {}", id, ceetUserDTO);
        if (ceetUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ceetUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ceetUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CeetUserDTO result = ceetUserService.update(ceetUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ceetUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ceet-users/:id} : Partial updates given fields of an existing ceetUser, field will ignore if it is null
     *
     * @param id the id of the ceetUserDTO to save.
     * @param ceetUserDTO the ceetUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ceetUserDTO,
     * or with status {@code 400 (Bad Request)} if the ceetUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ceetUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ceetUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ceet-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CeetUserDTO> partialUpdateCeetUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CeetUserDTO ceetUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CeetUser partially : {}, {}", id, ceetUserDTO);
        if (ceetUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ceetUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ceetUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CeetUserDTO> result = ceetUserService.partialUpdate(ceetUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ceetUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ceet-users} : get all the ceetUsers.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ceetUsers in body.
     */
    @GetMapping("/ceet-users")
    public ResponseEntity<List<CeetUserDTO>> getAllCeetUsers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("teacher-is-null".equals(filter)) {
            log.debug("REST request to get all CeetUsers where teacher is null");
            return new ResponseEntity<>(ceetUserService.findAllWhereTeacherIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of CeetUsers");
        Page<CeetUserDTO> page = ceetUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ceet-users/:id} : get the "id" ceetUser.
     *
     * @param id the id of the ceetUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ceetUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ceet-users/{id}")
    public ResponseEntity<CeetUserDTO> getCeetUser(@PathVariable Long id) {
        log.debug("REST request to get CeetUser : {}", id);
        Optional<CeetUserDTO> ceetUserDTO = ceetUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ceetUserDTO);
    }

    /**
     * {@code DELETE  /ceet-users/:id} : delete the "id" ceetUser.
     *
     * @param id the id of the ceetUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ceet-users/{id}")
    public ResponseEntity<Void> deleteCeetUser(@PathVariable Long id) {
        log.debug("REST request to delete CeetUser : {}", id);
        ceetUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
