package DAO;

import entities.Booking;
import entities.Teacher;
import org.json.JSONObject;
import utils.UtilsMethods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TeacherDAO {

    public static ArrayList<Teacher> getTeachers(String action) throws SQLException {
        DbManager db = new DbManager();
        try {
            ArrayList<Teacher> res = new ArrayList<>();
            String query = action.equals("active") ?
                    "SELECT * FROM teacher WHERE active = 1" : "select * from teacher";

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
        } finally {
            db.closeConnection();
        }
    }

    public static Teacher getTeacher(int id) throws SQLException {
        DbManager db = new DbManager();
        try {
            String query = "SELECT * FROM teacher WHERE idTeacher = (?)";
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
        } finally {
            db.closeConnection();
        }
    }

    public static boolean delete(int id) throws SQLException {
        String query = "UPDATE teacher SET active = 0 WHERE idTeacher = (?) AND active = 1";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } finally {
            db.closeConnection();
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
        } finally {
            db.closeConnection();
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
        } finally {
            db.closeConnection();
        }
    }

    public static ArrayList<Booking> getUnavailableDates(int idTeacher, int idUser) throws SQLException {
        String query = "" +
                "select DISTINCT * " +
                "from bookings b join courseteacher c on (b.idCourseTeacher = c.idCourseTeacher) " +
                "where (c.idTeacher = (?) or b.idUser = (?)) and state = 0";

        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idTeacher);
            ps.setInt(2, idUser);
            ResultSet rs = ps.executeQuery();

            if (UtilsMethods.countRows(rs) > 0) {
                rs.beforeFirst();
                ArrayList<Booking> response = new ArrayList<>();
                while (rs.next()) {
                    Booking datetime = new Booking(
                            rs.getDate("dateBooked"),
                            rs.getTime("hourBooked")
                    );
                    response.add(datetime);
                }
                return response;
            } else return null;
        } finally {
            db.closeConnection();
        }
    }

}
