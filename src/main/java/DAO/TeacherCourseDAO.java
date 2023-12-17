package DAO;

import DTO.TeacherCourseDTO;
import entities.Teacher;
import entities.TeacherCourse;
import utils.UtilsMethods;

import java.sql.*;
import java.util.ArrayList;

public class TeacherCourseDAO {

    public static int createTeaching(int idTeacher, int idCourse) throws SQLException {
        String queryCheck = "SELECT idCourseTeacher FROM courseteacher WHERE idCourse = ? AND idTeacher = ? AND active = 1";
        String queryUpdate = "UPDATE courseteacher SET active = 0 WHERE idCourse = ? AND idTeacher = ?";
        String queryInsert = "INSERT INTO courseteacher (idCourse, idTeacher) VALUES (?, ?)";

        DbManager db = new DbManager();

        try (Connection conn = db.openConnection();
             PreparedStatement psCheck = conn.prepareStatement(queryCheck);
             PreparedStatement psUpdate = conn.prepareStatement(queryUpdate);
             PreparedStatement psInsert = conn.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS)) {

            psCheck.setInt(1, idCourse);
            psCheck.setInt(2, idTeacher);
            ResultSet rsCheck = psCheck.executeQuery();

            if (rsCheck.next()) {
                psUpdate.setInt(1, idCourse);
                psUpdate.setInt(2, idTeacher);
                int rowsUpdated = psUpdate.executeUpdate();
                if (rowsUpdated > 0) {
                    return rsCheck.getInt("idCourseTeacher");
                } else {
                    return -1;
                }
            } else {
                psInsert.setInt(1, idCourse);
                psInsert.setInt(2, idTeacher);
                int rowsInserted = psInsert.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet rsInsert = psInsert.getGeneratedKeys();
                    if (rsInsert.next()) {
                        return rsInsert.getInt(1);
                    } else {
                        return 0;
                    }
                } else {
                    return -1;
                }
            }
        } finally {
            db.closeConnection();
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
        } finally {
            db.closeConnection();
        }
    }

    public static ArrayList<TeacherCourseDTO> getTeacherCourses(int idTeacher) throws SQLException {
        String query = "" +
                "select co.idCourseTeacher, c.title" +
                " from courseteacher co" +
                " join teacher t on (t.idTeacher = co.idTeacher)" +
                " join course c on (c.idCourse = co.idCourse)" +
                " where c.active = 0 and co.active = 0 and t.idTeacher = (?) and co.idTeacher = (?)";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idTeacher);
            ps.setInt(2, idTeacher);
            ResultSet rs = ps.executeQuery();
            int counter = UtilsMethods.countRows(rs);
            if (counter > 0) {
                rs.beforeFirst();
                ArrayList<TeacherCourseDTO> response = new ArrayList<>();
                while (rs.next()) {
                    TeacherCourseDTO tc = new TeacherCourseDTO(
                            rs.getInt("idCourseTeacher"),
                            rs.getString("title")
                    );
                    response.add(tc);
                }
                return response;
            } else {
                return null;
            }
        } finally {
            db.closeConnection();
        }
    }

    public static ArrayList<Teacher> getCourseTeachers(int idCourse) throws SQLException {
        String query;
        if (idCourse == 0) {
            query = "" +
                    "select distinct t.*, c.title" +
                    " from teacher t join courseteacher ct on (t.idTeacher = ct.idTeacher) " +
                    " join course c on (c.idCourse = ct.idCourse)" +
                    " where t.active = 1 and c.active = 0 and ct.active = 0" +
                    " group by t.idTeacher";
        } else {
            query = "" +
                    "select t.*, c.title" +
                    " from teacher t join courseteacher ct on (t.idTeacher = ct.idTeacher) " +
                    " join course c on (c.idCourse = ct.idCourse)" +
                    " where c.idCourse = (?) and t.active = 1 and c.active = 0 and ct.active = 0";
        }

        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            if (idCourse > 0 ) {
                ps.setInt(1, idCourse);
            }
            ResultSet rs = ps.executeQuery();
            ArrayList<Teacher> response = new ArrayList<>();
            while(rs.next()) {
                Teacher teacher = new Teacher(
                        rs.getInt("idTeacher"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getInt("rating"),
                        rs.getString("image")
                );
                response.add(teacher);
            }
            return response;
        } finally {
            db.closeConnection();
        }
    }

    public static boolean deleteTeaching(int idTeaching) throws SQLException {
        String query = "UPDATE courseteacher SET active = 1  where idCourseTeacher = (?)";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idTeaching);
            int row = ps.executeUpdate();
            return row > 0;
        } finally {
            db.closeConnection();
        }
    }
}
