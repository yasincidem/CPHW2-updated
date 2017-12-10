package Entity;

/**
 * Created by yasin_000 on 7.12.2017.
 */
public class RecordsForStudent {
    String studentId;
    String studentName;
    String grade;

    public RecordsForStudent(String... arr) {
        this.studentId = arr[0];
        this.studentName = arr[1];
        this.grade = arr[2];
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getGrade() {
        return grade;
    }



}
