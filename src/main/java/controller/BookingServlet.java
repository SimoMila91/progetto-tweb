package controller;

import DAO.BookingDAO;
import DTO.BookingDTO;
import com.google.gson.Gson;
import entities.Booking;
import org.json.JSONObject;
import utils.JsonUtils;
import utils.SessionHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;

@WebServlet(name = "BookingServlet", value = "/BookingServlet")
public class BookingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // checking if session is valid and idTeacher parameter exist
        PrintWriter out = response.getWriter();
        if (SessionHandler.checkSession(request)) {
            HttpSession session = request.getSession(false);

            int idUser = 0; // Valore di default se l'attributo non è presente o non è un intero valido

            idUser = (int) session.getAttribute("idUser");
            System.out.println(idUser); // debug
            if (idUser > 0) {
                JSONObject res = new JSONObject();
                try {
                    if (SessionHandler.checkIsAdmin(session)) {
                        System.out.println("admin");
                        ArrayList<BookingDTO> teacherBooking = BookingDAO.getBookedLessons();

                        res.put("message",
                                teacherBooking != null ?
                                        "Ci sono " + teacherBooking.size() + " prenotazioni"
                                        :
                                        "Non ci sono prenotazioni"
                        );
                        res.put("booking", teacherBooking);
                    } else {
                        ArrayList<BookingDTO> myBooking = BookingDAO.getMyBookedLessons(idUser);
                        res.put("message",
                                myBooking != null ?
                                        "Ci sono " + myBooking.size() + " prenotazioni"
                                        :
                                        "Non ci sono prenotazioni"
                        );
                        res.put("booking", myBooking);
                    }

                    out.println(res);
                    out.flush();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    response.setStatus(500);
                    res.put("message", "Errore interno nel sistema. Ritenta più tardi o contatta il supporto.");
                    out.println(res);
                    out.flush();
                }
            } else {
                JSONObject res = new JSONObject();
                response.setStatus(401);
                res.put("message", "Sessione compromessa");
                out.println(res);
                out.flush();
            }

        } else {
            JSONObject res = new JSONObject();
            response.setStatus(401);
            res.put("message", "Sessione scaduta");
            out.println(res);
            out.flush();
        }
        out.close();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (SessionHandler.checkSession(request)) {
                processRequest(request, response);
            } else {
                response.setStatus(401);
                response.getWriter().println("Non sei autorizzato");
            }
        } catch (SQLException | ParseException ex) {
            System.out.println(ex.getMessage());
            response.setStatus(500);
            response.getWriter().println("Errore interno nel sistema. Ritenta più tardi o contatta il supporto");
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ParseException {
        JSONObject req = JsonUtils.readJson(request);
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        int idBooking = 0;
        if (req.has("idBooking")) {
            idBooking = req.getInt("idBooking");
        }
        JSONObject res = new JSONObject();

        if (req.has("action")) {
            response.setContentType("application/json");
            String action = req.getString("action");

            switch (action) {
                case "create":
                    if (!SessionHandler.checkIsAdmin(session)) {
                        // get time and date
                        String timeString = req.getString("time");
                        String dateString = req.getString("date");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date utilDate = new Date(dateFormat.parse(dateString).getTime());
                        Time time = new Time(timeFormat.parse(timeString).getTime());
                        // get other params
                        int idUser = (int) session.getAttribute("idUser");
                        int idCourseTeacher = req.getInt("idCourseTeacher");
                        // create booking
                        Booking booking = new Booking(
                                time,
                                utilDate,
                                idUser,
                                idCourseTeacher
                        );
                        int bookingCreated = BookingDAO.createBooking(booking);
                        if (bookingCreated > 0) {
                            res.put("message", "Prenotazione effettuata con successo");
                            res.put("idBooking", bookingCreated);
                            out.println(res);
                            out.flush();
                        } else {
                            System.out.println("errore");
                        }
                    }
                    break;
                case "cancel":
                    boolean cancelBooking = BookingDAO.cancelBooking(idBooking);
                    if (cancelBooking) {
                        res.put("message", "Prenotazione cancellata correttamente");
                    } else {
                        response.setStatus(202);
                        res.put("message", "Errore nell'aggiornamento. Contattare il supporto");
                    }
                    out.println(res);
                    out.flush();
                    break;
                case "done":
                    System.out.println("qua");
                    if ((int)session.getAttribute("role") == 0) {
                        System.out.println("anche");
                        boolean bookingDone = BookingDAO.bookingDone(idBooking);
                        if (bookingDone) {
                            res.put("message", "La prenotazione è stata aggiornata con successo.");
                        } else {
                            response.setStatus(202);
                            res.put("message", "Errore nell'aggiornamento. Contattare il supporto");
                        }
                        out.println(res);
                        out.flush();
                    } else {
                        response.setStatus(401);
                        out.println("Non sei autorizzato");
                        out.flush();
                    }
                    break;
                default:
                    response.setStatus(404);
                    out.println("Errore nella richiesta");
                    out.flush();
                    break;
            }
        } else {
            response.setStatus(403);
            out.println("Errore nella richiesta");
            out.flush();
        }
        out.close();
    }

    @Override
    public void destroy() {}
}
