package entities;

public class User extends Person {
    private int idUser;
    private String psw;
    private int role;
    private String email;

    public User(String name, String surname, String email, int idUser, String psw, int role) {
        super(name, surname);
        this.idUser = idUser;
        this.psw = psw;
        this.role = role;
        this.email = email;
    }

    public User(String name, String surname, String email, String psw) {
        super(name, surname);
        this.psw = psw;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "idUser=" + idUser +
                ", psw='" + psw + '\'' +
                ", role=" + role +
                '}';
    }
}
