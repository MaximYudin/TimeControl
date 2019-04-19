package com.hibernate.crud.operations.idao;

import org.russianfeature.model.Student;
import org.russianfeature.model.Teacher;

import java.util.List;
import java.util.Map;

public interface ITeacherManager {

    Teacher findByTeacherName(String name, String surname);

    void deleteTeacher(Teacher student);

    List<Teacher> getAllTeacher();

    Teacher findTeacherById(int id);

    void saveTeacher(Teacher student);

    void createTeacher(Teacher student);

    List<Teacher> getDoubles(Map<String, String> params);

}
