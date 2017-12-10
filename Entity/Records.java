package Entity;

/**
 * Created by yasin_000 on 6.12.2017.
 */
public class Records {
    String studentId;
    String studentName;
    String grade;
    String course;

    public Records(String studentId, String studentName, String grade, String course) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.grade = grade;
        this.course = course;
    }

    public Records(String... arr) {
        this.studentId = arr[0];
        this.studentName = arr[1];
        this.grade = arr[2];
        this.course = arr[3];
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

    public String getCourse() {
        return course;
    }
}
