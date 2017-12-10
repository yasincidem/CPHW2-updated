package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller{

    @FXML
    public void onStudentClick(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource("../resources/student.fxml"));

        stage.setScene(new Scene(root,500, 450));
        stage.setTitle("Student Page");
        stage.show();
    }

    @FXML
    public void onInstructorClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource("../resources/instructor.fxml"));

        stage.setScene(new Scene(root,500, 450));
        stage.setTitle("Instructor Page");
        stage.show();
    }

}
