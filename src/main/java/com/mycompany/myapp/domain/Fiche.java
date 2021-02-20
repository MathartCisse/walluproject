package com.mycompany.myapp.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Fiche.
 */
@Entity
@Table(name = "fiche")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fiche")
public class Fiche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "groupe_sanguin")
    private String groupeSanguin;

    @Column(name = "poids")
    private Long poids;

    @Column(name = "taille")
    private Long taille;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupeSanguin() {
        return groupeSanguin;
    }

    public Fiche groupeSanguin(String groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
        return this;
    }

    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }

    public Long getPoids() {
        return poids;
    }

    public Fiche poids(Long poids) {
        this.poids = poids;
        return this;
    }

    public void setPoids(Long poids) {
        this.poids = poids;
    }

    public Long getTaille() {
        return taille;
    }

    public Fiche taille(Long taille) {
        this.taille = taille;
        return this;
    }

    public void setTaille(Long taille) {
        this.taille = taille;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fiche)) {
            return false;
        }
        return id != null && id.equals(((Fiche) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fiche{" +
            "id=" + getId() +
            ", groupeSanguin='" + getGroupeSanguin() + "'" +
            ", poids=" + getPoids() +
            ", taille=" + getTaille() +
            "}";
    }
}
