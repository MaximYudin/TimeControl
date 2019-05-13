package org.russianfeature.controllers.dictionary;

import com.hibernate.crud.operations.GroupTypeManager;
import com.hibernate.crud.operations.StudentManager;
import com.hibernate.crud.operations.TeacherManager;
import com.utils.CommonUtil;
import com.utils.EnumAction;
import com.utils.EnumYesNo;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.russianfeature.Main;
import org.russianfeature.controllers.QuestionYeaNoController;
import org.russianfeature.model.GroupType;
import org.russianfeature.model.Student;
import org.russianfeature.model.Teacher;

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
    private ObservableList<T> doublesList = FXCollections.observableArrayList();
    private Class<T> typeParameterClass;
    private java.lang.reflect.Method method;
    private TableView<T> tableDoubles;
    private List<String> columnList = new ArrayList<>();
    private T entity;

    @FXML
    private AnchorPane dictionaryMainEdit;

    @FXML
    void initialize() {
        //dictionaryMainEdit.setPrefHeight(300);
        //dictionaryMainEdit.setPrefWidth(500);
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

    public void initForm() {
        drawFormElements();
    }

    // Draw main form elements dinamyc
    private void drawFormElements() {

        VBox vbox = new VBox();
        drawFields(vbox);

        Button btnCheckDublicate = new Button("Проверка на дубли");
        btnCheckDublicate.setOnAction(event -> {
            StringBuilder msgError = new StringBuilder("");
            if (!isFormFieldsValueValid(msgError)) {
                CommonUtil.showNotificationWindow(mainApp, msgError);
                return;
            }

            doublesList = getDoublesList();
            tableDoubles.setItems(doublesList);
        });

        vbox.getChildren().add(btnCheckDublicate);
        drawTableView(vbox);

        HBox hBoxBtn = new HBox();

        Button btnOK = new Button("Сохранить");
        btnOK.setOnAction(event -> {
            saveEntity();
        });

        Button btnCancel = new Button("Отмена");
        btnCancel.setOnAction(event -> {
            stage.close();
        });

        hBoxBtn.getChildren().addAll(btnOK, btnCancel);
        vbox.getChildren().add(hBoxBtn);
        vbox.setStyle("-fx-border-color: red");

        AnchorPane.setBottomAnchor(vbox, 0.0);
        AnchorPane.setTopAnchor(vbox, 0.0);

        //VBox.setVgrow(btnOK, Priority.ALWAYS);

        dictionaryMainEdit.getChildren().add(vbox);
        dictionaryMainEdit.setMaxHeight(vbox.getPrefHeight());

        /*dictionaryMainEdit.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0,
                                Number arg1, Number arg2) {
                tableDoubles.setPrefHeight(arg2.doubleValue()/2);

            }
        });*/
    }

    // Draw text fields elements
    private void drawFields(VBox vbox) {
        GridPane myGP = new GridPane();
        myGP.setVgap(5);
        myGP.setHgap(5);
        myGP.minWidthProperty().bind(dictionaryMainEdit.widthProperty());
        myGP.setPadding(new Insets(0, 0, 0, 5));

        int index = 0;
        // Dynamic add text fields
        for (Field fld : typeParameterClass.getDeclaredFields()) {

            String fieldName = fld.getName();
            if (!CommonUtil.isFieldVisible(fieldName))
                continue;

            Label lbl = new Label(CommonUtil.getMapFieldName(fieldName));
            lbl.setMinWidth(dictionaryMainEdit.getPrefWidth() * 0.3);
            //lbl.setText(CommonUtil.getMapFieldName(fieldName));

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

                Node node = myGP.lookup("#" + fieldName);
                if (node == null)
                    continue;

                try {
                    //String fieldName = fld.getName().substring(0, 1).toUpperCase() + fld.getName().substring(1);
                    method = currentEntity.getClass().getMethod("get" + CommonUtil.convertFirstLetterToUpperCase(fieldName));
                    String fieldValue = (String) method.invoke(currentEntity);

                    if (!fieldName.contains("Date"))
                        ((TextField) node).setText(fieldValue);
                    else {
                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                        if (!fieldValue.isEmpty())
                            ((DatePicker) node).setValue(format.parse(fieldValue).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    }

                }
                catch (NoSuchMethodException exc) { exc.printStackTrace(); }
                catch (IllegalAccessException exc) { exc.printStackTrace(); }
                catch (InvocationTargetException exc) { exc.printStackTrace(); }
                catch (ParseException exc) { exc.printStackTrace(); }

            }
        }
        vbox.getChildren().add(myGP);

    }

    private void drawTableView(VBox vbox) {
        tableDoubles = new TableView<>();
        //tableDoubles.setMaxHeight(200);
        for (Field fld : typeParameterClass.getDeclaredFields()) {

            String fieldName = fld.getName();
            if (fld.getType().equals(IntegerProperty.class)) {
                TableColumn<T, Integer> myColumn = new TableColumn<>(CommonUtil.getMapFieldName(fieldName));
                myColumn.setCellValueFactory(new PropertyValueFactory<T, Integer>(fieldName));
                myColumn.setId("tc_" + fieldName);
                tableDoubles.getColumns().add(myColumn);
            } else {
                TableColumn<T, String> myColumn = new TableColumn<>(CommonUtil.getMapFieldName(fieldName));
                myColumn.setCellValueFactory(new PropertyValueFactory<T, String>(fieldName));
                myColumn.setId("tc_" + fieldName);
                tableDoubles.getColumns().add(myColumn);
            }
            columnList.add(fld.getName());
        }
        vbox.getChildren().add(tableDoubles);
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

        String errorColor = "red";
        String goodColor = "white";
        for (Field fld : typeParameterClass.getDeclaredFields()) {

            String fieldName = fld.getName();
            if (fieldName.equals("id"))
                continue;

            if (!CommonUtil.isFieldValueMustBeNonEmpty(fieldName))
                continue;

            Node node = dictionaryMainEdit.lookup("#" + fieldName);
            if (node == null)
                continue;

            boolean isValidField = true;
            if (fieldName.toLowerCase().contains("date")) {
                if (((DatePicker) node).getValue() == null)
                    isValidField = false;
            } else {
                if (((TextField) node).getText().isEmpty())
                    isValidField = false;
            }

            if (!isValidField) {
                msgError.append((msgError.toString().isEmpty() ? "Не заполнены поля: " : ", ") +
                        CommonUtil.getMapFieldName(fieldName));
                CommonUtil.setStyleBackgroundColor(node, errorColor);
            } else
                CommonUtil.setStyleBackgroundColor(node, goodColor);
        }
        return (msgError.toString().isEmpty() ? true : false);
    }

    private ObservableList<T> getDoublesList() {
        if (typeParameterClass.equals(Student.class))
            return getDoublesStudentList();
        if (typeParameterClass.equals(Teacher.class))
            return getDoublesTeacherList();
        if (typeParameterClass.equals(GroupType.class))
            return getDoublesGroupTypeList();

        return null;
    }

    private ObservableList<T> getDoublesStudentList() {


        Node birthDate = dictionaryMainEdit.lookup("#birthDate");
        if (birthDate == null)
            return null;

        Node firstName = dictionaryMainEdit.lookup("#firstName");
        if (firstName == null)
            return null;

        Node secondName = dictionaryMainEdit.lookup("#secondName");
        if (secondName == null)
            return null;

        Node lastName = dictionaryMainEdit.lookup("#lastName");
        if (lastName == null)
            return null;


        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date date = java.sql.Date.valueOf(((DatePicker) birthDate).getValue().toString());

        Map<String, String> params = new HashMap<>();
        /*
        if (action == EnumAction.UPDATE)
            params.put("id", ((Student) currentEntity).getId().toString());
        else
            params.put("id", "0");
        params.put("firstName", ((Student) currentEntity).getFirstName());
        params.put("secondName", ((Student) currentEntity).getSecondName());
        params.put("lastName", ((Student) currentEntity).getLastName());
        params.put("birthDate", ((Student) currentEntity).getBirthDate());
        params.put("checkId", (action == EnumAction.CREATE) ? "f" : "t");
        */

        if (action == EnumAction.UPDATE)
            params.put("id", ((Student) currentEntity).getId().toString());
        else
            params.put("id", "0");
        //params.put("id", ((TextField) id).getText());
        params.put("firstName", ((TextField) firstName).getText());
        params.put("secondName", ((TextField) secondName).getText());
        params.put("lastName", ((TextField) lastName).getText());
        params.put("birthDate", format.format(date));
        //params.put("checkId", (action == EnumAction.CREATE) ? "f" : "t");


        StudentManager studentManager = new StudentManager();
        ObservableList<T> studentList = FXCollections.observableArrayList();
        List<T> stList = (List<T>) studentManager.getDoubles(params);

        for (T student : stList) {
            studentList.add(student);
        }

        return studentList;
    }

    private ObservableList<T> getDoublesTeacherList() {

        Node birthDate = dictionaryMainEdit.lookup("#birthDate");
        if (birthDate == null)
            return null;

        Node firstName = dictionaryMainEdit.lookup("#firstName");
        if (firstName == null)
            return null;

        Node secondName = dictionaryMainEdit.lookup("#secondName");
        if (secondName == null)
            return null;

        Node lastName = dictionaryMainEdit.lookup("#lastName");
        if (lastName == null)
            return null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = java.sql.Date.valueOf(((DatePicker) birthDate).getValue().toString());

        Map<String, String> params = new HashMap<>();
        params.put("id", (action == EnumAction.UPDATE) ? ((Teacher) currentEntity).getId().toString() : "0");

        params.put("firstName", ((TextField) firstName).getText());
        params.put("secondName", ((TextField) secondName).getText());
        params.put("lastName", ((TextField) lastName).getText());
        params.put("birthDate", format.format(date));
        params.put("checkId", (action == EnumAction.CREATE) ? "f" : "t");

        TeacherManager teacherManager = new TeacherManager();
        ObservableList<T> teacherList = FXCollections.observableArrayList();
        List<T> tList = (List<T>) teacherManager.getDoubles(params);

        for (T teacher : tList) {
            teacherList.add(teacher);
        }

        return teacherList;
    }

    private ObservableList<T> getDoublesGroupTypeList() {

        Node groupKindName = dictionaryMainEdit.lookup("#groupKindName");
        if (groupKindName == null)
            return null;

        Node groupTypeName = dictionaryMainEdit.lookup("#groupTypeName");
        if (groupTypeName == null)
            return null;

        Map<String, String> params = new HashMap<>();

        if (action == EnumAction.UPDATE)
            params.put("id", ((GroupType) currentEntity).getId().toString());
        else
            params.put("id", "0");

        params.put("groupKindName", ((TextField) groupKindName).getText());
        params.put("groupTypeName", ((TextField) groupTypeName).getText());

        GroupTypeManager gtm = new GroupTypeManager();
        ObservableList<T> gtoList = FXCollections.observableArrayList();
        List<T> gtList = (List<T>) gtm.getDoubles(params);

        for (T gt : gtList) {
            gtoList.add(gt);
        }

        return gtoList;
    }

    void saveEntity() {

        StringBuilder sbMsgText = new StringBuilder();
        if (!isFormFieldsValueValid(sbMsgText)) {
            CommonUtil.showNotificationWindow(mainApp, sbMsgText);
            return;
        }

        doublesList = getDoublesList();
        boolean hasDoubles = (doublesList.size() > 0);
        if (hasDoubles) {
            if (typeParameterClass.equals(GroupType.class)
                    && action.equals(EnumAction.CREATE)) {
                StringBuilder msgText = new StringBuilder("Найдены дубли. Ошибка.");
                CommonUtil.showNotificationWindow(mainApp, msgText);
                return;
            }

            String msgText = "Найдены дубли. Продолжить без сравнения?";
            CommonUtil.showYesNoQuestionWindow(mainApp, msgText);
            if (QuestionYeaNoController.answer == EnumYesNo.NO) {
                tableDoubles.setItems(doublesList);
                return;
            }

            Node comment = dictionaryMainEdit.lookup("#comment");
            if (comment != null)
                if (((TextField) comment).getText().equals("")) {
                    sbMsgText.delete(0, sbMsgText.length());
                    sbMsgText.append("Для записи дублированной информации нужно заполнить комментарий.");
                    CommonUtil.showNotificationWindow(mainApp, sbMsgText);
                    return;
                }
        }

        if (action == EnumAction.UPDATE)
            updateEntity();
        else
            createEtnity();

        stage.close();
    }

    private void saveStudentEntity() {
        StringBuilder sbMsgText = new StringBuilder();
        if (!isFormFieldsValueValid(sbMsgText)) {
            CommonUtil.showNotificationWindow(mainApp, sbMsgText);
            return;
        }

        doublesList = getDoublesList();
        if (doublesList.size() > 0) {
            String msgText = "Найдены дубли. Продолжить без сравнения?";
            CommonUtil.showYesNoQuestionWindow(mainApp, msgText);
            if (QuestionYeaNoController.answer == EnumYesNo.NO) {
                tableDoubles.setItems(doublesList);
                return;
            }
        }
        if (action == EnumAction.UPDATE)
            updateStudent();
        else
            createStudent();
    }

    private void updateEntity() {
        if (typeParameterClass.equals(Student.class))
            updateStudent();
        else if (typeParameterClass.equals(Teacher.class))
            updateTeacher();
        else if (typeParameterClass.equals(GroupType.class))
            updateGroupType();
    }

    private void createEtnity() {
        if (typeParameterClass.equals(Student.class))
            createStudent();
        else if (typeParameterClass.equals(Teacher.class))
            createTeacher();
        else if (typeParameterClass.equals(GroupType.class))
            createGroupType();
    }

    void updateStudent() {
        StudentManager studentManager = new StudentManager();
        setEntityProperty((T) entity);
        //setStudentProperty((Student) entity);
        studentManager.saveStudent((Student) entity);
    }

    void updateTeacher() {
        TeacherManager teacherManager = new TeacherManager();
        setEntityProperty((T) currentEntity);
        teacherManager.saveTeacher((Teacher) currentEntity);
    }

    void updateGroupType() {
        GroupTypeManager gtManager = new GroupTypeManager();
        setEntityProperty((T) currentEntity);
        gtManager.saveGroupType((GroupType) currentEntity);
    }

    void createStudent() {
        StudentManager studentManager = new StudentManager();

        Student newStudent = new Student();
        setEntityProperty((T) newStudent);
        //setStudentProperty(newStudent);
        studentManager.saveStudent(newStudent);
        entity = (T) newStudent;
    }

    void createTeacher() {
        TeacherManager teacherManager = new TeacherManager();

        Teacher newTeacher = new Teacher();
        setEntityProperty((T) newTeacher);
        //setStudentProperty(newStudent);
        teacherManager.saveTeacher(newTeacher);
        entity = (T) newTeacher;
    }

    void createGroupType() {
        GroupTypeManager gtManager = new GroupTypeManager();

        GroupType newGT = new GroupType();
        setEntityProperty((T) newGT);
        //setStudentProperty(newStudent);
        gtManager.saveGroupType(newGT);
        entity = (T) newGT;
    }

    void setEntityProperty(T entity) {

        DateFormat sourceFormat = new SimpleDateFormat("dd.MM.yyyy");

        Class[] args = new Class[1];
        args[0] = String.class;
        //DateFormat sourceFormat = new SimpleDateFormat("dd.MM.yyyy");
        for (Field fld : typeParameterClass.getDeclaredFields()) {

            String fieldName = fld.getName();
            if (!CommonUtil.isFieldVisible(fieldName))
                continue;

            Node node = dictionaryMainEdit.lookup("#" + fieldName);
            if (node == null)
                continue;

            if (fieldName.toLowerCase().contains("date")) {
                if (((DatePicker) node).getValue() != null) {
                    try {
                        //DateFormat sourceFormat = new SimpleDateFormat("dd.MM.yyyy");
                        Date date = sourceFormat.parse(((DatePicker) node).getEditor().getText());
                        //entity.setBirthDate(sourceFormat.format(date));
                        method = entity.getClass().getMethod("set" + CommonUtil.convertFirstLetterToUpperCase(fieldName), args);
                        method.invoke(entity, sourceFormat.format(date));
                    }
                    catch (ParseException e) {e.printStackTrace();}
                    catch (NoSuchMethodException exc) { exc.printStackTrace(); }
                    catch (IllegalAccessException exc) { exc.printStackTrace(); }
                    catch (InvocationTargetException exc) { exc.printStackTrace(); }
                }
            } else {

                    try {
                        method = entity.getClass().getMethod("set" + CommonUtil.convertFirstLetterToUpperCase(fieldName), args);
                        method.invoke(entity, ((TextField) node).getText());
                    }
                    catch (NoSuchMethodException exc) { exc.printStackTrace(); }
                    catch (IllegalAccessException exc) { exc.printStackTrace(); }
                    catch (InvocationTargetException exc) { exc.printStackTrace(); }
            }
        }

        //DateFormat sourceFormat = new SimpleDateFormat("dd.MM.yyyy");
        if (action == EnumAction.CREATE) {
            Date dt = new Date();
            String dateToDB = sourceFormat.format(dt);
            try {
                method = entity.getClass().getMethod("setCreateDate", args);
                method.invoke(entity, dateToDB);
            }
            catch (NoSuchMethodException exc) { exc.printStackTrace(); }
            catch (IllegalAccessException exc) { exc.printStackTrace(); }
            catch (InvocationTargetException exc) { exc.printStackTrace(); }

            try {
                method = entity.getClass().getMethod("setLastEditDate", args);
                method.invoke(entity, dateToDB);
            }
            catch (NoSuchMethodException exc) { exc.printStackTrace(); }
            catch (IllegalAccessException exc) { exc.printStackTrace(); }
            catch (InvocationTargetException exc) { exc.printStackTrace(); }
        }
    }

    private void initializeDataPickerProperties1(DatePicker field) {

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
        field.setConverter(converter);
        field.setPromptText("Set date");

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
        field.setDayCellFactory(dayCellFactory);
        //fldDataPicker.setEditable(false);

    }

}

