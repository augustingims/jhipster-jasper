package com.teamdev.web.rest;

import com.teamdev.JasperTestApp;

import com.teamdev.domain.Personnes;
import com.teamdev.repository.PersonnesRepository;
import com.teamdev.service.PersonnesService;
import com.teamdev.service.dto.PersonnesDTO;
import com.teamdev.service.mapper.PersonnesMapper;
import com.teamdev.web.rest.errors.ExceptionTranslator;
import com.teamdev.service.dto.PersonnesCriteria;
import com.teamdev.service.PersonnesQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.teamdev.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PersonnesResource REST controller.
 *
 * @see PersonnesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JasperTestApp.class)
public class PersonnesResourceIntTest {

    private static final String DEFAULT_NOMS = "AAAAAAAAAA";
    private static final String UPDATED_NOMS = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOMS = "AAAAAAAAAA";
    private static final String UPDATED_PRENOMS = "BBBBBBBBBB";

    private static final String DEFAULT_SEXE = "AAAAAAAAAA";
    private static final String UPDATED_SEXE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITE = "BBBBBBBBBB";

    private static final String DEFAULT_DATE_NAISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_DATE_NAISSANCE = "BBBBBBBBBB";

    private static final String DEFAULT_LIEU_NAISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISSANCE = "BBBBBBBBBB";

    @Autowired
    private PersonnesRepository personnesRepository;

    @Autowired
    private PersonnesMapper personnesMapper;

    @Autowired
    private PersonnesService personnesService;

    @Autowired
    private PersonnesQueryService personnesQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPersonnesMockMvc;

    private Personnes personnes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PersonnesResource personnesResource = new PersonnesResource(personnesService, personnesQueryService);
        this.restPersonnesMockMvc = MockMvcBuilders.standaloneSetup(personnesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personnes createEntity(EntityManager em) {
        Personnes personnes = new Personnes()
            .noms(DEFAULT_NOMS)
            .prenoms(DEFAULT_PRENOMS)
            .sexe(DEFAULT_SEXE)
            .telephone(DEFAULT_TELEPHONE)
            .pays(DEFAULT_PAYS)
            .nationalite(DEFAULT_NATIONALITE)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .lieuNaissance(DEFAULT_LIEU_NAISSANCE);
        return personnes;
    }

    @Before
    public void initTest() {
        personnes = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersonnes() throws Exception {
        int databaseSizeBeforeCreate = personnesRepository.findAll().size();

        // Create the Personnes
        PersonnesDTO personnesDTO = personnesMapper.toDto(personnes);
        restPersonnesMockMvc.perform(post("/api/personnes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personnesDTO)))
            .andExpect(status().isCreated());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeCreate + 1);
        Personnes testPersonnes = personnesList.get(personnesList.size() - 1);
        assertThat(testPersonnes.getNoms()).isEqualTo(DEFAULT_NOMS);
        assertThat(testPersonnes.getPrenoms()).isEqualTo(DEFAULT_PRENOMS);
        assertThat(testPersonnes.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testPersonnes.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testPersonnes.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testPersonnes.getNationalite()).isEqualTo(DEFAULT_NATIONALITE);
        assertThat(testPersonnes.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testPersonnes.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void createPersonnesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personnesRepository.findAll().size();

        // Create the Personnes with an existing ID
        personnes.setId(1L);
        PersonnesDTO personnesDTO = personnesMapper.toDto(personnes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonnesMockMvc.perform(post("/api/personnes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personnesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPersonnes() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList
        restPersonnesMockMvc.perform(get("/api/personnes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnes.getId().intValue())))
            .andExpect(jsonPath("$.[*].noms").value(hasItem(DEFAULT_NOMS.toString())))
            .andExpect(jsonPath("$.[*].prenoms").value(hasItem(DEFAULT_PRENOMS.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS.toString())))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE.toString())));
    }

    @Test
    @Transactional
    public void getPersonnes() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get the personnes
        restPersonnesMockMvc.perform(get("/api/personnes/{id}", personnes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(personnes.getId().intValue()))
            .andExpect(jsonPath("$.noms").value(DEFAULT_NOMS.toString()))
            .andExpect(jsonPath("$.prenoms").value(DEFAULT_PRENOMS.toString()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS.toString()))
            .andExpect(jsonPath("$.nationalite").value(DEFAULT_NATIONALITE.toString()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.lieuNaissance").value(DEFAULT_LIEU_NAISSANCE.toString()));
    }

    @Test
    @Transactional
    public void getAllPersonnesByNomsIsEqualToSomething() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where noms equals to DEFAULT_NOMS
        defaultPersonnesShouldBeFound("noms.equals=" + DEFAULT_NOMS);

        // Get all the personnesList where noms equals to UPDATED_NOMS
        defaultPersonnesShouldNotBeFound("noms.equals=" + UPDATED_NOMS);
    }

    @Test
    @Transactional
    public void getAllPersonnesByNomsIsInShouldWork() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where noms in DEFAULT_NOMS or UPDATED_NOMS
        defaultPersonnesShouldBeFound("noms.in=" + DEFAULT_NOMS + "," + UPDATED_NOMS);

        // Get all the personnesList where noms equals to UPDATED_NOMS
        defaultPersonnesShouldNotBeFound("noms.in=" + UPDATED_NOMS);
    }

    @Test
    @Transactional
    public void getAllPersonnesByNomsIsNullOrNotNull() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where noms is not null
        defaultPersonnesShouldBeFound("noms.specified=true");

        // Get all the personnesList where noms is null
        defaultPersonnesShouldNotBeFound("noms.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonnesByPrenomsIsEqualToSomething() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where prenoms equals to DEFAULT_PRENOMS
        defaultPersonnesShouldBeFound("prenoms.equals=" + DEFAULT_PRENOMS);

        // Get all the personnesList where prenoms equals to UPDATED_PRENOMS
        defaultPersonnesShouldNotBeFound("prenoms.equals=" + UPDATED_PRENOMS);
    }

    @Test
    @Transactional
    public void getAllPersonnesByPrenomsIsInShouldWork() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where prenoms in DEFAULT_PRENOMS or UPDATED_PRENOMS
        defaultPersonnesShouldBeFound("prenoms.in=" + DEFAULT_PRENOMS + "," + UPDATED_PRENOMS);

        // Get all the personnesList where prenoms equals to UPDATED_PRENOMS
        defaultPersonnesShouldNotBeFound("prenoms.in=" + UPDATED_PRENOMS);
    }

    @Test
    @Transactional
    public void getAllPersonnesByPrenomsIsNullOrNotNull() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where prenoms is not null
        defaultPersonnesShouldBeFound("prenoms.specified=true");

        // Get all the personnesList where prenoms is null
        defaultPersonnesShouldNotBeFound("prenoms.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonnesBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where sexe equals to DEFAULT_SEXE
        defaultPersonnesShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the personnesList where sexe equals to UPDATED_SEXE
        defaultPersonnesShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllPersonnesBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultPersonnesShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the personnesList where sexe equals to UPDATED_SEXE
        defaultPersonnesShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllPersonnesBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where sexe is not null
        defaultPersonnesShouldBeFound("sexe.specified=true");

        // Get all the personnesList where sexe is null
        defaultPersonnesShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonnesByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where telephone equals to DEFAULT_TELEPHONE
        defaultPersonnesShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the personnesList where telephone equals to UPDATED_TELEPHONE
        defaultPersonnesShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllPersonnesByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultPersonnesShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the personnesList where telephone equals to UPDATED_TELEPHONE
        defaultPersonnesShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllPersonnesByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where telephone is not null
        defaultPersonnesShouldBeFound("telephone.specified=true");

        // Get all the personnesList where telephone is null
        defaultPersonnesShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonnesByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where pays equals to DEFAULT_PAYS
        defaultPersonnesShouldBeFound("pays.equals=" + DEFAULT_PAYS);

        // Get all the personnesList where pays equals to UPDATED_PAYS
        defaultPersonnesShouldNotBeFound("pays.equals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllPersonnesByPaysIsInShouldWork() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where pays in DEFAULT_PAYS or UPDATED_PAYS
        defaultPersonnesShouldBeFound("pays.in=" + DEFAULT_PAYS + "," + UPDATED_PAYS);

        // Get all the personnesList where pays equals to UPDATED_PAYS
        defaultPersonnesShouldNotBeFound("pays.in=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllPersonnesByPaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where pays is not null
        defaultPersonnesShouldBeFound("pays.specified=true");

        // Get all the personnesList where pays is null
        defaultPersonnesShouldNotBeFound("pays.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonnesByNationaliteIsEqualToSomething() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where nationalite equals to DEFAULT_NATIONALITE
        defaultPersonnesShouldBeFound("nationalite.equals=" + DEFAULT_NATIONALITE);

        // Get all the personnesList where nationalite equals to UPDATED_NATIONALITE
        defaultPersonnesShouldNotBeFound("nationalite.equals=" + UPDATED_NATIONALITE);
    }

    @Test
    @Transactional
    public void getAllPersonnesByNationaliteIsInShouldWork() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where nationalite in DEFAULT_NATIONALITE or UPDATED_NATIONALITE
        defaultPersonnesShouldBeFound("nationalite.in=" + DEFAULT_NATIONALITE + "," + UPDATED_NATIONALITE);

        // Get all the personnesList where nationalite equals to UPDATED_NATIONALITE
        defaultPersonnesShouldNotBeFound("nationalite.in=" + UPDATED_NATIONALITE);
    }

    @Test
    @Transactional
    public void getAllPersonnesByNationaliteIsNullOrNotNull() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where nationalite is not null
        defaultPersonnesShouldBeFound("nationalite.specified=true");

        // Get all the personnesList where nationalite is null
        defaultPersonnesShouldNotBeFound("nationalite.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonnesByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where dateNaissance equals to DEFAULT_DATE_NAISSANCE
        defaultPersonnesShouldBeFound("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personnesList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultPersonnesShouldNotBeFound("dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllPersonnesByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where dateNaissance in DEFAULT_DATE_NAISSANCE or UPDATED_DATE_NAISSANCE
        defaultPersonnesShouldBeFound("dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE);

        // Get all the personnesList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultPersonnesShouldNotBeFound("dateNaissance.in=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllPersonnesByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where dateNaissance is not null
        defaultPersonnesShouldBeFound("dateNaissance.specified=true");

        // Get all the personnesList where dateNaissance is null
        defaultPersonnesShouldNotBeFound("dateNaissance.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonnesByLieuNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where lieuNaissance equals to DEFAULT_LIEU_NAISSANCE
        defaultPersonnesShouldBeFound("lieuNaissance.equals=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the personnesList where lieuNaissance equals to UPDATED_LIEU_NAISSANCE
        defaultPersonnesShouldNotBeFound("lieuNaissance.equals=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllPersonnesByLieuNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where lieuNaissance in DEFAULT_LIEU_NAISSANCE or UPDATED_LIEU_NAISSANCE
        defaultPersonnesShouldBeFound("lieuNaissance.in=" + DEFAULT_LIEU_NAISSANCE + "," + UPDATED_LIEU_NAISSANCE);

        // Get all the personnesList where lieuNaissance equals to UPDATED_LIEU_NAISSANCE
        defaultPersonnesShouldNotBeFound("lieuNaissance.in=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllPersonnesByLieuNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList where lieuNaissance is not null
        defaultPersonnesShouldBeFound("lieuNaissance.specified=true");

        // Get all the personnesList where lieuNaissance is null
        defaultPersonnesShouldNotBeFound("lieuNaissance.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPersonnesShouldBeFound(String filter) throws Exception {
        restPersonnesMockMvc.perform(get("/api/personnes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnes.getId().intValue())))
            .andExpect(jsonPath("$.[*].noms").value(hasItem(DEFAULT_NOMS.toString())))
            .andExpect(jsonPath("$.[*].prenoms").value(hasItem(DEFAULT_PRENOMS.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS.toString())))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPersonnesShouldNotBeFound(String filter) throws Exception {
        restPersonnesMockMvc.perform(get("/api/personnes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPersonnes() throws Exception {
        // Get the personnes
        restPersonnesMockMvc.perform(get("/api/personnes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonnes() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);
        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();

        // Update the personnes
        Personnes updatedPersonnes = personnesRepository.findOne(personnes.getId());
        // Disconnect from session so that the updates on updatedPersonnes are not directly saved in db
        em.detach(updatedPersonnes);
        updatedPersonnes
            .noms(UPDATED_NOMS)
            .prenoms(UPDATED_PRENOMS)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .pays(UPDATED_PAYS)
            .nationalite(UPDATED_NATIONALITE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE);
        PersonnesDTO personnesDTO = personnesMapper.toDto(updatedPersonnes);

        restPersonnesMockMvc.perform(put("/api/personnes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personnesDTO)))
            .andExpect(status().isOk());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate);
        Personnes testPersonnes = personnesList.get(personnesList.size() - 1);
        assertThat(testPersonnes.getNoms()).isEqualTo(UPDATED_NOMS);
        assertThat(testPersonnes.getPrenoms()).isEqualTo(UPDATED_PRENOMS);
        assertThat(testPersonnes.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testPersonnes.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testPersonnes.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testPersonnes.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testPersonnes.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testPersonnes.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingPersonnes() throws Exception {
        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();

        // Create the Personnes
        PersonnesDTO personnesDTO = personnesMapper.toDto(personnes);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPersonnesMockMvc.perform(put("/api/personnes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personnesDTO)))
            .andExpect(status().isCreated());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePersonnes() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);
        int databaseSizeBeforeDelete = personnesRepository.findAll().size();

        // Get the personnes
        restPersonnesMockMvc.perform(delete("/api/personnes/{id}", personnes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personnes.class);
        Personnes personnes1 = new Personnes();
        personnes1.setId(1L);
        Personnes personnes2 = new Personnes();
        personnes2.setId(personnes1.getId());
        assertThat(personnes1).isEqualTo(personnes2);
        personnes2.setId(2L);
        assertThat(personnes1).isNotEqualTo(personnes2);
        personnes1.setId(null);
        assertThat(personnes1).isNotEqualTo(personnes2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonnesDTO.class);
        PersonnesDTO personnesDTO1 = new PersonnesDTO();
        personnesDTO1.setId(1L);
        PersonnesDTO personnesDTO2 = new PersonnesDTO();
        assertThat(personnesDTO1).isNotEqualTo(personnesDTO2);
        personnesDTO2.setId(personnesDTO1.getId());
        assertThat(personnesDTO1).isEqualTo(personnesDTO2);
        personnesDTO2.setId(2L);
        assertThat(personnesDTO1).isNotEqualTo(personnesDTO2);
        personnesDTO1.setId(null);
        assertThat(personnesDTO1).isNotEqualTo(personnesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(personnesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(personnesMapper.fromId(null)).isNull();
    }
}
