package com.teamdev.service;

import com.teamdev.service.dto.PersonnesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Personnes.
 */
public interface PersonnesService {

    /**
     * Save a personnes.
     *
     * @param personnesDTO the entity to save
     * @return the persisted entity
     */
    PersonnesDTO save(PersonnesDTO personnesDTO);

    /**
     * Get all the personnes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PersonnesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" personnes.
     *
     * @param id the id of the entity
     * @return the entity
     */
    PersonnesDTO findOne(Long id);

    /**
     * Delete the "id" personnes.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
