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
                return response;
            } else {
                return null;
            }
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
        }
    }

    public static boolean delete(int id) throws SQLException {
        String query = "UPDATE course SET active = 0 WHERE idCourse = (?) AND active = 1";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public static boolean activeCourse(int id) throws SQLException {
        String query = "UPDATE course SET active = 1 WHERE idCourse = (?) AND active = 0";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}
