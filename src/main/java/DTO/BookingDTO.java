package DTO;

import java.sql.Date;
import java.sql.Time;

public class BookingDTO {
    private int idBooking;
    private String hourBooked;
    private Date dateBooked;
    private int state;
    private String name;
    private String surname;
    private String courseTitle;
    private String studentName;
    private String teacherName;

    public BookingDTO(int idBooking, String hourBooked, Date dateBooked, int state, String name, String surname, String courseTitle) {
        this.idBooking = idBooking;
        this.hourBooked = hourBooked;
        this.dateBooked = dateBooked;
        this.state = state;
        this.name = name;
        this.surname = surname;
        this.courseTitle = courseTitle;
        this.studentName = "";
        this.teacherName = "";
    }

    public BookingDTO(int idBooking, String hourBooked, Date dateBooked, int state, String name, String surname, String courseTitle, String studentName, String teacherName) {
        this.idBooking = idBooking;
        this.hourBooked = hourBooked;
        this.dateBooked = dateBooked;
        this.name = name;
        this.surname = surname;
        this.state = state;
        this.studentName = studentName;
        this.courseTitle = courseTitle;
        this.teacherName = teacherName;
    }

    public int getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(int idBooking) {
        this.idBooking = idBooking;
    }

    public String getHourBooked() {
        return hourBooked;
    }

    public void setHourBooked(String hourBooked) {
        this.hourBooked = hourBooked;
    }

    public Date getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(Date dateBooked) {
        this.dateBooked = dateBooked;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "idBooking=" + idBooking +
                ", hourBooked=" + hourBooked +
                ", dateBooked=" + dateBooked +
                ", state=" + state +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
                '}';
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
