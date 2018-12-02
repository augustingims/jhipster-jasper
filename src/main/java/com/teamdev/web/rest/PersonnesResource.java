package com.teamdev.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamdev.service.PersonnesService;
import com.teamdev.web.rest.errors.BadRequestAlertException;
import com.teamdev.web.rest.util.HeaderUtil;
import com.teamdev.web.rest.util.PaginationUtil;
import com.teamdev.service.dto.PersonnesDTO;
import com.teamdev.service.dto.PersonnesCriteria;
import com.teamdev.service.PersonnesQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Personnes.
 */
@RestController
@RequestMapping("/api")
public class PersonnesResource {

    private final Logger log = LoggerFactory.getLogger(PersonnesResource.class);

    private static final String ENTITY_NAME = "personnes";

    private final PersonnesService personnesService;

    private final PersonnesQueryService personnesQueryService;

    public PersonnesResource(PersonnesService personnesService, PersonnesQueryService personnesQueryService) {
        this.personnesService = personnesService;
        this.personnesQueryService = personnesQueryService;
    }

    /**
     * POST  /personnes : Create a new personnes.
     *
     * @param personnesDTO the personnesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personnesDTO, or with status 400 (Bad Request) if the personnes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/personnes")
    @Timed
    public ResponseEntity<PersonnesDTO> createPersonnes(@RequestBody PersonnesDTO personnesDTO) throws URISyntaxException {
        log.debug("REST request to save Personnes : {}", personnesDTO);
        if (personnesDTO.getId() != null) {
            throw new BadRequestAlertException("A new personnes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonnesDTO result = personnesService.save(personnesDTO);
        return ResponseEntity.created(new URI("/api/personnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personnes : Updates an existing personnes.
     *
     * @param personnesDTO the personnesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personnesDTO,
     * or with status 400 (Bad Request) if the personnesDTO is not valid,
     * or with status 500 (Internal Server Error) if the personnesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/personnes")
    @Timed
    public ResponseEntity<PersonnesDTO> updatePersonnes(@RequestBody PersonnesDTO personnesDTO) throws URISyntaxException {
        log.debug("REST request to update Personnes : {}", personnesDTO);
        if (personnesDTO.getId() == null) {
            return createPersonnes(personnesDTO);
        }
        PersonnesDTO result = personnesService.save(personnesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, personnesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personnes : get all the personnes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of personnes in body
     */
    @GetMapping("/personnes")
    @Timed
    public ResponseEntity<List<PersonnesDTO>> getAllPersonnes(PersonnesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Personnes by criteria: {}", criteria);
        Page<PersonnesDTO> page = personnesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/personnes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /personnes/:id : get the "id" personnes.
     *
     * @param id the id of the personnesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personnesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/personnes/{id}")
    @Timed
    public ResponseEntity<PersonnesDTO> getPersonnes(@PathVariable Long id) {
        log.debug("REST request to get Personnes : {}", id);
        PersonnesDTO personnesDTO = personnesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(personnesDTO));
    }

    /**
     * DELETE  /personnes/:id : delete the "id" personnes.
     *
     * @param id the id of the personnesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/personnes/{id}")
    @Timed
    public ResponseEntity<Void> deletePersonnes(@PathVariable Long id) {
        log.debug("REST request to delete Personnes : {}", id);
        personnesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
