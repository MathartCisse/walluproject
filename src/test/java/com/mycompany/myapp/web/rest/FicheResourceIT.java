package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.WalluApp;
import com.mycompany.myapp.domain.Fiche;
import com.mycompany.myapp.repository.FicheRepository;
import com.mycompany.myapp.repository.search.FicheSearchRepository;

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
 * Integration tests for the {@link FicheResource} REST controller.
 */
@SpringBootTest(classes = WalluApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FicheResourceIT {

    private static final String DEFAULT_GROUPE_SANGUIN = "AAAAAAAAAA";
    private static final String UPDATED_GROUPE_SANGUIN = "BBBBBBBBBB";

    private static final Long DEFAULT_POIDS = 1L;
    private static final Long UPDATED_POIDS = 2L;

    private static final Long DEFAULT_TAILLE = 1L;
    private static final Long UPDATED_TAILLE = 2L;

    @Autowired
    private FicheRepository ficheRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.FicheSearchRepositoryMockConfiguration
     */
    @Autowired
    private FicheSearchRepository mockFicheSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFicheMockMvc;

    private Fiche fiche;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fiche createEntity(EntityManager em) {
        Fiche fiche = new Fiche()
            .groupeSanguin(DEFAULT_GROUPE_SANGUIN)
            .poids(DEFAULT_POIDS)
            .taille(DEFAULT_TAILLE);
        return fiche;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fiche createUpdatedEntity(EntityManager em) {
        Fiche fiche = new Fiche()
            .groupeSanguin(UPDATED_GROUPE_SANGUIN)
            .poids(UPDATED_POIDS)
            .taille(UPDATED_TAILLE);
        return fiche;
    }

    @BeforeEach
    public void initTest() {
        fiche = createEntity(em);
    }

    @Test
    @Transactional
    public void createFiche() throws Exception {
        int databaseSizeBeforeCreate = ficheRepository.findAll().size();
        // Create the Fiche
        restFicheMockMvc.perform(post("/api/fiches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fiche)))
            .andExpect(status().isCreated());

        // Validate the Fiche in the database
        List<Fiche> ficheList = ficheRepository.findAll();
        assertThat(ficheList).hasSize(databaseSizeBeforeCreate + 1);
        Fiche testFiche = ficheList.get(ficheList.size() - 1);
        assertThat(testFiche.getGroupeSanguin()).isEqualTo(DEFAULT_GROUPE_SANGUIN);
        assertThat(testFiche.getPoids()).isEqualTo(DEFAULT_POIDS);
        assertThat(testFiche.getTaille()).isEqualTo(DEFAULT_TAILLE);

        // Validate the Fiche in Elasticsearch
        verify(mockFicheSearchRepository, times(1)).save(testFiche);
    }

    @Test
    @Transactional
    public void createFicheWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ficheRepository.findAll().size();

        // Create the Fiche with an existing ID
        fiche.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFicheMockMvc.perform(post("/api/fiches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fiche)))
            .andExpect(status().isBadRequest());

        // Validate the Fiche in the database
        List<Fiche> ficheList = ficheRepository.findAll();
        assertThat(ficheList).hasSize(databaseSizeBeforeCreate);

        // Validate the Fiche in Elasticsearch
        verify(mockFicheSearchRepository, times(0)).save(fiche);
    }


    @Test
    @Transactional
    public void getAllFiches() throws Exception {
        // Initialize the database
        ficheRepository.saveAndFlush(fiche);

        // Get all the ficheList
        restFicheMockMvc.perform(get("/api/fiches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiche.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupeSanguin").value(hasItem(DEFAULT_GROUPE_SANGUIN)))
            .andExpect(jsonPath("$.[*].poids").value(hasItem(DEFAULT_POIDS.intValue())))
            .andExpect(jsonPath("$.[*].taille").value(hasItem(DEFAULT_TAILLE.intValue())));
    }
    
    @Test
    @Transactional
    public void getFiche() throws Exception {
        // Initialize the database
        ficheRepository.saveAndFlush(fiche);

        // Get the fiche
        restFicheMockMvc.perform(get("/api/fiches/{id}", fiche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fiche.getId().intValue()))
            .andExpect(jsonPath("$.groupeSanguin").value(DEFAULT_GROUPE_SANGUIN))
            .andExpect(jsonPath("$.poids").value(DEFAULT_POIDS.intValue()))
            .andExpect(jsonPath("$.taille").value(DEFAULT_TAILLE.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingFiche() throws Exception {
        // Get the fiche
        restFicheMockMvc.perform(get("/api/fiches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFiche() throws Exception {
        // Initialize the database
        ficheRepository.saveAndFlush(fiche);

        int databaseSizeBeforeUpdate = ficheRepository.findAll().size();

        // Update the fiche
        Fiche updatedFiche = ficheRepository.findById(fiche.getId()).get();
        // Disconnect from session so that the updates on updatedFiche are not directly saved in db
        em.detach(updatedFiche);
        updatedFiche
            .groupeSanguin(UPDATED_GROUPE_SANGUIN)
            .poids(UPDATED_POIDS)
            .taille(UPDATED_TAILLE);

        restFicheMockMvc.perform(put("/api/fiches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFiche)))
            .andExpect(status().isOk());

        // Validate the Fiche in the database
        List<Fiche> ficheList = ficheRepository.findAll();
        assertThat(ficheList).hasSize(databaseSizeBeforeUpdate);
        Fiche testFiche = ficheList.get(ficheList.size() - 1);
        assertThat(testFiche.getGroupeSanguin()).isEqualTo(UPDATED_GROUPE_SANGUIN);
        assertThat(testFiche.getPoids()).isEqualTo(UPDATED_POIDS);
        assertThat(testFiche.getTaille()).isEqualTo(UPDATED_TAILLE);

        // Validate the Fiche in Elasticsearch
        verify(mockFicheSearchRepository, times(1)).save(testFiche);
    }

    @Test
    @Transactional
    public void updateNonExistingFiche() throws Exception {
        int databaseSizeBeforeUpdate = ficheRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFicheMockMvc.perform(put("/api/fiches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fiche)))
            .andExpect(status().isBadRequest());

        // Validate the Fiche in the database
        List<Fiche> ficheList = ficheRepository.findAll();
        assertThat(ficheList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Fiche in Elasticsearch
        verify(mockFicheSearchRepository, times(0)).save(fiche);
    }

    @Test
    @Transactional
    public void deleteFiche() throws Exception {
        // Initialize the database
        ficheRepository.saveAndFlush(fiche);

        int databaseSizeBeforeDelete = ficheRepository.findAll().size();

        // Delete the fiche
        restFicheMockMvc.perform(delete("/api/fiches/{id}", fiche.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fiche> ficheList = ficheRepository.findAll();
        assertThat(ficheList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Fiche in Elasticsearch
        verify(mockFicheSearchRepository, times(1)).deleteById(fiche.getId());
    }

    @Test
    @Transactional
    public void searchFiche() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ficheRepository.saveAndFlush(fiche);
        when(mockFicheSearchRepository.search(queryStringQuery("id:" + fiche.getId())))
            .thenReturn(Collections.singletonList(fiche));

        // Search the fiche
        restFicheMockMvc.perform(get("/api/_search/fiches?query=id:" + fiche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiche.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupeSanguin").value(hasItem(DEFAULT_GROUPE_SANGUIN)))
            .andExpect(jsonPath("$.[*].poids").value(hasItem(DEFAULT_POIDS.intValue())))
            .andExpect(jsonPath("$.[*].taille").value(hasItem(DEFAULT_TAILLE.intValue())));
    }
}
