package entities;

public class Teacher extends Person {
    private int idTeacher;
    private int rating;
    private int active;

    public Teacher(int idTeacher, String name, String surname, int rating) {
        super(name, surname);
        this.idTeacher = idTeacher;
        this.rating = rating;
    }

    public Teacher(int idTeacher, String name, String surname, int rating, int active) {
        super(name, surname);
        this.idTeacher = idTeacher;
        this.rating = rating;
        this.active = active;
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
