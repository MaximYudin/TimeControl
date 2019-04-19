package org.russianfeature.controllers.dictionary;

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
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    void initialize() {

        /*showDicList();

        setBtnImages();

        setButtonProperties();

        showStudents();

        setColumnProperty();



        setFormEvents();


        */
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
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
        //if (typeParameterClass.equals(Teacher.class)) {

            for (Field fld : typeParameterClass.getDeclaredFields()) {

                if (fld.getType().equals(IntegerProperty.class)) {
                    TableColumn<T, Integer> myColumn = new TableColumn<>();
                    myColumn.setCellValueFactory(new PropertyValueFactory<T, Integer>(fld.getName()));
                    myColumn.setId(fld.getName());
                    mainTable.getColumns().add(myColumn);
                } else {
                    TableColumn<T, String> myColumn = new TableColumn<>();
                    myColumn.setCellValueFactory(new PropertyValueFactory<T, String>(fld.getName()));
                    myColumn.setId(fld.getName());
                    mainTable.getColumns().add(myColumn);
                }
                columnList.add(fld.getName());
            }

        loadDataInTableView();

    }


    @FXML
    void btnAddOnClick(ActionEvent event) {

        showEditCreateWindow(EnumAction.CREATE);
        loadDataInTableView();

    }

    @FXML
    void btnDeleteOnClick(ActionEvent event) {

        /*
        ObservableList<Student> selectedStudent = tableStudents.getSelectionModel().getSelectedItems();
        StringBuilder msgText = new StringBuilder();
        if (selectedStudent.size() > 1) {
            msgText.append("Вы действительно хотите удалить записи?");
        } else {
            msgText.append("Вы действительно хотите удалить запись?");
        }

        CommonUtil.showYesNoQuestionWindow(mainApp, msgText.toString());
        if (QuestionYeaNoController.answer == EnumYesNo.NO)
            return;

        // Check relationships
        if (selectedStudent.size() == 0) return;
        StudentManager studentManager = new StudentManager();
        selectedStudent.forEach((student) -> {
            studentManager.deleteStudent(student);
        });

        loadDataInTableView();
        */
    }

    @FXML
    void btnEditOnClick(ActionEvent event) {
        showEditCreateWindow(EnumAction.UPDATE);
    }

    @FXML
    void btnLoadFromExcelOnClick(ActionEvent event) {
        /*
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

            List<StudentLoadInfo> studentList = FXCollections.observableArrayList();
            try {
                studentList = ExcelReader.getStudentListFromExcel(file);
                int a = 1;
            } catch (IOException exc) {
                exc.printStackTrace();
            } catch (InvalidFormatException exc) {
                exc.printStackTrace();
            }

            showPreloadForm(studentList);
        }
        */
    }

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

    public void setTypeParameterClass(Class<T> clazz) {
        typeParameterClass = clazz;
    }

    public void initForm() {

        showDicList();

        setPlaceProperty();

        setBtnImages();

        setButtonProperties();

        setColumnProperty();

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

    private void loadDataInTableView() {

        if (typeParameterClass.equals(Student.class))
            dataList = (ObservableList<T>) getStudentList();
        else if (typeParameterClass.equals(Teacher.class))
            dataList = (ObservableList<T>) getTeacherList();
        else
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
                TableCell<Student, String> cell = new TableCell<>() {
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
                TableCell<Student, String> cell = new TableCell<>() {
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

    void showEditCreateWindow(EnumAction action) {

        ObservableList<T> selectedStudent = mainTable.getSelectionModel().getSelectedItems();
        if (selectedStudent.size() == 0
                && action == EnumAction.UPDATE)
            return;
        //T entity = selectedStudent.get(0);
        //System.out.println(entity);

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
            controller.initForm();
            if (action == EnumAction.UPDATE) {
                controller.setEntity(currentEntity);
                //controller.setFieldValues();
            }

            studentDialogAction.setScene(new Scene(studentAction));
            studentDialogAction.initOwner(mainApp.getPrimaryStage());
            studentDialogAction.initModality(Modality.APPLICATION_MODAL);
            studentDialogAction.showAndWait();

            currentEntity = (T) controller.getEntity();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    void setColumnProperty() {
        tableStudents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        firstName.prefWidthProperty().bind(dictionaryStudentsMain.widthProperty().divide(5.0));
        secondName.prefWidthProperty().bind(dictionaryStudentsMain.widthProperty().divide(5.0));
        lastName.prefWidthProperty().bind(dictionaryStudentsMain.widthProperty().divide(5.0));
        createDate.setPrefWidth(80);
        birthDate.setPrefWidth(80);
        id.setVisible(false);

        // set column createDate date format
        createDate.setCellFactory(column -> {
            TableCell<Student, String> cell = new TableCell<>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
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


        // set column birthDate date format
        birthDate.setCellFactory(column -> {
            TableCell<Student, String> cell = new TableCell<>() {
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
    */
/*


    void showPreloadForm(List<StudentLoadInfo> stList) {

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
            controller.setElementFormProperty();

            preloadFormStage.setScene(new Scene(preloadForm));
            preloadFormStage.initOwner(mainApp.getPrimaryStage());
            preloadFormStage.initModality(Modality.APPLICATION_MODAL);
            preloadFormStage.showAndWait();

            if (controller.mustRefreshData()) {
                loadDataInTableView();
            }

            //currentStudent = controller.getStudent();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void showStudents() {

        //Binding model and view elements
        id.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
        secondName.setCellValueFactory(new PropertyValueFactory<Student, String>("secondName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
        createDate.setCellValueFactory(new PropertyValueFactory<Student, String>("createDate"));
        birthDate.setCellValueFactory(new PropertyValueFactory<Student, String>("birthDate"));
        comment.setCellValueFactory(new PropertyValueFactory<Student, String>("comment"));

        loadDataInTableView();

    }








*/

}