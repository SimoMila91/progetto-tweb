package entities;

import java.sql.Date;
import java.sql.Time;

public class Booking {
    private int idBooking;
    private Time time;
    private Date date;
    private int state;
    private int idUser;
    private int idLesson;
    private String studentName;



    public Booking(int idBooking, Time time, Date date, int state, int idUser, int idLesson, String studentName) {
        this.idBooking = idBooking;
        this.time = time;
        this.date = date;
        this.state = state;
        this.idUser = idUser;
        this.idLesson = idLesson;
        this.studentName = studentName;
    }

    public Booking(Time time, Date date, int idUser, int idLesson) {
        this.time = time;
        this.date = date;
        this.idUser = idUser;
        this.idLesson = idLesson;
    }

    public Booking(Date date, Time time) {
        this.date = date;
        this.time = time;
    }

    public int getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(int idBooking) {
        this.idBooking = idBooking;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdLesson() {
        return idLesson;
    }

    public void setIdLesson(int idLesson) {
        this.idLesson = idLesson;
    }

    public String getStudentName() {
        return studentName;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "idBooking=" + idBooking +
                ", time=" + time +
                ", date=" + date +
                ", state=" + state +
                ", idUser=" + idUser +
                ", idLesson=" + idLesson +
                '}';
    }


}
