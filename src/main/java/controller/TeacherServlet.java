package controller;

import DAO.BookingDAO;
import DAO.TeacherDAO;
import DAO.UserDAO;
import com.google.gson.Gson;
import entities.Booking;
import entities.Teacher;
import org.json.JSONObject;
import utils.JsonUtils;
import utils.SessionHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "TeacherServlet", value = "/TeacherServlet")
public class TeacherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (SessionHandler.checkSession(request)) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            int idTeacher = 0;
            String action = "";
            if (request.getParameter("action") != null) {
                action = request.getParameter("action");
            }
            if (request.getParameter("idTeacher") != null) {
                idTeacher = Integer.parseInt(request.getParameter("idTeacher"));
            }

            try {
                if (idTeacher > 0) {
                    Teacher getTeacher = TeacherDAO.getTeacher(idTeacher);
                    if (getTeacher != null) {
                        out.println(new Gson().toJson(getTeacher));
                        out.flush();
                    } else {
                        response.setStatus(404);
                        out.println("Il docente non esiste");
                        out.flush();
                    }
                } else {
                    ArrayList<Teacher> getTeachers =  TeacherDAO.getTeachers(action);

                    JSONObject res = new JSONObject();
                    res.put("teachers", getTeachers);
                    String json = new Gson().toJson(getTeachers);
                    out.println(json);
                    out.flush();
                }

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            response.setStatus(401);
            JSONObject res = new JSONObject();
            res.put("message", "Sessione scaduta");
            response.getWriter().println(res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {

        PrintWriter out = res.getWriter();
        JSONObject request = JsonUtils.readJson(req);
        String action = request.getString("action");
        res.setContentType("application/json");

        // controllo la sessione
        if (SessionHandler.checkSession(req)) {
            HttpSession session = req.getSession(false);
            switch (action) {
                case "delete":
                    if (SessionHandler.checkIsAdmin(session)) {
                        boolean delete = TeacherDAO.delete(request.getInt("idTeacher"));
                        System.out.println(delete);
                        out.println(delete);
                        out.flush();
                    } else {
                        res.setStatus(401);
                        out.println("Non hai i permessi per effettuare questa operazione");
                        out.flush();
                    }
                    break;
                case "create":
                    if (SessionHandler.checkIsAdmin(session)) {
                        long createTeacher = TeacherDAO.createTeacher(request);
                        if (createTeacher > 0) {
                            out.println(new Gson().toJson(createTeacher));
                            out.flush();
                        } else {
                            res.setStatus(500);
                            out.println("Errore nel sistema, contattare il supporto");
                            out.flush();
                        }
                    } else {
                        res.setStatus(401);
                        out.println("Non hai i permessi per effettuare questa operazione");
                        out.flush();
                    }

                    break;
                case "active":
                    if (SessionHandler.checkIsAdmin(session)) {
                        boolean activeTeacher = TeacherDAO.activeTeacher(request.getInt("idTeacher"));
                        if (activeTeacher) {
                            out.println("Docente riattivato correttamente");
                            out.flush();
                        } else {
                            res.setStatus(404);
                            out.println("Errore nella richiesta di riattivazione docente");
                            out.flush();
                        }
                    } else {
                        res.setStatus(401);
                        out.println("Non hai i permessi per effettuare questa operazione");
                        out.flush();
                    }
                    break;
                case "getTeacherDates":
                    if (!SessionHandler.checkIsAdmin(session)) {
                        ArrayList<Booking> unavailableDates = TeacherDAO.getUnavailableDates(request.getInt("idTeacher"), (Integer) session.getAttribute("idUser"));
                        JSONObject response = new JSONObject();
                        if (unavailableDates != null) {
                            response.put("dates", unavailableDates);
                        } else {
                            response.put("dates", (Object) null);
                        }
                        System.out.println(response.toString());
                        out.println(response);
                        out.flush();
                    } else if(SessionHandler.checkIsAdmin(session) && request.has("idTeacher")) {
                        ArrayList<Booking> bookedLessons = BookingDAO.getBookedLessons(request.getInt("idTeacher"));
                        JSONObject response = new JSONObject();
                        response.put("dates", bookedLessons);
                        out.println(response);
                        out.flush();
                    } else {
                        res.setStatus(401);
                        out.println("Non sei autorizzato");
                        out.flush();
                    }
                    break;
                default:
                    res.setStatus(404);
                    out.println("Errore nella richiesta");
                    out.flush();
            }
        } else {
            //sessione scaduta
            res.setStatus(401);
            JSONObject response = new JSONObject();
            response.put("message", "Sessione scaduta");
            out.println(response);
            out.flush();
        }
        out.close();
    }

    public void destroy() {}
}
