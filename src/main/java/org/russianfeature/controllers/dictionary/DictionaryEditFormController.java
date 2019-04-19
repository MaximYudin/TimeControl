package org.russianfeature.controllers.dictionary;

import com.hibernate.crud.operations.StudentManager;
import com.utils.CommonUtil;
import com.utils.EnumAction;
import com.utils.EnumYesNo;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.russianfeature.Main;
import org.russianfeature.controllers.QuestionYeaNoController;
import org.russianfeature.model.Student;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DictionaryEditFormController<T> {

    private Main mainApp;
    private Stage stage;
    private T currentEntity;
    private EnumAction action;
    private ObservableList<Student> doublesStudentList = FXCollections.observableArrayList();
    private Class<T> typeParameterClass;
    private java.lang.reflect.Method method;
    private TableView<T> tableDoubles;
    private List<String> columnList = new ArrayList<>();

    @FXML
    private AnchorPane dictionaryMainEdit;

    @FXML
    void initialize() {

    }

    public void initForm() {
        drawFormElements();
    }

    private void drawFormElements() {

        VBox vbox = new VBox();
        drawFields(vbox);

        Button btnCheckDoblicate = new Button("Проверка на дубли");
        btnCheckDoblicate.setOnAction(event -> {
            StringBuilder msgError = new StringBuilder("");
            if (!isFormFieldsValueValid(msgError))
                return;

            //doublesStudentList = getDoublesStudentList();
            //showDoublesStudents();
        });

        vbox.getChildren().add(btnCheckDoblicate);
        drawTableView(vbox);
        dictionaryMainEdit.getChildren().addAll(vbox);
    }

    private void drawFields(VBox vbox) {
        GridPane myGP = new GridPane();
        myGP.setVgap(5);
        myGP.setHgap(5);
        myGP.minWidthProperty().bind(dictionaryMainEdit.widthProperty());
        myGP.setPadding(new Insets(0, 0, 0, 5));

        int index = 0;
        // Dynamic add text fields
        for (Field fld : typeParameterClass.getDeclaredFields()) {

            if (fld.getName().equals("id"))
                continue;

            String fieldName = fld.getName();
            Label lbl = new Label();
            lbl.setMinWidth(dictionaryMainEdit.getPrefWidth() * 0.3);
            lbl.setText(CommonUtil.getMapFieldName(fieldName));

            myGP.add(lbl, 0, index, 1, 1);

            if (!fieldName.contains("Date")) {
                // Add text fiels
                addTextField(myGP, fieldName, index);
            } else {
                // Add date picker field
                addDatePickerField(myGP, fieldName, index);
            }

            index++;

            // Set field values from DB
            if (currentEntity != null) {

                try {
                    //String fieldName = fld.getName().substring(0, 1).toUpperCase() + fld.getName().substring(1);
                    method = currentEntity.getClass().getMethod("get" + CommonUtil.convertFirstLetterToUpperCase(fieldName));
                    String fieldValue = (String) method.invoke(currentEntity);
                }
                catch (NoSuchMethodException exc) { exc.printStackTrace(); }
                catch (IllegalAccessException exc) { exc.printStackTrace(); }
                catch (InvocationTargetException exc) { exc.printStackTrace(); }

            }
        }
        vbox.getChildren().add(myGP);
    }

    private void drawTableView(VBox vbox) {
        tableDoubles = new TableView<>();
        for (Field fld : typeParameterClass.getDeclaredFields()) {

            if (fld.getType().equals(IntegerProperty.class)) {
                TableColumn<T, Integer> myColumn = new TableColumn<>();
                myColumn.setCellValueFactory(new PropertyValueFactory<T, Integer>(fld.getName()));
                myColumn.setId(fld.getName());
                tableDoubles.getColumns().add(myColumn);
            } else {
                TableColumn<T, String> myColumn = new TableColumn<>();
                myColumn.setCellValueFactory(new PropertyValueFactory<T, String>(fld.getName()));
                myColumn.setId(fld.getName());
                tableDoubles.getColumns().add(myColumn);
            }
            columnList.add(fld.getName());
        }
        vbox.getChildren().add(tableDoubles);
    }

    public void setTypeParameterClass(Class<T> clazz) {
        typeParameterClass = clazz;
    }

    public void setAction(EnumAction action) {
        this.action = action;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage stg) {
        stage = stg;
    }

    public void setEntity(T entity) {
        this.currentEntity = entity;
    }

    public T getEntity() {
        return currentEntity;
    }

    private void initializeDataPickerProperties(DatePicker fldBirthDate) {

        Locale.setDefault(new Locale("ru"));
        // Converter
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        fldBirthDate.setConverter(converter);
        fldBirthDate.setPromptText("Set date");

        // Create a day cell factory
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>()
        {
            public DateCell call(final DatePicker datePicker)
            {
                return new DateCell()
                {
                    @Override
                    public void updateItem(LocalDate item, boolean empty)
                    {
                        // Must call super
                        super.updateItem(item, empty);

                        // Show Weekends in blue color
                        DayOfWeek day = DayOfWeek.from(item);
                        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY)
                        {
                            this.setTextFill(Color.BLUE);
                        }

                        // Disable all future date cells
                        if (item.isAfter(LocalDate.now()))
                        {
                            this.setDisable(true);
                        }
                    }
                };
            }
        };
        // Set the day cell factory to the DatePicker
        fldBirthDate.setDayCellFactory(dayCellFactory);
        //fldDataPicker.setEditable(false);

    }

    private void addTextField(GridPane myGP, String fieldName, int index) {
        TextField dataField = new TextField();
        dataField.setId(fieldName);
        dataField.setMinWidth(dictionaryMainEdit.getPrefWidth() * 0.7);
        myGP.add(dataField, 1, index, 1, 1);
    }

    private void addDatePickerField(GridPane myGP, String fieldName, int index) {
        DatePicker dataField = new DatePicker();
        dataField.setId(fieldName);
        dataField.setMinWidth(dictionaryMainEdit.getPrefWidth() * 0.4);
        myGP.add(dataField, 1, index, 1, 1);
        initializeDataPickerProperties(dataField);

        dataField.setOnAction(event -> {
            CommonUtil.setStyleBackgroundColor(dataField, "white");
        });
        GridPane.setHalignment(dataField, HPos.RIGHT);
    }

    // Validation of form fields
    private boolean isFormFieldsValueValid(StringBuilder msgError) {

        /*
        String errorColor = "red";
        for (Field fld : typeParameterClass.getDeclaredFields()) {

            if (fld.getName().equals("id"))
                continue;

        }

        if (fldFirstName.getText().isEmpty()) {
            msgError.append("Не заполнена фамилия");
            CommonUtil.setStyleBackgroundColor(fldFirstName, errorColor);
        }
        if (fldSecondName.getText().isEmpty()) {
            msgError.append((msgError.toString().isEmpty() ? "" : ", ") + "Не заполнено имя");
            CommonUtil.setStyleBackgroundColor(fldSecondName, errorColor);
        }
        if (fldLastName.getText().isEmpty()) {
            msgError.append((msgError.toString().isEmpty() ? "" : ", ") + "Не заполнено отчество");
            CommonUtil.setStyleBackgroundColor(fldLastName, errorColor);
        }
        if (fldBirthDate.getValue() == null) {
            msgError.append((msgError.toString().isEmpty() ? "" : ", ") + "Не заполнено дата рождения");
            CommonUtil.setStyleBackgroundColor(fldBirthDate, errorColor);
        }

        return (msgError.toString().isEmpty() ? true : false);
        */
        return false;
    }
    /*

    // Set fields value on form
    public void setFieldValues() {
        fldID.setText(String.valueOf(student.getId()));
        fldFirstName.setText(student.getFirstName());
        fldSecondName.setText(student.getSecondName());
        fldLastName.setText(student.getLastName());
        taComment.setText(student.getComment());

        if (student.getBirthDate() != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                fldBirthDate.setValue(format.parse(student.getBirthDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnCancelOnClick(ActionEvent event) {
        stage.close();
    }

    @FXML
    void btnOKOnClick(ActionEvent event) {

        StringBuilder sbMsgText = new StringBuilder();
        if (!isFormFieldsValueValid(sbMsgText)) {
            CommonUtil.showNotificationWindow(mainApp, sbMsgText);
            return;
        }

        doublesStudentList = getDoublesStudentList();
        if (doublesStudentList.size() > 0) {
            String msgText = "Найдены дубли. Продолжить без сравнения?";
            CommonUtil.showYesNoQuestionWindow(mainApp, msgText);
            if (QuestionYeaNoController.answer == EnumYesNo.NO) {
                showDoublesStudents();
                return;
            }
        }
        if (action == EnumAction.UPDATE)
            updateStudent();
        else
            createStudent();

        stage.close();
    }



    @FXML
    void firstNameOnKeyPressed(KeyEvent event) {
        CommonUtil.setStyleBackgroundColor(fldFirstName, "white");
    }

    @FXML
    void secondNameOnKeyPressed(KeyEvent event) {
        CommonUtil.setStyleBackgroundColor(fldSecondName, "white");
    }

    @FXML
    void lastNameOnKeyPressed(KeyEvent event) {
        CommonUtil.setStyleBackgroundColor(fldLastName, "white");
    }

    @FXML
    void bdOnChange(InputMethodEvent event) {
        //CommonUtil.setStyleBackgroundColor(fldBirthDate, "white");
        //fldBirthDate.setStyle("-fx-control-inner-background: white");
    }

    @FXML
    void bdOnAction(ActionEvent event) {
        CommonUtil.setStyleBackgroundColor(fldBirthDate, "white");
        //fldBirthDate.setStyle("-fx-control-inner-background: white");
    }

    @FXML
    void firstNameOnEnter(ActionEvent event) {
        TextField textField = (TextField) event.getSource();
        setTextFileldCorrectValue(event, textField);
    }

    @FXML
    void lastNameOnEnter(ActionEvent event) {
        TextField textField = (TextField) event.getSource();
        setTextFileldCorrectValue(event, textField);
    }

    @FXML
    void secondNameOnEnter(ActionEvent event) {
        TextField textField = (TextField) event.getSource();
        setTextFileldCorrectValue(event, textField);
    }

    void updateStudent() {
        StudentManager studentManager = new StudentManager();
        setStudentProperty(student);
        studentManager.saveStudent(student);
    }

    @FXML
    void btnCheckDoublesOnClick(ActionEvent event) {
        StringBuilder msgError = new StringBuilder("");
        if (!isFormFieldsValueValid(msgError))
            return;

        doublesStudentList = getDoublesStudentList();
        showDoublesStudents();
    }



    void createStudent() {
        StudentManager studentManager = new StudentManager();
        Student newStudent = new Student();
        setStudentProperty(newStudent);
        studentManager.saveStudent(newStudent);
        student = newStudent;
    }

    // Set entity property value
    void setStudentProperty(Student entity) {
        entity.setFirstName(fldFirstName.getText());
        entity.setSecondName(fldSecondName.getText());
        entity.setLastName(fldLastName.getText());
        entity.setComment(taComment.getText());
        DateFormat sourceFormat = new SimpleDateFormat("dd.MM.yyyy");
        if (action == EnumAction.CREATE) {
            Date dt = new Date();
            String dateToDB = sourceFormat.format(dt);
            entity.setCreateDate(dateToDB);
        }

        if (fldBirthDate.getValue() != null) {
            try {
                Date date = sourceFormat.parse(fldBirthDate.getEditor().getText());
                entity.setBirthDate(sourceFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    void setTextFileldCorrectValue(ActionEvent event, TextField textField) {
        TextField firstName = (TextField) event.getSource();
        String text = firstName.getText();
        firstName.setText(CommonUtil.convertFirstLetterToUpperCase(text));
    }



    void showDoublesStudents() {

        //Binding model and view elements
        id.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
        secondName.setCellValueFactory(new PropertyValueFactory<Student, String>("secondName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
        createDate.setCellValueFactory(new PropertyValueFactory<Student, String>("createDate"));
        birthDate.setCellValueFactory(new PropertyValueFactory<Student, String>("birthDate"));
        comment.setCellValueFactory(new PropertyValueFactory<Student, String>("comment"));

        tableStudentDoubles.setItems(doublesStudentList);

    }

    private ObservableList<Student> getDoublesStudentList() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = java.sql.Date.valueOf(fldBirthDate.getValue().toString());

        Map<String, String> params = new HashMap<>();
        params.put("id", fldID.getText());
        params.put("firstName", fldFirstName.getText());
        params.put("secondName", fldSecondName.getText());
        params.put("lastName", fldLastName.getText());
        params.put("birthDate", format.format(date));
        params.put("checkId", (action == EnumAction.CREATE) ? "f" : "t");

        StudentManager studentManager = new StudentManager();
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        List<Student> stList = studentManager.getDoubles(params);

        for (Student student : stList) {
            studentList.add(student);
        }

        return studentList;
    }

    private void initializeDataPickerProperties() {

        Locale.setDefault(new Locale("ru"));
        // Converter
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        fldBirthDate.setConverter(converter);
        fldBirthDate.setPromptText("Set date");

        // Create a day cell factory
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>()
        {
            public DateCell call(final DatePicker datePicker)
            {
                return new DateCell()
                {
                    @Override
                    public void updateItem(LocalDate item, boolean empty)
                    {
                        // Must call super
                        super.updateItem(item, empty);

                        // Show Weekends in blue color
                        DayOfWeek day = DayOfWeek.from(item);
                        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY)
                        {
                            this.setTextFill(Color.BLUE);
                        }

                        // Disable all future date cells
                        if (item.isAfter(LocalDate.now()))
                        {
                            this.setDisable(true);
                        }
                    }
                };
            }
        };
        // Set the day cell factory to the DatePicker
        fldBirthDate.setDayCellFactory(dayCellFactory);
        //fldDataPicker.setEditable(false);

    }

    // Validation of form fields
    private boolean isFormFieldsValueValid(StringBuilder msgError) {

        String errorColor = "red";
        if (fldFirstName.getText().isEmpty()) {
            msgError.append("Не заполнена фамилия");
            CommonUtil.setStyleBackgroundColor(fldFirstName, errorColor);
        }
        if (fldSecondName.getText().isEmpty()) {
            msgError.append((msgError.toString().isEmpty() ? "" : ", ") + "Не заполнено имя");
            CommonUtil.setStyleBackgroundColor(fldSecondName, errorColor);
        }
        if (fldLastName.getText().isEmpty()) {
            msgError.append((msgError.toString().isEmpty() ? "" : ", ") + "Не заполнено отчество");
            CommonUtil.setStyleBackgroundColor(fldLastName, errorColor);
        }
        if (fldBirthDate.getValue() == null) {
            msgError.append((msgError.toString().isEmpty() ? "" : ", ") + "Не заполнено дата рождения");
            CommonUtil.setStyleBackgroundColor(fldBirthDate, errorColor);
        }

        return (msgError.toString().isEmpty() ? true : false);
    }
    */
}

