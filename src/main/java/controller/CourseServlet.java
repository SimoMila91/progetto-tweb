package controller;

import DAO.CourseDAO;
import DAO.TeacherDAO;
import com.google.gson.Gson;
import entities.Course;
import org.json.JSONObject;
import utils.JsonUtils;
import utils.SessionHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "CourseServlet", value = "/CourseServlet")
public class CourseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        System.out.println(SessionHandler.checkSession(request));
        if (SessionHandler.checkSession(request))  {
            int idCourse = 0;
            if (request.getParameter("idCourse") != null) {
                idCourse = Integer.parseInt(request.getParameter("idCourse"));
            }
            try {
                if (idCourse > 0) {
                    Course getCourse = CourseDAO.getCourse(idCourse);
                    if (getCourse != null) {
                        out.println(new Gson().toJson(getCourse));
                        out.flush();
                    } else {
                        response.setStatus(404);
                        out.println("Il corso non esiste");
                        out.flush();
                    }
                } else {
                    ArrayList<Course> getCourses =  CourseDAO.getCourses((int)request.getSession().getAttribute("role"));
                    if (getCourses != null) {
                        String json = new Gson().toJson(getCourses);
                        out.println(json);
                        out.flush();
                    } else {
                        out.println("Non ci sono corsi");
                        out.flush();
                    }
                }

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            ArrayList<Course> getCourses = null;
            try {
                getCourses = CourseDAO.getCourses(0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (getCourses != null) {
                String json = new Gson().toJson(getCourses);
                out.println(json);
                out.flush();
            } else {
                out.println("Non ci sono corsi");
                out.flush();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JSONObject res = new JSONObject();
            PrintWriter out = response.getWriter();
            if (ex.getErrorCode() == 1062) {
                response.setStatus(409);
                res.put("message", "Non puoi creare un corso due volte. Si prega di crearne uno nuovo.");
            } else {
                response.setStatus(500);
                res.put("message", "Errore interno al server. Si prega di contattare il supporto.");
            }
            out.println(res);
            out.flush();
            out.close();
        }
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
        PrintWriter out = res.getWriter();
        JSONObject request = JsonUtils.readJson(req);
        String action = request.getString("action");
        res.setContentType("application/json");

        if (SessionHandler.checkSession(req)) {
            HttpSession session = req.getSession(false);
            switch (action) {
                case "create":
                    if (SessionHandler.checkIsAdmin(session)) {
                        int createCourse = CourseDAO.createCourse(request);
                        if (createCourse > 0) {
                            JSONObject response = new JSONObject();
                            response.put("idCourse", createCourse);
                            out.println(response);
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
                case "update":
                    if (SessionHandler.checkIsAdmin(session)) {
                       boolean updateTitle = CourseDAO.updateCourse(request.getString("title"), request.getInt("idCourse"));
                        if (updateTitle) {
                            out.println("Aggiornamento riuscito");
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
                case "delete":
                    if (SessionHandler.checkIsAdmin(session)) {
                        boolean deactivateCourse = CourseDAO.delete(request.getInt("idCourse"));
                        if (deactivateCourse) {
                            out.println("Corso eliminato");
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
                        boolean activeCourse = CourseDAO.activeCourse(request.getInt("idCourse"));
                        if (activeCourse) {
                            out.println("Corso riattivato correttamente");
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
                default:
                    res.setStatus(404);
                    out.println("Errore nella richiesta");
                    out.flush();
            }
        } else {
            //sessione scaduta
            res.setStatus(401);
            out.println("Sessione scaduta");
            out.flush();
        }
    }

    public void destroy() {}
}
