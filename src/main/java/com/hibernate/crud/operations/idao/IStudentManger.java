package com.hibernate.crud.operations.idao;

import org.russianfeature.model.Student;

import java.util.List;
import java.util.Map;

public interface IStudentManger {

    Student findByStudentName(String name, String surname);

    void deleteStudent(Student student);

    List<Student> getAllStudent();

    Student findStudentById(int id);

    void saveStudent(Student student);

    void createStudent(Student student);

    List<Student> getDoubles(Map<String, String> params);

}
