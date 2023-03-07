package entities;

public class Utente extends Persona {
    private int idUser;
    private String psw;
    private int role;
    private Persona person;

    public Utente(String nome, String cognome, String email, int idUser, String psw, int role, Persona person) {
        super(nome, cognome, email);
        this.idUser = idUser;
        this.psw = psw;
        this.role = role;
        this.person = person;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Persona getPerson() {
        return person;
    }

    public void setPerson(Persona person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "idUser=" + idUser +
                ", psw='" + psw + '\'' +
                ", role=" + role +
                ", person=" + person +
                '}';
    }
}
