package com.mycompany.myapp.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "patient")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The firstname attribute.
     */
    @ApiModelProperty(value = "The firstname attribute.")
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "age")
    private Long age;

    @Column(name = "salary")
    private Long salary;

    @Column(name = "id_card_number")
    private Long idCardNumber;

    @OneToOne
    @JoinColumn(unique = true)
    private Fiche patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Patient firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Patient lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Patient email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Patient phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getAge() {
        return age;
    }

    public Patient age(Long age) {
        this.age = age;
        return this;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getSalary() {
        return salary;
    }

    public Patient salary(Long salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getIdCardNumber() {
        return idCardNumber;
    }

    public Patient idCardNumber(Long idCardNumber) {
        this.idCardNumber = idCardNumber;
        return this;
    }

    public void setIdCardNumber(Long idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public Fiche getPatient() {
        return patient;
    }

    public Patient patient(Fiche fiche) {
        this.patient = fiche;
        return this;
    }

    public void setPatient(Fiche fiche) {
        this.patient = fiche;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", age=" + getAge() +
            ", salary=" + getSalary() +
            ", idCardNumber=" + getIdCardNumber() +
            "}";
    }
}
