package DAO;

import entities.Course;
import org.json.JSONObject;
import utils.JsonUtils;
import utils.UtilsMethods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CourseDAO {

    public static ArrayList<Course> getCourses(int role) throws SQLException {
        String query = role > 0 ? "SELECT * FROM course" : "SELECT idCourse, title FROM course WHERE active = 0";
        ArrayList<Course> response = new ArrayList<>();
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            int rows = UtilsMethods.countRows(rs);
            if (rows > 0) {
                rs.beforeFirst();
                while(rs.next()) {
                    if (role > 0) {
                        Course course = new Course(
                            rs.getInt("idCourse"),
                            rs.getString("title"),
                            rs.getInt("active")
                        );
                        response.add(course);
                    } else {
                        Course course = new Course(
                                rs.getInt("idCourse"),
                                rs.getString("title")
                        );
                        response.add(course);
                    }
                }

                System.out.println(response.toString());
                return response;
            } else {
                return null;
            }
        } finally {
            db.closeConnection();
        }
    }

    public static Course getCourse(int idCourse) throws SQLException {
        String query = "SELECT * FROM course WHERE idCourse = (?)";
        DbManager db = new DbManager();

        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idCourse);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Course(
                        rs.getInt("idCourse"),
                        rs.getString("title"),
                        rs.getInt("active")
                );
            } else {
                return null;
            }
        } finally {
            db.closeConnection();
        }
    }

    public static int createCourse(JSONObject request) throws SQLException {

        String title = request.getString("title");
        String query = "INSERT INTO course (title) VALUES (?)";
        DbManager db = new DbManager();

        try(PreparedStatement ps = db.openConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return (int)rs.getLong(1);
                } else {
                    return 0;
                }
            } else {
                return -1;
            }
        } finally {
            db.closeConnection();
        }
    }

    public static boolean delete(int id) throws SQLException {
        String query = "UPDATE course SET active = 1 WHERE idCourse = (?)";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public static boolean activeCourse(int id) throws SQLException {
        String query = "UPDATE course SET active = 0 WHERE idCourse = (?) AND active = 1";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } finally {
            db.closeConnection();
        }
    }

    public static boolean updateCourse(String title, int idCourse) throws SQLException {
        String query = "UPDATE course SET title = (?) WHERE idCourse = (?)";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setString(1, title);
            ps.setInt(2, idCourse);
            int rows = ps.executeUpdate();
            return rows > 0;
        } finally {
            db.closeConnection();
        }
    }
}
