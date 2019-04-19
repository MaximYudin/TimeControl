package com.hibernate.crud.operations;

import com.hibernate.crud.operations.dao.StudentDAO;
import com.hibernate.crud.operations.dao.TeacherDAO;
import com.hibernate.crud.operations.idao.IStudentDAO;
import com.hibernate.crud.operations.idao.ITeacherDAO;
import com.hibernate.crud.operations.idao.ITeacherManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.russianfeature.model.Student;
import org.russianfeature.model.StudentLoadInfo;
import org.russianfeature.model.Teacher;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TeacherManager implements ITeacherManager {
    private ITeacherDAO teacherDAO = new TeacherDAO();

    @Override
    public Teacher findByTeacherName(String name, String surname) {
        Teacher teacher = null;
        try {
            HibernateUtil.beginTransaction();
            teacher = teacherDAO.findByName(name, surname);
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            System.out.println("Query returned more than one results.");
        } catch (HibernateException ex) {
            System.out.println(ex);
        }
        return teacher;
    }

    @Override
    public void deleteTeacher(Teacher teacher) {
        try {
            HibernateUtil.beginTransaction();
            teacherDAO.delete(teacher);
            HibernateUtil.commitTransaction();
        } catch (HibernateException ex) {
            System.out.println(ex);
            HibernateUtil.rollbackTransactiom();
        }
    }

    @Override
    public List<Teacher> getAllTeacher() {
        List<Teacher> teacherList = new ArrayList<>();
        try {
            HibernateUtil.beginTransaction();
            teacherList = teacherDAO.findAll(Teacher.class);
            HibernateUtil.commitTransaction();
        } catch (HibernateException ex) {
            System.out.println(ex);
        }
        return teacherList;
    }

    @Override
    public Teacher findTeacherById(int id) {
        Teacher teacher = null;
        try {
            HibernateUtil.beginTransaction();
            teacher = teacherDAO.findByID(Teacher.class, id);
            HibernateUtil.commitTransaction();
        } catch (HibernateException ex) {
            System.out.println(ex);
        }
        return teacher;
    }

    @Override
    public void saveTeacher(Teacher teacher) {
        try {
            HibernateUtil.beginTransaction();
            teacherDAO.save(teacher);
            HibernateUtil.commitTransaction();
        } catch (HibernateException ex) {
            //System.out.println(ex);
            ex.printStackTrace();
            HibernateUtil.rollbackTransactiom();
        }
    }

    @Override
    public void createTeacher(Teacher teacher) {
        try {
            HibernateUtil.beginTransaction();
            teacherDAO.create(teacher);
            HibernateUtil.commitTransaction();
        } catch (HibernateException ex) {
            System.out.println(ex);
            HibernateUtil.rollbackTransactiom();
        }
    }

    @Override
    public List<Teacher> getDoubles(Map<String, String> params) {
        List<Teacher> teacherList = new ArrayList<>();
        try {
            HibernateUtil.beginTransaction();
            teacherList = teacherDAO.getDoubles(params);
            HibernateUtil.commitTransaction();
        } catch (HibernateException ex) {
            System.out.println(ex);
        }
        return teacherList;
    }

    /*
    public ObservableList<StudentLoadInfo> getStudentLoadList(List<StudentLoadInfo> studentInfoList) {
        Session session = HibernateUtil.beginTransaction();
        // Delete all rows from temp table
        Query queryDeleteAll = session.createSQLQuery("delete from student_tmp");
        queryDeleteAll.executeUpdate();

        // Insert rows into temp table
        Query queryInsert = session.createSQLQuery("INSERT INTO student_tmp " +
                "(firstName, secondName, lastName, birthDate) " +
                "VALUES (:val1, :val2, :val3, :val4)");
        for (StudentLoadInfo stInfo : studentInfoList) {
            queryInsert.setParameter("val1", stInfo.getFirstName());
            queryInsert.setParameter("val2", stInfo.getSecondName());
            queryInsert.setParameter("val3", stInfo.getLastName());
            queryInsert.setParameter("val4", stInfo.getBirthDate());
            queryInsert.executeUpdate();
        }
        HibernateUtil.commitTransaction();

        // Find dublicates
        session = HibernateUtil.beginTransaction();
        Query queryDublicates = session.createSQLQuery(
                "SELECT\n" +
                        "\tst.firstName, st.secondName, st.lastName, st.birthDate,\n" +
                        "\tCASE\n" +
                        "\t\tWHEN s.firstName is not null then 1\n" +
                        "\t\telse 0\n" +
                        "\tend as isDublicate\n" +
                        "from student_tmp as st\n" +
                        "left join student as s\n" +
                        "\ton st.firstName = s.firstName\n" +
                        "\tand st.secondName = s.secondName\n" +
                        "\tand st.lastName = s.lastName\n" +
                        "\tand st.birthDate = s.birthDate\n"
                //+"where s.firstName is null"
        );

        ObservableList<StudentLoadInfo> studentInfoObsList = FXCollections.observableArrayList();
        List<Object[]> rows = queryDublicates.list();
        HibernateUtil.commitTransaction();
        for (Object[] row : rows) {
            StudentLoadInfo stInfo = new StudentLoadInfo();
            stInfo.setFirstName(row[0].toString());
            stInfo.setSecondName(row[1].toString());
            stInfo.setLastName(row[2].toString());

            String birthDate = row[3].toString();
            DateFormat sourceFormat = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date date = sourceFormat.parse(birthDate);
                stInfo.setBirthDate(sourceFormat.format(date));
            } catch (ParseException e) {
                stInfo.setErrorText("Не верный формат даты рождения.");
                e.printStackTrace();
            }

            stInfo.setLoadFlag(true);
            if (row[4].toString().equals("1")) {
                stInfo.setErrorText("Найден дубль");
                stInfo.setDublicateFlag(true);
                stInfo.setLoadFlag(false);
            }

            studentInfoObsList.add(stInfo);
        }

        return studentInfoObsList;
    }
    */
}
