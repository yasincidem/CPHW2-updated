package Controllers;

import Entity.Records;
import Exceptions.DBAlreadyHasException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by yasin_000 on 9.12.2017.
 */
public class InstructorController implements Initializable{

    @FXML
    ImageView backButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showRecords();
            backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {

                    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("../resources/sample.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stage.setTitle("Grading System");
                    stage.setScene(new Scene(root, 500, 450));
                    stage.setResizable(false);
                    stage.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @FXML
    TextField studentNameTextField;
    @FXML
    TextField studentIdTextField;
    @FXML
    TextField gradeTextField;
    @FXML
    ChoiceBox<String> choiceBox ;
    @FXML
    Label errorStudentId;
    @FXML
    Label errorStudentName;
    @FXML
    Label errorStudentGrade;
    @FXML
    Label errorCourse;
    @FXML
    Label errorDBAlreadyHas;


    @FXML
    public void addRecord(ActionEvent actionEvent) throws IOException {
        System.out.println("clicked");

        if (choiceBox.getValue() == null) {
            errorCourse.setText("You must choose a course");
        }else{
            errorCourse.setText("");
            if (studentNameTextField.getText().equals("")){
                errorStudentName.setText("You must enter Student Name");
            }else {
                errorStudentName.setText("");

                try {
                    Long.parseLong((studentIdTextField.getText()));

                    System.out.println(studentIdTextField.getText());

                    errorStudentId.setText("");

                    try {
                        Long.parseLong((gradeTextField.getText()));
                        final int i = Integer.parseInt(gradeTextField.getText());
                        if (!(i >= 0 && i <= 100)){
                            errorStudentGrade.setText("Grade must be between 0 and 100");
                        }else {
                            errorStudentGrade.setText("");
                            handleDB();
                            errorDBAlreadyHas.setText("");
                            errorStudentGrade.setText("");
                            studentNameTextField.clear();
                            studentIdTextField.clear();
                            gradeTextField.clear();
                        }

                    }catch (NumberFormatException e){
                        if (errorStudentGrade.getText().equals(""))
                            errorStudentGrade.setText("You must enter Grade");
                        else{
                            errorStudentGrade.setText("Grade must be a number");
                        }
                    } catch (DBAlreadyHasException e) {
                        errorDBAlreadyHas.setText("This student record already exists");
                    }

                }catch (NumberFormatException e){
                    errorStudentId.setText("You must enter Student ID correctly");
                    if (studentIdTextField.getText().equals("")){
                        errorStudentId.setText("You must enter Student ID");
                    }
                }


            }
        }

        showRecords();

    }


    public void handleDB() throws IOException, DBAlreadyHasException {
        String fileName = choiceBox.getValue();

        Path path = Paths.get(fileName + ".txt");
        String[] arr = new String[]{studentIdTextField.getText(), studentNameTextField.getText(), gradeTextField.getText()};


        if (arr[1].contains(" ")) {
            arr[1] = arr[1].replace(' ', '*');
        }

        if (Files.exists(path)) {
            final List<String> list = Files.readAllLines(path);
            boolean doesContains = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).contains(arr[0])) {
                    doesContains = true;
                }
            }

            if (!doesContains) {
                Files.write(path, System.getProperty("line.separator").getBytes(), StandardOpenOption.APPEND);

                for (int i = 0; i < 3; i++) {
                    Files.write(path, (arr[i] + " ").getBytes(), StandardOpenOption.APPEND);
                }
            } else {
                System.out.println("There is already exists a student with id: " + arr[0]);
                throw new DBAlreadyHasException();
            }

        } else {
            Files.write(path, ("").getBytes(), StandardOpenOption.CREATE);

            for (int i = 0; i < 3; i++) {
                Files.write(path, (arr[i] + " ").getBytes(), StandardOpenOption.APPEND);
            }
        }

        Path allRecordsPath = Paths.get("allRecords.txt");
        if (Files.exists(allRecordsPath)) {
            Files.write(allRecordsPath, System.getProperty("line.separator").getBytes(), StandardOpenOption.APPEND);


            for (int i = 0; i < 3; i++) {
                Files.write(allRecordsPath, (arr[i] + " ").getBytes(), StandardOpenOption.APPEND);
            }
            Files.write(allRecordsPath, fileName.getBytes(), StandardOpenOption.APPEND);

        } else {
            Files.write(allRecordsPath, ("").getBytes(), StandardOpenOption.CREATE);

            for (int i = 0; i < 3; i++) {
                Files.write(allRecordsPath, (arr[i] + " ").getBytes(), StandardOpenOption.APPEND);
            }
            Files.write(allRecordsPath, fileName.getBytes(), StandardOpenOption.APPEND);
        }

    }




    @FXML
    TableView<Records> table ;

    @FXML
    TableColumn<Records, String> studentId;

    @FXML
    TableColumn<Records, String> studentName;

    @FXML
    TableColumn<Records, String> grade;

    @FXML
    TableColumn<Records, String> course;



    @FXML
    public void showRecords() throws IOException {


        studentId.setCellValueFactory(new PropertyValueFactory<Records, String>("studentId"));
        studentName.setCellValueFactory(new PropertyValueFactory<Records, String>("studentName"));
        grade.setCellValueFactory(new PropertyValueFactory<Records, String>("grade"));
        course.setCellValueFactory(new PropertyValueFactory<Records, String>("course"));


        table.setItems(getDataToInstructor("allRecords.txt"));
    }

    public ObservableList<Records> getDataToInstructor(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        List<String> list = Files.readAllLines(path);


        List<Records> recordsList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String[] split = list.get(i).split(" ");
            if (split[1].contains("*")){

                split[1] = split[1].replace('*', ' ');
            }
            recordsList.add(new Records(split));
        }

        ObservableList<Records> observableList = FXCollections.observableList(recordsList);
        Collections.reverse(observableList);
        return observableList;
    }


}
