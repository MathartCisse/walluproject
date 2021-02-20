package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.WalluApp;
import com.mycompany.myapp.domain.Patient;
import com.mycompany.myapp.repository.PatientRepository;
import com.mycompany.myapp.repository.search.PatientSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@SpringBootTest(classes = WalluApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PatientResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Long DEFAULT_AGE = 1L;
    private static final Long UPDATED_AGE = 2L;

    private static final Long DEFAULT_SALARY = 1L;
    private static final Long UPDATED_SALARY = 2L;

    private static final Long DEFAULT_ID_CARD_NUMBER = 1L;
    private static final Long UPDATED_ID_CARD_NUMBER = 2L;

    @Autowired
    private PatientRepository patientRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.PatientSearchRepositoryMockConfiguration
     */
    @Autowired
    private PatientSearchRepository mockPatientSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .age(DEFAULT_AGE)
            .salary(DEFAULT_SALARY)
            .idCardNumber(DEFAULT_ID_CARD_NUMBER);
        return patient;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .age(UPDATED_AGE)
            .salary(UPDATED_SALARY)
            .idCardNumber(UPDATED_ID_CARD_NUMBER);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();
        // Create the Patient
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPatient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPatient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPatient.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPatient.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testPatient.getIdCardNumber()).isEqualTo(DEFAULT_ID_CARD_NUMBER);

        // Validate the Patient in Elasticsearch
        verify(mockPatientSearchRepository, times(1)).save(testPatient);
    }

    @Test
    @Transactional
    public void createPatientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient with an existing ID
        patient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);

        // Validate the Patient in Elasticsearch
        verify(mockPatientSearchRepository, times(0)).save(patient);
    }


    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.intValue())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].idCardNumber").value(hasItem(DEFAULT_ID_CARD_NUMBER.intValue())));
    }
    
    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.intValue()))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.intValue()))
            .andExpect(jsonPath("$.idCardNumber").value(DEFAULT_ID_CARD_NUMBER.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .age(UPDATED_AGE)
            .salary(UPDATED_SALARY)
            .idCardNumber(UPDATED_ID_CARD_NUMBER);

        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatient)))
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPatient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPatient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPatient.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPatient.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testPatient.getIdCardNumber()).isEqualTo(UPDATED_ID_CARD_NUMBER);

        // Validate the Patient in Elasticsearch
        verify(mockPatientSearchRepository, times(1)).save(testPatient);
    }

    @Test
    @Transactional
    public void updateNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Patient in Elasticsearch
        verify(mockPatientSearchRepository, times(0)).save(patient);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Patient in Elasticsearch
        verify(mockPatientSearchRepository, times(1)).deleteById(patient.getId());
    }

    @Test
    @Transactional
    public void searchPatient() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        when(mockPatientSearchRepository.search(queryStringQuery("id:" + patient.getId())))
            .thenReturn(Collections.singletonList(patient));

        // Search the patient
        restPatientMockMvc.perform(get("/api/_search/patients?query=id:" + patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.intValue())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].idCardNumber").value(hasItem(DEFAULT_ID_CARD_NUMBER.intValue())));
    }
}
