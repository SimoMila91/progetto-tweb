package entities;

import java.util.List;

public class Teacher extends Person {
    private int idTeacher;
    private int rating;
    private int active;
    private String image;

    public Teacher(int idTeacher, String name, String surname, int rating, String image) {
        super(name, surname);
        this.idTeacher = idTeacher;
        this.rating = rating;
        this.image = image;
    }

    public Teacher(int idTeacher, String name, String surname, int rating, int active) {
        super(name, surname);
        this.idTeacher = idTeacher;
        this.rating = rating;
        this.active = active;
        this.image = image;
    }

    public Teacher(String name, String surname, int idTeacher, int rating) {
        super(name, surname);
        this.idTeacher = idTeacher;
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Docente{" +
                "idDocente=" + idTeacher +
                ", rating=" + rating +
                '}';
    }
}
