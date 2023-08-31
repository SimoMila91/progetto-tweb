package DTO;

public class TeacherCourseDTO {
    private int idTeacherCourse;
    private String titleCourse;

    public TeacherCourseDTO(int idTeacherCourse, String titleCourse) {
        this.idTeacherCourse = idTeacherCourse;
        this.titleCourse = titleCourse;
    }

    public int getIdTeacherCourse() {
        return idTeacherCourse;
    }

    public void setIdTeacherCourse(int idTeacherCourse) {
        this.idTeacherCourse = idTeacherCourse;
    }

    public String getTitleCourse() {
        return titleCourse;
    }

    public void setTitleCourse(String titleCourse) {
        this.titleCourse = titleCourse;
    }
}