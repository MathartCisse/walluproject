package com.mycompany.myapp.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Fiche.class)
public abstract class Fiche_ {

	public static volatile SingularAttribute<Fiche, String> groupeSanguin;
	public static volatile SingularAttribute<Fiche, Long> taille;
	public static volatile SingularAttribute<Fiche, Long> poids;
	public static volatile SingularAttribute<Fiche, Long> id;

	public static final String GROUPE_SANGUIN = "groupeSanguin";
	public static final String TAILLE = "taille";
	public static final String POIDS = "poids";
	public static final String ID = "id";

}

