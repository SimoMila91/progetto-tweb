package controller;

import DAO.UserDAO;
import entities.User;
import org.json.JSONObject;
import utils.JsonUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "UserServlet", value = "/UserServlet")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        JSONObject jsonObject = JsonUtils.readJson(req);
        String action = jsonObject.getString("action");

        switch (action) {
            case "login":
                if (!jsonObject.optString("email").isEmpty() && !jsonObject.optString("psw").isEmpty()) {
                    User user = UserDAO.login(jsonObject.getString("email"), jsonObject.getString("psw"));
                    if (user != null) {
                        HttpSession session = req.getSession();
                        session.setAttribute("idUser", user.getIdUser());
                        session.setAttribute("role", user.getRole());
                        System.out.println(session.getMaxInactiveInterval());
                        JSONObject response = new JSONObject();
                        response.put("idUser", user.getIdUser());
                        response.put("name", user.getName());
                        response.put("role", user.getRole());
                        response.put("token", session.getId());
                        response.put("message", "Autenticazione avvenuta con successo");
                        res.setStatus(200);
                        out.println(response);
                        out.flush();
                    } else {
                        res.setStatus(401);
                        JSONObject response = new JSONObject();
                        response.put("message", "L'email o la password non sono corretti");
                        out.println(response);
                        out.flush();
                    }
                }
                break;
            case "signUp":
                JSONObject response = new JSONObject();
                String email = jsonObject.optString("email");
                String psw = jsonObject.optString("psw");
                String surname = jsonObject.optString("surname");
                String name = jsonObject.optString("name");

                if (!email.isEmpty() && !name.isEmpty() && !surname.isEmpty() && psw.length() >= 6) {
                    User user = new User(
                            name,
                            surname,
                            email,
                            psw
                    );
                    User insert = UserDAO.signUp(user);
                    if (insert != null) {
                        HttpSession session = req.getSession();
                        session.setAttribute("idUser", insert.getIdUser());
                        session.setAttribute("role", insert.getRole());
                        res.setStatus(200);
                        response.put("idUser", insert.getIdUser());
                        response.put("name", insert.getName());
                        response.put("role", insert.getRole());
                        response.put("token", session.getId());
                        response.put("message", "Registrazione avvenuta con successo");
                        out.println(response);
                        out.flush();
                    } else {
                        res.setStatus(500);
                        out.println("Errore interno al server, ritenta la registrazione più tardi o contatta il supporto");
                        out.flush();
                    }

                    response.put("message", "Registrazione avvenuta con successo");
                } else {
                    res.setStatus(400);
                    out.println("Tutti i campi sono obbligatori per la registrazione");
                    out.flush();
                }
                break;
            case "logout":
                HttpSession session = req.getSession(false);
                session.invalidate();
                break;
            default:
                res.setStatus(404);
                out.println("Errore nella richiesta");
                out.flush();
                break;
        }


    }

    public void destroy() {
    }
}
