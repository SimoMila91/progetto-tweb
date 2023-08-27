package DAO;

import entities.Booking;
import utils.UtilsMethods;

import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.sql.*;
import java.util.ArrayList;

public class BookingDAO {
    /**
     * 1: getAll
     * 2: update => admin ? disdici | => user ?  effettuata or disdici
     * 3: create
     * 4:
     */

    public static int createBooking(Booking booking) throws SQLException {
        // check if exists
        if (checkExistingBooking(booking.getIdLesson(), booking.getDate(), booking.getTime())) {
            return -1;
        }

        String query = "" +
                "insert into booking" +
                "(dateBooked, hourBooked, idUser, idCourseTeacher)" +
                "values (?, ?, ?, ?)";
        DbManager db = new DbManager();
        try(PreparedStatement ps = db.openConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, booking.getDate());
            ps.setTime(2, booking.getTime());
            ps.setInt(3, booking.getIdUser());
            ps.setInt(4, booking.getIdLesson());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return (int)rs.getLong(1);
                } else return 0;
            } else return 0;
        }
    }

    public static boolean checkExistingBooking(int idTeaching, Date datetime, Time time) throws SQLException {
        String query = "" +
                "select idBooking" +
                "from booking" +
                "where idCourseTeacher = (?) and dateBooked = (?) and hourBooked = (?)";
        DbManager db = new DbManager();

        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idTeaching);
            ps.setDate(2, datetime);
            ps.setTime(3, time);

            return ps.execute();
        }
    }

    public static ArrayList<Booking> getMyBookedLessons(int idUser) throws SQLException {
        String query = "" +
                "select *" +
                "from booking " +
                "where idUser = (?)" +
                "order by dateBooked desc";
        DbManager db = new DbManager();
        try(PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();

            if (UtilsMethods.countRows(rs) > 0) {
                rs.beforeFirst();
                ArrayList<Booking> response = new ArrayList<>();
                while(rs.next()) {
                    Booking booked = new Booking(
                            rs.getInt("idBooking"),
                            rs.getTime("hourBooked"),
                            rs.getDate("dateBooked"),
                            rs.getInt("state"),
                            rs.getInt("idUser"),
                            rs.getInt("idCourseTeacher"),
                            rs.getString("name")
                    );
                    response.add(booked);
                }
                return response;
            } else return null;

        }
    }

    public static ArrayList<Booking> getBookedLessons(int idTeacher) throws SQLException {
        String query = "" +
                "select b.*, u.name" +
                "from teacher t join courseTeacher ct on (t.idTeacher = ct.idTeacher)" +
                "join booking b on (b.idCourseTeacher = ct.idCourseTeacher)" +
                "join user u on (b.idUser = u.idUser)" +
                "where idTeacher = (?)" +
                "order by dateBooked DESC";
        DbManager db = new DbManager();
        try (PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idTeacher);
            ResultSet rs = ps.executeQuery();

            if (UtilsMethods.countRows(rs) > 0) {
                rs.beforeFirst();
                ArrayList<Booking> response = new ArrayList<>();
                while(rs.next()) {
                    Booking booked = new Booking(
                        rs.getInt("idBooking"),
                        rs.getTime("hourBooked"),
                        rs.getDate("dateBooked"),
                        rs.getInt("state"),
                        rs.getInt("idUser"),
                        rs.getInt("idCourseTeacher"),
                        rs.getString("name")
                    );
                    response.add(booked);
                }
                return response;
            } else return null;
        }
    }

    // admin and user
    public static boolean cancelBooking(int idBooking) throws SQLException {
        String query = "" +
                "update booking " +
                "set state = 2 " +
                "where idBooking = (?)";
        DbManager db = new DbManager();
        try(PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idBooking);
            return ps.executeUpdate() > 0;
        }
    }


    public static boolean bookingDone(int idBooking) throws SQLException {
        String query = "" +
                "update booking " +
                "set state = 1 " +
                "where idBooking = (?)";
        DbManager db = new DbManager();
        try(PreparedStatement ps = db.openConnection().prepareStatement(query)) {
            ps.setInt(1, idBooking);
            return ps.executeUpdate() > 0;
        }
    }
}
