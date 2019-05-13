package org.russianfeature.controllers.dictionary;

import com.hibernate.crud.operations.GroupTypeManager;
import com.hibernate.crud.operations.StudentManager;
import com.hibernate.crud.operations.TeacherManager;
import com.utils.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.russianfeature.Main;
import org.russianfeature.controllers.PreloadFormController;
import org.russianfeature.controllers.QuestionYeaNoController;
import org.russianfeature.model.GroupType;
import org.russianfeature.model.Student;
import org.russianfeature.model.StudentLoadInfo;
import org.russianfeature.model.Teacher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DictionaryListFormController<T> {

    private Main mainApp;
    private ObservableList<T> dataList = FXCollections.observableArrayList();
    private T currentEntity;
    private Stage currentStage;
    private Class<T> typeParameterClass;
    private List<String> columnList = new ArrayList<>();

    @FXML
    private AnchorPane dictionaryMain;

    @FXML
    private TableView<T> mainTable;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnLoadFromExcel;

    @FXML
    private TableColumn<T, String> col1;

    @FXML
    void btnAddOnClick(ActionEvent event) {

        showEditCreateWindow(EnumAction.CREATE);
        loadDataInTableView();

    }

    @FXML
    void btnDeleteOnClick(ActionEvent event) {

        ObservableList<T> selectedEntity = mainTable.getSelectionModel().getSelectedItems();
        if (selectedEntity.size() == 0) return;

        StringBuilder msgText = new StringBuilder();
        if (selectedEntity.size() > 1) {
            msgText.append("Вы действительно хотите удалить записи?");
        } else {
            msgText.append("Вы действительно хотите удалить запись?");
        }

        CommonUtil.showYesNoQuestionWindow(mainApp, msgText.toString());
        if (QuestionYeaNoController.answer == EnumYesNo.NO)
            return;

        deleteEntities(selectedEntity);

        /*
        // Check relationships
        StudentManager studentManager = new StudentManager();
        selectedEntity.forEach((student) -> {
            studentManager.deleteStudent(student);
        });
        */

        loadDataInTableView();

    }

    @FXML
    void btnEditOnClick(ActionEvent event) {
        showEditCreateWindow(EnumAction.UPDATE);
    }

    @FXML
    void btnLoadFromExcelOnClick(ActionEvent event) {

        if (!CommonUtil.isLoadFromExcelButtonHandled(typeParameterClass))
            return;

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Выберите файл для загрузки");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX", "*.xlsx"),
                new FileChooser.ExtensionFilter("XLS", "*.xls")
        );

        File file = fileChooser.showOpenDialog(currentStage);
        if (file != null) {
            String msgText = "Вы действительно хотите выполнить загрузку?";
            CommonUtil.showYesNoQuestionWindow(mainApp, msgText);
            if (QuestionYeaNoController.answer == EnumYesNo.NO)
                return;

            //List<T> entityList = FXCollections.observableArrayList();
            List<T> entityList = getEntityListFromExcel(file);

            showPreloadForm(entityList);
        }

        System.gc();

    }

    @FXML
    void initialize() {

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setTypeParameterClass(Class<T> clazz) {
        typeParameterClass = clazz;
    }

    public void setPlaceProperty() {
        AnchorPane.setBottomAnchor(dictionaryMain, (double) Config.TOP_BOTTOM_PANE_HEIGHT);
        AnchorPane.setTopAnchor(dictionaryMain, (double) Config.TOP_BOTTOM_PANE_HEIGHT);
        AnchorPane.setLeftAnchor(dictionaryMain, (double) Config.BTN_MENU_WIDTH + 20);
        AnchorPane.setRightAnchor(dictionaryMain, 0.0);

        //bindLayoutsBlocks();

    }

    void showDicList() {

        //Binding model and view elements

            for (Field fld : typeParameterClass.getDeclaredFields()) {

                String fieldName = fld.getName();
                if (!CommonUtil.isFieldVisible(fieldName))
                    continue;

                if (fieldName.equals(IntegerProperty.class)) {
                    TableColumn<T, Integer> myColumn = new TableColumn<>(CommonUtil.getMapFieldName(fieldName));
                    myColumn.setCellValueFactory(new PropertyValueFactory<T, Integer>(fieldName));
                    myColumn.setId(fieldName);
                    mainTable.getColumns().add(myColumn);
                } else {
                    TableColumn<T, String> myColumn = new TableColumn<>(CommonUtil.getMapFieldName(fieldName));
                    myColumn.setCellValueFactory(new PropertyValueFactory<T, String>(fieldName));
                    myColumn.setId(fieldName);
                    mainTable.getColumns().add(myColumn);
                }
                columnList.add(fieldName);
            }

        loadDataInTableView();

    }

    // ----- Delete entity -----
    private void deleteEntities(ObservableList<T> selectedEntity) {
        if (typeParameterClass.equals(Student.class)) {
            deleteStudents(selectedEntity);
        } else if (typeParameterClass.equals(Teacher.class)) {
            deleteTeachers(selectedEntity);
        } else if (typeParameterClass.equals(GroupType.class)) {
            deleteGroupType(selectedEntity);
        }
    }

    private void deleteStudents(ObservableList<T> selectedEntity) {
        StudentManager studentManager = new StudentManager();
        selectedEntity.forEach((student) -> {
            studentManager.deleteStudent((Student) student);
        });
    }

    private void deleteTeachers(ObservableList<T> selectedEntity) {
        TeacherManager teacherManager = new TeacherManager();
        selectedEntity.forEach((teacher) -> {
            teacherManager.deleteTeacher((Teacher) teacher);
        });
    }

    private void deleteGroupType(ObservableList<T> selectedEntity) {
        GroupTypeManager gtm = new GroupTypeManager();
        selectedEntity.forEach((gt) -> {
            gtm.deleteGroupType((GroupType) gt);
        });
    }
    //---------------------------

    void setBtnImages() {

        Image image = null;
        ImageView ImgView = null;
        try {
            image = new Image(getClass().getClassLoader().getResourceAsStream("images/add.png"));
            ImgView = new ImageView(image);
            ImgView.setFitHeight(30);
            ImgView.setFitWidth(30);
            btnAdd.setGraphic(ImgView);

            image = new Image(getClass().getClassLoader().getResourceAsStream("images/edit.png"));
            ImgView = new ImageView(image);
            ImgView.setFitHeight(30);
            ImgView.setFitWidth(30);
            btnEdit.setGraphic(ImgView);

            image = new Image(getClass().getClassLoader().getResourceAsStream("images/remove.png"));
            ImgView = new ImageView(image);
            ImgView.setFitHeight(30);
            ImgView.setFitWidth(30);
            btnDelete.setGraphic(ImgView);

            image = new Image(getClass().getClassLoader().getResourceAsStream("images/excel_load.png"));
            ImgView = new ImageView(image);
            ImgView.setFitHeight(30);
            ImgView.setFitWidth(30);
            btnLoadFromExcel.setGraphic(ImgView);

        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    private void setButtonProperties() {
        btnAdd.setTooltip(new Tooltip("Добавить запись"));
        btnEdit.setTooltip(new Tooltip("Редактировать запись"));
        btnDelete.setTooltip(new Tooltip("Удалить записи"));
        btnLoadFromExcel.setTooltip(new Tooltip("Загрузить из Excel"));
    }

    public void initForm() {

        showDicList();

        setPlaceProperty();

        setBtnImages();

        setButtonProperties();

        setColumnProperty();

        setFormEvents();

    }

    private void loadDataInTableView() {

        setEntityList();
        if (dataList.size() == 0)
            return;

        mainTable.setItems(dataList);
        if (dataList.size() > 0) {
            if (currentEntity == null) {
                mainTable.getSelectionModel().select(0);
                mainTable.getFocusModel().focus(0);
            } else {
                Integer index = getIndexByEntity(currentEntity);
                if (index >= 0) {
                    mainTable.requestFocus();
                    mainTable.getSelectionModel().select(index);
                    mainTable.getFocusModel().focus(index);
                }
            }
        }
        mainTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    // ----- Set entity list -----
    private void setEntityList() {
        if (typeParameterClass.equals(Student.class))
            dataList = (ObservableList<T>) getStudentList();
        else if (typeParameterClass.equals(Teacher.class))
            dataList = (ObservableList<T>) getTeacherList();
        else if (typeParameterClass.equals(GroupType.class))
            dataList = (ObservableList<T>) getGroupTypeList();
    }

    private ObservableList<Student> getStudentList() {

        StudentManager studentManager = new StudentManager();
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        List<Student> stList = studentManager.getAllStudent();

        for (Student student : stList) {
            studentList.add(student);
        }

        return studentList;
    }

    private ObservableList<Teacher> getTeacherList() {

        TeacherManager teacherManager = new TeacherManager();
        ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
        List<Teacher> tList = teacherManager.getAllTeacher();

        for (Teacher teacher : tList) {
            teacherList.add(teacher);
        }

        return teacherList;
    }

    private ObservableList<GroupType> getGroupTypeList() {

        GroupTypeManager gtManager = new GroupTypeManager();
        ObservableList<GroupType> gtoList = FXCollections.observableArrayList();
        List<GroupType> gtList = gtManager.getAllGroupType();

        for (GroupType gt : gtList) {
            gtoList.add(gt);
        }

        return gtoList;
    }
    // --------------------------

    // ----- Get entity index number in entity list -----
    private Integer getIndexByEntity(T entity) {
        if (typeParameterClass.equals(Student.class))
            return getIndexByStudent((Student) entity);
        else if (typeParameterClass.equals(Teacher.class))
            return getIndexByTeacher((Teacher) entity);

        return -1;
    }

    private Integer getIndexByStudent(Student student) {
        Integer index = 0;
        Integer studentId = student.getId();
        for (T st : dataList) {
            if (((Student) st).getId() == studentId) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private Integer getIndexByTeacher(Teacher teacher) {
        Integer index = 0;
        Integer teacherId = teacher.getId();
        for (T st : dataList) {
            if (((Teacher) st).getId() == teacherId) {
                return index;
            }
            index++;
        }
        return -1;
    }
    // --------------------------------------------------

    void setColumnProperty() {
        mainTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn id = findColumnByName("id");
        if (id != null)
            id.setVisible(false);

        TableColumn firstName = findColumnByName("firstName");
        if (firstName != null)
            firstName.prefWidthProperty().bind(dictionaryMain.widthProperty().divide(5.0));

        TableColumn secondName = findColumnByName("secondName");
        if (secondName != null)
            secondName.prefWidthProperty().bind(dictionaryMain.widthProperty().divide(5.0));

        TableColumn lastName = findColumnByName("lastName");
        if (lastName != null)
            lastName.prefWidthProperty().bind(dictionaryMain.widthProperty().divide(5.0));

        TableColumn createDate = findColumnByName("createDate");
        // set column createDate date format
        if (createDate != null) {
            createDate.setPrefWidth(80);
            createDate.setCellFactory(column -> {
                TableCell<T, String> cell = new TableCell<>() {
                    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            //setText(format.format(java.sql.Date.valueOf(item)));
                            try {
                                setText(format.format(format.parse(item)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };

                return cell;
            });
        }

        TableColumn birthDate = findColumnByName("birthDate");
        // set column birthDate date format
        if (birthDate != null) {
            birthDate.setPrefWidth(80);
            birthDate.setCellFactory(column -> {
                TableCell<T, String> cell = new TableCell<>() {
                    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) {
                            setText(null);
                        }
                        else {
                            String dateCurrent = "01.01.1500";
                            try {
                                if (item != null)
                                    dateCurrent = format.format(format.parse(item));

                                setText(dateCurrent);

                            } catch (ParseException exc) {
                                exc.printStackTrace();
                            }

                        }
                    }
                };

                return cell;
            });
        }

    }

    private TableColumn findColumnByName(String name) {
        int colIndex = columnList.indexOf(name);
        if (colIndex >= 0)
            return mainTable.getColumns().get(colIndex);
        else
            return null;
    }

    // Form events
    private void setFormEvents() {
        mainTable.setRowFactory( tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    showEditCreateWindow(EnumAction.UPDATE);
                }
            });
            return row ;
        });
    }

    private void showEditCreateWindow(EnumAction action) {

        ObservableList<T> selectedEntity = mainTable.getSelectionModel().getSelectedItems();

        if (action == EnumAction.UPDATE) {
            if (selectedEntity.size() == 0)
                return;
        }

        if (selectedEntity.size() > 0)
            currentEntity = selectedEntity.get(0);

        try {

            Stage studentDialogAction = new Stage();
            studentDialogAction.setTitle("Создание/редактирование");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/dictionary/DictionaryEditForm.fxml"));

            AnchorPane studentAction = loader.load();

            studentAction.setId("dictionaryFormAction");

            // Bind controller and main app
            DictionaryEditFormController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setStage(studentDialogAction);
            controller.setAction(action);
            controller.setTypeParameterClass(typeParameterClass);
            if (action == EnumAction.UPDATE) {
                controller.setEntity(currentEntity);
            }
            controller.initForm();

            studentDialogAction.setScene(new Scene(studentAction));
            studentDialogAction.initOwner(mainApp.getPrimaryStage());
            studentDialogAction.initModality(Modality.APPLICATION_MODAL);
            studentDialogAction.showAndWait();

            currentEntity = (T) controller.getEntity();

            System.gc();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showPreloadForm(List<T> stList) {

        try {

            Stage preloadFormStage = new Stage();
            preloadFormStage.setTitle("Форма предзагрузки");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/PreloadForm.fxml"));

            AnchorPane preloadForm = loader.load();

            preloadForm.setId("PreloadForm");

            // Bind controller and main app
            PreloadFormController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setStage(preloadFormStage);
            controller.setDataList(stList);
            controller.setMainPane(preloadForm);
            controller.setTypeParameterClass(getTypeParameterClassForPreloadForm());
            controller.setElementFormProperty();

            preloadFormStage.setScene(new Scene(preloadForm));
            preloadFormStage.initOwner(mainApp.getPrimaryStage());
            preloadFormStage.initModality(Modality.APPLICATION_MODAL);
            preloadFormStage.showAndWait();

            if (controller.mustRefreshData()) {
                loadDataInTableView();
            }

            System.gc();

            //currentStudent = controller.getStudent();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Class getTypeParameterClassForPreloadForm() {
        Class<?> act = null;
        try {
            act = Class.forName(typeParameterClass.getName() + "LoadInfo");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return act;
    }

    private List<T> getEntityListFromExcel(File file) {
        List<T> entityList = new ArrayList<>();
        try {
            if (typeParameterClass.equals(Student.class))
                entityList = (List<T>) ExcelReader.getStudentListFromExcel(file);
            else if (typeParameterClass.equals(Teacher.class))
                entityList = (List<T>) ExcelReader.getTeacherListFromExcel(file);
        } catch (IOException exc) {
        exc.printStackTrace();
        } catch (InvalidFormatException exc) {
            exc.printStackTrace();
        }
        return entityList;
    }

}