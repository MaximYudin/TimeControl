package org.russianfeature.controllers;

import com.hibernate.crud.operations.HibernateUtil;
import com.hibernate.crud.operations.StudentManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.russianfeature.Main;
import org.russianfeature.model.Student;
import org.russianfeature.model.StudentLoadInfo;

import java.util.List;

public class PreloadFormController {

    private Stage stage;
    private Main mainApp;
    private List<StudentLoadInfo> dataList;
    private AnchorPane pane;

    @FXML
    void initialize() {
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage stg) {
        stage = stg;
    }

    public void setDataList(List<StudentLoadInfo> dtList) {
        dataList = dtList;
    }

    public void setMainPane(AnchorPane pane) {
        this.pane = pane;
    }

    public void setElementFormProperty() {

        StudentManager studentManager = new StudentManager();
        ObservableList<StudentLoadInfo> studentInfoObsList = studentManager.getStudentLoadList(dataList);

        TableView<StudentLoadInfo> tblStudentView = new TableView<>();
        tblStudentView.setEditable(true);

        TableColumn<StudentLoadInfo, Boolean> loadFlagCol = new TableColumn<>("loadFlag");
        loadFlagCol.setCellValueFactory(new PropertyValueFactory<StudentLoadInfo, Boolean>("loadFlag"));
        loadFlagCol.setCellFactory( tc -> new CheckBoxTableCell<>());
        tblStudentView.getColumns().add(loadFlagCol);

        TableColumn<StudentLoadInfo, String> firstNameCol = new TableColumn<>("First name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<StudentLoadInfo, String>("firstName"));
        tblStudentView.getColumns().add(firstNameCol);

        TableColumn<StudentLoadInfo, String> secondNameCol = new TableColumn<>("Second name");
        secondNameCol.setCellValueFactory(new PropertyValueFactory<StudentLoadInfo, String>("secondName"));
        tblStudentView.getColumns().add(secondNameCol);

        TableColumn<StudentLoadInfo, String> lastNameCol = new TableColumn<>("Last name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<StudentLoadInfo, String>("lastName"));
        tblStudentView.getColumns().add(lastNameCol);

        TableColumn<StudentLoadInfo, String> birthDateCol = new TableColumn<>("Birth date");
        birthDateCol.setCellValueFactory(new PropertyValueFactory<StudentLoadInfo, String>("birthDate"));
        tblStudentView.getColumns().add(birthDateCol);

        TableColumn<StudentLoadInfo, String> errorTextCol = new TableColumn<>("Error text");
        errorTextCol.setCellValueFactory(new PropertyValueFactory<StudentLoadInfo, String>("errorText"));
        tblStudentView.getColumns().add(errorTextCol);

        AnchorPane.setLeftAnchor(tblStudentView, 0.0);
        AnchorPane.setRightAnchor(tblStudentView, 0.0);
        AnchorPane.setTopAnchor(tblStudentView, 0.0);
        AnchorPane.setBottomAnchor(tblStudentView, 0.0);

        tblStudentView.setItems(studentInfoObsList);
        pane.getChildren().add(tblStudentView);
    }


}
