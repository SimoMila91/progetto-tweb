package entities;

public class TeacherCourse {
    private int idTeacherCourse;
    private int idTeacher;
    private int idCourse;
    private String teacherName;
    private String titleCourse ;

    public TeacherCourse(int idTeacherCourse, int idTeacher, int idCourse, String teacherName, String titleCourse) {
        this.idTeacherCourse = idTeacherCourse;
        this.idTeacher = idTeacher;
        this.idCourse = idCourse;
        this.teacherName = teacherName;
        this.titleCourse = titleCourse;
    }

    public int getIdTeacherCourse() {
        return idTeacherCourse;
    }

    public void setIdTeacherCourse(int idTeacherCourse) {
        this.idTeacherCourse = idTeacherCourse;
    }

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTitleCourse() {
        return titleCourse;
    }

    public void setTitleCourse(String titleCourse) {
        this.titleCourse = titleCourse;
    }

    @Override
    public String toString() {
        return "TeacherCourse{" +
                "idTeacherCourse=" + idTeacherCourse +
                ", idTeacher=" + idTeacher +
                ", idCourse=" + idCourse +
                ", teacherName='" + teacherName + '\'' +
                ", titleCourse='" + titleCourse + '\'' +
                '}';
    }
}
