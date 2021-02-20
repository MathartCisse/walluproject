package com.mycompany.myapp.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Patient.class)
public abstract class Patient_ {

	public static volatile SingularAttribute<Patient, String> firstName;
	public static volatile SingularAttribute<Patient, String> lastName;
	public static volatile SingularAttribute<Patient, String> phoneNumber;
	public static volatile SingularAttribute<Patient, Fiche> patient;
	public static volatile SingularAttribute<Patient, Long> idCardNumber;
	public static volatile SingularAttribute<Patient, Long> id;
	public static volatile SingularAttribute<Patient, Long> salary;
	public static volatile SingularAttribute<Patient, String> email;
	public static volatile SingularAttribute<Patient, Long> age;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String PATIENT = "patient";
	public static final String ID_CARD_NUMBER = "idCardNumber";
	public static final String ID = "id";
	public static final String SALARY = "salary";
	public static final String EMAIL = "email";
	public static final String AGE = "age";

}

