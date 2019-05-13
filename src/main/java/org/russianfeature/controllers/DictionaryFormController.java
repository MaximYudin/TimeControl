package org.russianfeature.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import com.utils.Config;
import javafx.scene.text.Font;
import org.russianfeature.Main;
import org.russianfeature.model.GroupType;
import org.russianfeature.model.Student;
import org.russianfeature.model.Teacher;

import java.net.URL;
import java.util.ResourceBundle;

public class DictionaryFormController {

    private Main mainApp;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane dictionaryWorkPlace;

    @FXML
    private Button btnStudents;

    @FXML
    private Button btnTeachers;

    @FXML
    private Button btnLessons;

    @FXML
    private Button btnGroups;

    @FXML
    private Button btnGroupType;

    @FXML
    void btnStudentOnClick(ActionEvent event) {
        mainApp.removeWorkPlace("dictionaryStudentsMain");

        //mainApp.showDictionaryStudentsMain();
        mainApp.showDictionaryMain(Student.class);
    }

    @FXML
    void btnTeacherOnClick(ActionEvent event) {
        mainApp.removeWorkPlace("dictionaryTeacherMain");

        mainApp.showDictionaryMain(Teacher.class);
    }

    @FXML
    void btnGroupTypeOnClick(ActionEvent event) {
        mainApp.removeWorkPlace("dictionaryGroupTypeMain");

        mainApp.showDictionaryMain(GroupType.class);
    }

    @FXML
    void initialize() {

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setPlaceProperty() {
        AnchorPane.setBottomAnchor(dictionaryWorkPlace, (double) Config.TOP_BOTTOM_PANE_HEIGHT);
        AnchorPane.setTopAnchor(dictionaryWorkPlace, (double) Config.TOP_BOTTOM_PANE_HEIGHT);
        AnchorPane.setLeftAnchor(dictionaryWorkPlace, (double) Config.BTN_MENU_WIDTH + 20);
        AnchorPane.setRightAnchor(dictionaryWorkPlace, 0.0);

        btnGroups.setPrefSize(Config.DICTIONARY_MENU_WIDTH, Config.DICTIONARY_MENU_HEIGHT);
        btnStudents.setPrefSize(Config.DICTIONARY_MENU_WIDTH, Config.DICTIONARY_MENU_HEIGHT);
        btnTeachers.setPrefSize(Config.DICTIONARY_MENU_WIDTH, Config.DICTIONARY_MENU_HEIGHT);
        btnLessons.setPrefSize(Config.DICTIONARY_MENU_WIDTH, Config.DICTIONARY_MENU_HEIGHT);
        btnGroupType.setPrefSize(Config.DICTIONARY_MENU_WIDTH, Config.DICTIONARY_MENU_HEIGHT);

        btnGroups.setFont(new Font("System", Config.DICTIONARY_MENU_FONT_SIZE));
        btnStudents.setFont(new Font("System", Config.DICTIONARY_MENU_FONT_SIZE));
        btnTeachers.setFont(new Font("System", Config.DICTIONARY_MENU_FONT_SIZE));
        btnLessons.setFont(new Font("System", Config.DICTIONARY_MENU_FONT_SIZE));
        btnGroupType.setFont(new Font("System", Config.DICTIONARY_MENU_FONT_SIZE));
        //bindLayoutsBlocks();

    }


}
