package org.russianfeature.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;

@Entity
@Table(name = "student")
@Access(AccessType.PROPERTY)
public class Teacher {
    private IntegerProperty id;
    private StringProperty firstName;
    private StringProperty secondName;
    private StringProperty lastName;
    private StringProperty comment;
    private StringProperty createDate;
    private StringProperty birthDate;
    private StringProperty startWorkDate;
    private StringProperty endWorkDate;
    private StringProperty changeDate;
    private StringProperty changeAuthorId;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
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

    @Column(name = "startWorkDate")
    public String getStartWorkDate() {
        return startWorkDate.get();
    }

    public StringProperty startWorkDateProperty() {
        return startWorkDate;
    }

    public void setStartWorkDate(String startWorkDate) {
        this.startWorkDate.set(startWorkDate);
    }

    @Column(name = "changeDate")
    public String getChangeDate() {
        return changeDate.get();
    }

    public StringProperty changeDateProperty() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate.set(changeDate);
    }

    @Column(name = "changeAuthorId")
    public String getChangeAuthorId() {
        return changeAuthorId.get();
    }

    public StringProperty changeAuthorIdProperty() {
        return changeAuthorId;
    }

    public void setChangeAuthorId(String changeAuthor) {
        this.changeAuthorId.set(changeAuthor);
    }

    @Column(name = "endWorkDate")
    public String getEndWorkDate() {
        return endWorkDate.get();
    }

    public StringProperty endWorkDateProperty() {
        return endWorkDate;
    }

    public void setEndWorkDate(String endWorkDate) {
        this.endWorkDate.set(endWorkDate);
    }
}
