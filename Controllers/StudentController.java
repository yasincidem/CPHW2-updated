package Controllers;

import Entity.RecordsForStudent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.ResourceBundle;

/**
 * Created by yasin_000 on 9.12.2017.
 */
public class StudentController implements Initializable{

    @FXML
    ImageView backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    }

    @FXML
    ChoiceBox<String> studentChoiceBox;
    @FXML
    TableView<RecordsForStudent> studentTableView;
    @FXML
    TableColumn<RecordsForStudent, String> studentStudentIdColumn;
    @FXML
    TableColumn<RecordsForStudent, String> studentStudentNameColumn;
    @FXML
    TableColumn<RecordsForStudent, String> studentGradeColumn;



    @FXML
    Label averageGrade;

    @FXML
    Label error;

    public double giveAverageGrade(ObservableList<RecordsForStudent> observableList){
        final OptionalDouble average = observableList.stream().map(RecordsForStudent::getGrade).mapToInt(Integer::parseInt).average();
        return average.getAsDouble();
    }

    @FXML
    public void showGrades() {
        String value = studentChoiceBox.getValue();
        if (value == null)
            error.setText("You must choose a course");
        else {
            studentStudentIdColumn.setCellValueFactory(new PropertyValueFactory<RecordsForStudent, String>("studentId"));
            studentStudentNameColumn.setCellValueFactory(new PropertyValueFactory<RecordsForStudent, String>("studentName"));
            studentGradeColumn.setCellValueFactory(new PropertyValueFactory<RecordsForStudent, String>("grade"));

            double average = 0;
            try {
                average = giveAverageGrade(getDataToStudent(value + ".txt"));
                error.setText("");
            } catch (IOException e) {
                studentTableView.setItems(null);
                lineChart.getData().clear();
                error.setText("No data for " + value + " course");
                e.printStackTrace();
            }

            averageGrade.setText(String.valueOf(average));

            try {
                studentTableView.setItems(getDataToStudent(value + ".txt"));
                error.setText("");
            } catch (IOException e) {
                studentTableView.setItems(null);
                lineChart.getData().clear();
                error.setText("No data for " + value + " course");
                e.printStackTrace();
            }
            try {
                drawChart();
                error.setText("");
            } catch (IOException e) {
                studentTableView.setItems(null);
                lineChart.getData().clear();
                error.setText("No data for " + value + " course");
                e.printStackTrace();
            }
        }


    }

    private ObservableList<RecordsForStudent> getDataToStudent(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        List<String> list = Files.readAllLines(path);


        List<RecordsForStudent> recordsList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String[] split = list.get(i).split(" ");
            if (split[1].contains("*")){
                split[1] = split[1].replace('*', ' ');
            }

            recordsList.add(new RecordsForStudent(split));
        }

        ObservableList<RecordsForStudent> observableList = FXCollections.observableList(recordsList);

        return observableList;
    }

    @FXML
    LineChart<String, Number> lineChart;

    @FXML
    public void drawChart() throws IOException {

        final String choiceBoxValue = studentChoiceBox.getValue();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        ObservableList<RecordsForStudent> observableList = getDataToStudent(choiceBoxValue + ".txt");

        int between0and20 = getGradesQuantityBetween(0, 20, observableList);
        int between20and40 = getGradesQuantityBetween(20, 40, observableList);
        int between40and60 = getGradesQuantityBetween(40, 60, observableList);
        int between60and80 = getGradesQuantityBetween(60, 80, observableList);
        int between80and100 = getGradesQuantityBetween(80, 100, observableList);

        XYChart.Series series = new XYChart.Series();

        series.getData().add(new XYChart.Data("0-20", between0and20));
        series.getData().add(new XYChart.Data("20-40", between20and40));
        series.getData().add(new XYChart.Data("40-60", between40and60));
        series.getData().add(new XYChart.Data("60-80", between60and80));
        series.getData().add(new XYChart.Data("80-100", between80and100));

        lineChart.setAnimated(false);

        lineChart.getData().clear();
        lineChart.getData().addAll(series);
    }

    public int getGradesQuantityBetween(int lower, int higher, ObservableList<RecordsForStudent> observableList){
        int count = 0;
        for (int i = 0; i < observableList.size(); i++) {
            final int grade = Integer.parseInt(observableList.get(i).getGrade());
            if (grade >= lower && grade <= higher)
                count++;
        }
        return count;
    }

}
