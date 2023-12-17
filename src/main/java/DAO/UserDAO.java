package DAO;
import entities.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    /* Register user  */
    public static User signUp(User user) throws SQLException {
        String cryptPsw = BCrypt.hashpw(user.getPsw(), BCrypt.gensalt());
        String query = "INSERT INTO users (name, surname, psw, email) VALUES (?,?,?,?)";
        try (DbManager db = new DbManager();
             PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setString(3, cryptPsw);
            ps.setString(4, user.getEmail());

            int rs = ps.executeUpdate();
            if (rs > 0) {
                return login(user.getEmail(), user.getPsw());
            } else return null;
        }
    }



    /* Login user */
    public static User login(String email, String psw) throws SQLException {
        try {
            String query = "SELECT * FROM users WHERE email = (?)";

            try (DbManager db = new DbManager();
                 PreparedStatement ps = db.openConnection().prepareStatement("SELECT * FROM users WHERE email = (?)")) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if (BCrypt.checkpw(psw, rs.getString("psw"))) {
                        User res = new User(
                                rs.getString("name"),
                                rs.getString("surname"),
                                rs.getString("email"),
                                rs.getInt("idUser"),
                                rs.getString("psw"),
                                rs.getInt("admin")
                        );
                        return res;
                    } else {
                        return null;
                    }
                } return null;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
