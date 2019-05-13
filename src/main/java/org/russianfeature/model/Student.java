package org.russianfeature.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.*;
import java.io.Serializable;
//import javax.persistence.Access;
//import javax.persistence.AccessType;

@Entity
@Table(name = "student")
@Access(AccessType.PROPERTY)
public class Student implements Serializable {

    private IntegerProperty id;
    private StringProperty firstName;
    private StringProperty secondName;
    private StringProperty lastName;
    private StringProperty comment;
    private StringProperty createDate;
    private StringProperty birthDate;

    public Student() {
        this.id = new SimpleIntegerProperty();
        this.firstName = new SimpleStringProperty("");
        this.secondName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.comment = new SimpleStringProperty("");
        this.createDate = new SimpleStringProperty("");
        this.birthDate = new SimpleStringProperty("");
    }

    @Id
    //@Column(name = "id", unique = true, nullable = false)
    //@GeneratedValue(strategy = GenerationType.TABLE, generator="native")
    public Integer getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    @Column(name = "firstName")
    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    @Column(name = "secondName")
    public String getSecondName() {
        return secondName.get();
    }

    public StringProperty secondNameProperty() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName.set(secondName);
    }

    @Column(name = "lastName")
    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    @Column(name = "comment")
    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    @Column(name = "createDate")
    public String getCreateDate() {
        return createDate.get();
    }

    public StringProperty createDateProperty() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate.set(createDate);
    }

    @Column(name = "birthDate")
    public String getBirthDate() {
        return birthDate.get();
    }

    public StringProperty birthDateProperty() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate.set(birthDate);
    }

}



