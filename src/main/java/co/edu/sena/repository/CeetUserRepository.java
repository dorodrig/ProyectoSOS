package co.edu.sena.repository;

import co.edu.sena.domain.CeetUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CeetUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CeetUserRepository extends JpaRepository<CeetUser, Long> {}
