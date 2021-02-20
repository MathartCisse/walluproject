package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Fiche;
import com.mycompany.myapp.repository.FicheRepository;
import com.mycompany.myapp.repository.search.FicheSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Fiche}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FicheResource {

    private final Logger log = LoggerFactory.getLogger(FicheResource.class);

    private static final String ENTITY_NAME = "fiche";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FicheRepository ficheRepository;

    private final FicheSearchRepository ficheSearchRepository;

    public FicheResource(FicheRepository ficheRepository, FicheSearchRepository ficheSearchRepository) {
        this.ficheRepository = ficheRepository;
        this.ficheSearchRepository = ficheSearchRepository;
    }

    /**
     * {@code POST  /fiches} : Create a new fiche.
     *
     * @param fiche the fiche to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fiche, or with status {@code 400 (Bad Request)} if the fiche has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fiches")
    public ResponseEntity<Fiche> createFiche(@RequestBody Fiche fiche) throws URISyntaxException {
        log.debug("REST request to save Fiche : {}", fiche);
        if (fiche.getId() != null) {
            throw new BadRequestAlertException("A new fiche cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fiche result = ficheRepository.save(fiche);
        ficheSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fiches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fiches} : Updates an existing fiche.
     *
     * @param fiche the fiche to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiche,
     * or with status {@code 400 (Bad Request)} if the fiche is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fiche couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fiches")
    public ResponseEntity<Fiche> updateFiche(@RequestBody Fiche fiche) throws URISyntaxException {
        log.debug("REST request to update Fiche : {}", fiche);
        if (fiche.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Fiche result = ficheRepository.save(fiche);
        ficheSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fiche.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fiches} : get all the fiches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fiches in body.
     */
    @GetMapping("/fiches")
    public List<Fiche> getAllFiches() {
        log.debug("REST request to get all Fiches");
        return ficheRepository.findAll();
    }

    /**
     * {@code GET  /fiches/:id} : get the "id" fiche.
     *
     * @param id the id of the fiche to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fiche, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fiches/{id}")
    public ResponseEntity<Fiche> getFiche(@PathVariable Long id) {
        log.debug("REST request to get Fiche : {}", id);
        Optional<Fiche> fiche = ficheRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fiche);
    }

    /**
     * {@code DELETE  /fiches/:id} : delete the "id" fiche.
     *
     * @param id the id of the fiche to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fiches/{id}")
    public ResponseEntity<Void> deleteFiche(@PathVariable Long id) {
        log.debug("REST request to delete Fiche : {}", id);
        ficheRepository.deleteById(id);
        ficheSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/fiches?query=:query} : search for the fiche corresponding
     * to the query.
     *
     * @param query the query of the fiche search.
     * @return the result of the search.
     */
    @GetMapping("/_search/fiches")
    public List<Fiche> searchFiches(@RequestParam String query) {
        log.debug("REST request to search Fiches for query {}", query);
        return StreamSupport
            .stream(ficheSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
