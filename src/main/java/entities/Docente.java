package entities;

public class Docente extends Persona {

    private int idTeacher;
    private int rating;
    private Persona person;

    public Docente(String nome, String cognome, String email, int idTeacher, int rating, Persona person) {
        super(nome, cognome, email);
        this.idTeacher = idTeacher;
        this.rating = rating;
        this.person = person;
    }

    public int getIdDocente() {
        return idTeacher;
    }

    public void setIdDocente(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Persona getPersona() {
        return person;
    }

    public void setPersona(Persona person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Docente{" +
                "idDocente=" + idTeacher +
                ", rating=" + rating +
                ", persona=" + person +
                '}';
    }
}
