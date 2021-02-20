package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Fiche;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Fiche entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FicheRepository extends JpaRepository<Fiche, Long> {
}
