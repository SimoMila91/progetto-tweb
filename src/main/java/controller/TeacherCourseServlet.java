package controller;

import DAO.TeacherCourseDAO;
import DAO.TeacherDAO;
import DTO.TeacherCourseDTO;
import com.google.gson.Gson;
import entities.Teacher;
import entities.TeacherCourse;
import org.json.JSONObject;
import utils.JsonUtils;
import utils.SessionHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

@WebServlet(name = "TeacherCourseServlet", value = "/TeacherCourseServlet")
public class TeacherCourseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        String action = "";
        PrintWriter out = response.getWriter();
        JSONObject res = new JSONObject();

        if (request.getParameter("action") != null) {
            action = request.getParameter("action");


            switch (action) {
                case "getTeacherCourses":
                    int idTeacher = 0;

                    if (request.getParameter("idTeacher") != null) {
                        idTeacher = Integer.parseInt(request.getParameter("idTeacher"));
                        try {
                            ArrayList<TeacherCourseDTO> getTeacherCourses = TeacherCourseDAO.getTeacherCourses(idTeacher);
                            System.out.println(getTeacherCourses);
                            res.put("courses", getTeacherCourses);
                            System.out.println(res);
                        } catch (SQLException e) {
                           System.out.println(e.getMessage());
                           response.setStatus(500);
                           res.put("message", "Errore nel sistema. Ritenta più tardi o contatta il supporto");
                        }
                    } else {
                        response.setStatus(404);
                        res.put("message", "Errore nella richiesta");
                    }
                    break;
                case "getCourseTeachers":
                    int idCourse = 0;
                    if (request.getParameter("idCourse") != null) {
                        idCourse = Integer.parseInt(request.getParameter("idCourse"));
                        try {
                            ArrayList<Teacher> getCourseTeachers = TeacherCourseDAO.getCourseTeachers(idCourse);
                            res.put("teachers",getCourseTeachers);
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                            response.setStatus(500);
                            res.put("message", "Errore nel sistema. Ritenta più tardi o contatta il supporto");
                        }
                    } else {
                        response.setStatus(404);
                        res.put("message", "Errore nella richiesta");
                    }
                    break;
                default:
                    response.setStatus(404);
                    res.put("message", "Errore nella richiesta");
            }

        } else {
            response.setStatus(404);
            res.put("message", "Errore nella richiesta");
        }
        out.println(res);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (SessionHandler.checkSession(request) && SessionHandler.checkIsAdmin(request.getSession(false))) {
                processRequest(request, response);
            } else {
                response.setStatus(401);
                response.getWriter().println("Non sei autorizzato");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            response.setStatus(500);
            response.getWriter().println("Errore nel sistema. Ritenta più tardi o contatta il supporto");
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        JSONObject req = JsonUtils.readJson(request);
        PrintWriter out = response.getWriter();
        JSONObject res = new JSONObject();

        if (req.has("action")) {
            switch (req.getString("action")) {
                case "create":
                    int created = TeacherCourseDAO.createTeaching(req.getInt("idTeacher"), req.getInt("idCourse"));
                    if (created > 0) {
                        res.put("message", "Corso-Docente creato con successo");
                        res.put("idCourseTeaching", created);
                    } else {
                        response.setStatus(500);
                        res.put("message", "Errore nella creazione. Contattare il supporto");
                    }
                    break;
                case "delete":
                    boolean deleted = TeacherCourseDAO.deleteTeaching(req.getInt("idCourseTeacher"));
                    if (deleted) {
                        res.put("message", "Corso-Docente eliminato correttamente");
                    } else {
                        response.setStatus(500);
                        res.put("message", "Errore nell'eliminazione. Contattare il supporto");
                    }
            }
        } else {
            response.setStatus(403);
            res.put("message", "Errore nella richiesta");
        }
        out.println(res);
        out.flush();
    }

    @Override
    public void destroy() {}
}
