package DAO;

import entities.Teacher;
import entities.TeacherCourse;
import utils.UtilsMethods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TeacherCourseDAO {

    public static int createTeaching(int idTeacher, int idCourse) throws SQLException {
        String query = "INSERT INTO courseteacher (idCourse, idTeacher) VALUES (?, ?)";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCourse);
            ps.setInt(2, idTeacher);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return (int) rs.getLong(1);
                } else {
                    return 0;
                }
            } else {
                return -1;
            }
        }
    }

    public static ArrayList<TeacherCourse> getTeachings() throws SQLException {
        String query = "" +
                "select co.*, t.name, t.surname, c.title " +
                "from  courseteacher co " +
                "join teacher t on (t.idTeacher =  co.idTeacher)" +
                "join course c on (c.idCourse = co.idCourse) " +
                "where t.active = 0 and c.active = 0" +
                "order by t.name";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            int counter = UtilsMethods.countRows(rs);
            if (counter > 0) {
                rs.beforeFirst();
                ArrayList<TeacherCourse> response = new ArrayList<>();
                while (rs.next()) {
                    TeacherCourse tc = new TeacherCourse(
                            rs.getInt("idTeacherCourse"),
                            rs.getInt("idTeacher"),
                            rs.getInt("idCourse"),
                            rs.getString("name"),
                            rs.getString("title")
                    );
                    response.add(tc);
                }
                return response;
            } else {
                return null;
            }
        }
    }

    public static ArrayList<TeacherCourse> getTeacherCourses(int idTeacher) throws SQLException {
        String query = "" +
                "select co.*, c.course" +
                "from courseteacher co" +
                "join teacher t on (t.idTeacher = co.idTeacher)" +
                "join course c on (c.idCourse = co.idCourse)" +
                "where active = 0 and c.active = 0 and idTeacher = (?)";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idTeacher);
            ResultSet rs = ps.executeQuery();
            int counter = UtilsMethods.countRows(rs);
            if (counter > 0) {
                rs.beforeFirst();
                ArrayList<TeacherCourse> response = new ArrayList<>();
                while (rs.next()) {
                    TeacherCourse tc = new TeacherCourse(
                            rs.getInt("idTeacherCourse"),
                            rs.getInt("idTeacher"),
                            rs.getInt("idCourse"),
                            rs.getString("name"),
                            rs.getString("title")
                    );
                    response.add(tc);
                }
                return response;
            } else {
                return null;
            }
        }
    }

    public static ArrayList<Teacher> getCourseTeachers(int idCourse) throws SQLException {
        String query = "" +
                "select t.*, c.name" +
                "from teacher t join courseteacher ct on (t.idTeacher = ct.idTeacher) " +
                "join course c on (ct.idCourse = c.idCourse)" +
                "where c.idCourse = (?) and t.active = 0 and c.active = 0";

        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idCourse);
            ResultSet rs = ps.executeQuery();
            ArrayList<Teacher> response = new ArrayList<>();
            while(rs.next()) {
                Teacher teacher = new Teacher(
                        rs.getInt("idTeacher"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getInt("rating")
                );
                response.add(teacher);
            }
            return response;
        }
    }

    public static boolean deleteTeaching(int idTeaching) throws SQLException {
        String query = "UPDATED courseteacher SET active = 1  where idCourseTeacher = (?)";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idTeaching);
            int row = ps.executeUpdate();
            return row > 0;
        }
    }
}
