package DAO;

import entities.Teacher;
import org.json.JSONObject;
import utils.UtilsMethods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TeacherDAO {

    public static ArrayList<Teacher> getTeachers() throws SQLException {
        try {
            ArrayList<Teacher> res = new ArrayList<>();
            String query = "SELECT * FROM teacher";
            DbManager db = new DbManager();
            try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                int counter = UtilsMethods.countRows(rs);
                if (counter > 0) {
                    rs.beforeFirst();
                    while(rs.next()) {
                        Teacher t = new Teacher(
                                rs.getInt("idTeacher"),
                                rs.getString("name"),
                                rs.getString("surname"),
                                rs.getInt("rating"),
                                rs.getInt("active")
                        );
                        res.add(t);
                    }
                    return res;
                } else {
                    return null;
                }
            }
        }  catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static Teacher getTeacher(int id) throws SQLException {
        try {
            String query = "SELECT * FROM teacher WHERE idTeacher = (?)";
            DbManager db = new DbManager();
            try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Teacher t = new Teacher(
                            rs.getInt("idTeacher"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getInt("rating"),
                            rs.getInt("active")
                    );
                    return t;
                } else {
                    return null;
                }
            }
        }  catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static boolean delete(int id) throws SQLException {
        String query = "UPDATE teacher SET active = 0 WHERE idTeacher = (?) AND active = 1";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // riattivo il docente se disattivato => active = 0 ? active = 1 : error
    public static boolean activeTeacher(int id) throws SQLException {
        String query = "UPDATE teacher SET active = 1 WHERE idTeacher = (?) AND active = 0";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // creo un nuovo docente
    public static long createTeacher(JSONObject object) throws SQLException {
        String name = object.getString("name");
        String surname = object.getString("surname");
        String query = "INSERT INTO teacher (name, surname) VALUES (?,?)";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, surname);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
    }
}
