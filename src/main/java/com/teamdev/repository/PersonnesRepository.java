package com.teamdev.repository;

import com.teamdev.domain.Personnes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Personnes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonnesRepository extends JpaRepository<Personnes, Long>, JpaSpecificationExecutor<Personnes> {

}
