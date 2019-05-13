package com.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.russianfeature.Main;
import org.russianfeature.controllers.NotificationFormController;
import org.russianfeature.controllers.QuestionYeaNoController;
import org.russianfeature.model.Student;
import org.russianfeature.model.Teacher;

import java.io.IOException;
import java.util.TreeMap;

public class CommonUtil {
    public static String convertFirstLetterToUpperCase(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    public static void showYesNoQuestionWindow(Main mainApp, String msgText) {

        try {

            Stage yesNoDialog = new Stage();
            //yesNoDialog.setTitle(msgText);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/QuestionYesNo.fxml"));

            AnchorPane studentAction = loader.load();

            Label lbl = (Label) studentAction.lookup("#msgLabel");
            if (lbl != null)
                lbl.setText(msgText);

            studentAction.setId("QuestionYesNo");

            // Bind controller and main app
            QuestionYeaNoController controller = loader.getController();
            controller.setStage(yesNoDialog);
            controller.setMainApp(mainApp);

            yesNoDialog.setScene(new Scene(studentAction));
            yesNoDialog.initOwner(mainApp.getPrimaryStage());
            yesNoDialog.initModality(Modality.APPLICATION_MODAL);
            yesNoDialog.showAndWait();

            //tableStudents.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showNotificationWindow(Main mainApp, StringBuilder msgText) {

        try {

            Stage notification = new Stage();
            //yesNoDialog.setTitle(msgText);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/NotificationForm.fxml"));

            AnchorPane studentAction = loader.load();

            Label lbl = (Label) studentAction.lookup("#msgLabel");
            if (lbl != null)
                lbl.setText(msgText.toString());

            studentAction.setId("NotificationForm");

            // Bind controller and main app
            NotificationFormController controller = loader.getController();
            controller.setStage(notification);
            controller.setMainApp(mainApp);

            notification.setScene(new Scene(studentAction));
            notification.initOwner(mainApp.getPrimaryStage());
            notification.initModality(Modality.APPLICATION_MODAL);
            notification.showAndWait();

            //tableStudents.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setStyleBackgroundColor(Node node, String color) {
        node.setStyle("-fx-background-color: " + color);
    }

    public static String getMapFieldName(String fieldName) {
        TreeMap<String, String> mapFields = Config.getFieldMapName();
        String correctName = mapFields.get(fieldName);

        return (correctName == null ? fieldName : correctName);
    }

    public static Boolean isFieldValueMustBeNonEmpty(String fieldName) {
        TreeMap<String, String> fields = Config.getFieldNonEmpty();

        return (fields.get(fieldName) == null ? false : true);
    }

    public static Boolean isFieldVisible(String fieldName) {
        TreeMap<String, String> fields = Config.getVisibleFields();

        return (fields.get(fieldName) == null ? false : true);
    }

    public static Boolean isLoadFromExcelButtonHandled(Class clazz) {
        if (clazz.equals(Student.class)
            || clazz.equals(Teacher.class))
            return true;
        return false;
    }
}
