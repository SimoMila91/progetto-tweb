package entities;

public class Course {
    private int idCourse;
    private String title;
    private int active;

    public Course(int idCourse, String title, int active) {
        this.idCourse = idCourse;
        this.title = title;
        this.active = active;
    }

    public Course(int idCourse, String title) {
        this.idCourse = idCourse;
        this.title = title;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Course{" +
                "idCourse=" + idCourse +
                ", title='" + title + '\'' +
                ", active=" + active +
                '}';
    }
}
