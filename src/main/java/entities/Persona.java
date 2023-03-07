package entities;

public class Persona {
    private String name;
    private String surname;
    private String email;

    public Persona(String nome, String cognome, String email) {
        this.name = nome;
        this.surname = cognome;
        this.email = email;
    }

    public String getNome() {
        return name;
    }

    public void setNome(String nome) {
        this.name = nome;
    }

    public String getCognome() {
        return surname;
    }

    public void setCognome(String cognome) {
        this.surname = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nome='" + name + '\'' +
                ", cognome='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
