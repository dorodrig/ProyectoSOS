package co.edu.sena.service.impl;

import co.edu.sena.domain.CeetUser;
import co.edu.sena.repository.CeetUserRepository;
import co.edu.sena.service.CeetUserService;
import co.edu.sena.service.dto.CeetUserDTO;
import co.edu.sena.service.mapper.CeetUserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CeetUser}.
 */
@Service
@Transactional
public class CeetUserServiceImpl implements CeetUserService {

    private final Logger log = LoggerFactory.getLogger(CeetUserServiceImpl.class);

    private final CeetUserRepository ceetUserRepository;

    private final CeetUserMapper ceetUserMapper;

    public CeetUserServiceImpl(CeetUserRepository ceetUserRepository, CeetUserMapper ceetUserMapper) {
        this.ceetUserRepository = ceetUserRepository;
        this.ceetUserMapper = ceetUserMapper;
    }

    @Override
    public CeetUserDTO save(CeetUserDTO ceetUserDTO) {
        log.debug("Request to save CeetUser : {}", ceetUserDTO);
        CeetUser ceetUser = ceetUserMapper.toEntity(ceetUserDTO);
        ceetUser = ceetUserRepository.save(ceetUser);
        return ceetUserMapper.toDto(ceetUser);
    }

    @Override
    public CeetUserDTO update(CeetUserDTO ceetUserDTO) {
        log.debug("Request to save CeetUser : {}", ceetUserDTO);
        CeetUser ceetUser = ceetUserMapper.toEntity(ceetUserDTO);
        ceetUser = ceetUserRepository.save(ceetUser);
        return ceetUserMapper.toDto(ceetUser);
    }

    @Override
    public Optional<CeetUserDTO> partialUpdate(CeetUserDTO ceetUserDTO) {
        log.debug("Request to partially update CeetUser : {}", ceetUserDTO);

        return ceetUserRepository
            .findById(ceetUserDTO.getId())
            .map(existingCeetUser -> {
                ceetUserMapper.partialUpdate(existingCeetUser, ceetUserDTO);

                return existingCeetUser;
            })
            .map(ceetUserRepository::save)
            .map(ceetUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CeetUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CeetUsers");
        return ceetUserRepository.findAll(pageable).map(ceetUserMapper::toDto);
    }

    /**
     *  Get all the ceetUsers where Teacher is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CeetUserDTO> findAllWhereTeacherIsNull() {
        log.debug("Request to get all ceetUsers where Teacher is null");
        return StreamSupport
            .stream(ceetUserRepository.findAll().spliterator(), false)
            .filter(ceetUser -> ceetUser.getTeacher() == null)
            .map(ceetUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CeetUserDTO> findOne(Long id) {
        log.debug("Request to get CeetUser : {}", id);
        return ceetUserRepository.findById(id).map(ceetUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CeetUser : {}", id);
        ceetUserRepository.deleteById(id);
    }
}
