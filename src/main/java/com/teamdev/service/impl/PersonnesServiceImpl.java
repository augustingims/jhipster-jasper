package com.teamdev.service.impl;

import com.teamdev.service.PersonnesService;
import com.teamdev.domain.Personnes;
import com.teamdev.repository.PersonnesRepository;
import com.teamdev.service.dto.PersonnesDTO;
import com.teamdev.service.mapper.PersonnesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Personnes.
 */
@Service
@Transactional
public class PersonnesServiceImpl implements PersonnesService {

    private final Logger log = LoggerFactory.getLogger(PersonnesServiceImpl.class);

    private final PersonnesRepository personnesRepository;

    private final PersonnesMapper personnesMapper;

    public PersonnesServiceImpl(PersonnesRepository personnesRepository, PersonnesMapper personnesMapper) {
        this.personnesRepository = personnesRepository;
        this.personnesMapper = personnesMapper;
    }

    /**
     * Save a personnes.
     *
     * @param personnesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PersonnesDTO save(PersonnesDTO personnesDTO) {
        log.debug("Request to save Personnes : {}", personnesDTO);
        Personnes personnes = personnesMapper.toEntity(personnesDTO);
        personnes = personnesRepository.save(personnes);
        return personnesMapper.toDto(personnes);
    }

    /**
     * Get all the personnes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PersonnesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Personnes");
        return personnesRepository.findAll(pageable)
            .map(personnesMapper::toDto);
    }

    /**
     * Get one personnes by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PersonnesDTO findOne(Long id) {
        log.debug("Request to get Personnes : {}", id);
        Personnes personnes = personnesRepository.findOne(id);
        return personnesMapper.toDto(personnes);
    }

    /**
     * Delete the personnes by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Personnes : {}", id);
        personnesRepository.delete(id);
    }
}
