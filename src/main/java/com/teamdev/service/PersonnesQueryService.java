package com.teamdev.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.teamdev.domain.Personnes;
import com.teamdev.domain.*; // for static metamodels
import com.teamdev.repository.PersonnesRepository;
import com.teamdev.service.dto.PersonnesCriteria;

import com.teamdev.service.dto.PersonnesDTO;
import com.teamdev.service.mapper.PersonnesMapper;

/**
 * Service for executing complex queries for Personnes entities in the database.
 * The main input is a {@link PersonnesCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonnesDTO} or a {@link Page} of {@link PersonnesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonnesQueryService extends QueryService<Personnes> {

    private final Logger log = LoggerFactory.getLogger(PersonnesQueryService.class);


    private final PersonnesRepository personnesRepository;

    private final PersonnesMapper personnesMapper;

    public PersonnesQueryService(PersonnesRepository personnesRepository, PersonnesMapper personnesMapper) {
        this.personnesRepository = personnesRepository;
        this.personnesMapper = personnesMapper;
    }

    /**
     * Return a {@link List} of {@link PersonnesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonnesDTO> findByCriteria(PersonnesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Personnes> specification = createSpecification(criteria);
        return personnesMapper.toDto(personnesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PersonnesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonnesDTO> findByCriteria(PersonnesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Personnes> specification = createSpecification(criteria);
        final Page<Personnes> result = personnesRepository.findAll(specification, page);
        return result.map(personnesMapper::toDto);
    }

    /**
     * Function to convert PersonnesCriteria to a {@link Specifications}
     */
    private Specifications<Personnes> createSpecification(PersonnesCriteria criteria) {
        Specifications<Personnes> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Personnes_.id));
            }
            if (criteria.getNoms() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNoms(), Personnes_.noms));
            }
            if (criteria.getPrenoms() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenoms(), Personnes_.prenoms));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSexe(), Personnes_.sexe));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Personnes_.telephone));
            }
            if (criteria.getPays() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPays(), Personnes_.pays));
            }
            if (criteria.getNationalite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationalite(), Personnes_.nationalite));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDateNaissance(), Personnes_.dateNaissance));
            }
            if (criteria.getLieuNaissance() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLieuNaissance(), Personnes_.lieuNaissance));
            }
        }
        return specification;
    }

}
