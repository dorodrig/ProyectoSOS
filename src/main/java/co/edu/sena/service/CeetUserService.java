package co.edu.sena.service;

import co.edu.sena.service.dto.CeetUserDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.sena.domain.CeetUser}.
 */
public interface CeetUserService {
    /**
     * Save a ceetUser.
     *
     * @param ceetUserDTO the entity to save.
     * @return the persisted entity.
     */
    CeetUserDTO save(CeetUserDTO ceetUserDTO);

    /**
     * Updates a ceetUser.
     *
     * @param ceetUserDTO the entity to update.
     * @return the persisted entity.
     */
    CeetUserDTO update(CeetUserDTO ceetUserDTO);

    /**
     * Partially updates a ceetUser.
     *
     * @param ceetUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CeetUserDTO> partialUpdate(CeetUserDTO ceetUserDTO);

    /**
     * Get all the ceetUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CeetUserDTO> findAll(Pageable pageable);
    /**
     * Get all the CeetUserDTO where Teacher is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CeetUserDTO> findAllWhereTeacherIsNull();

    /**
     * Get the "id" ceetUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CeetUserDTO> findOne(Long id);

    /**
     * Delete the "id" ceetUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
