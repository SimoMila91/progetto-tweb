package controller;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

import DAO.DbManager;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        DbManager db = new DbManager();
        try {
            db.openConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        message = "BUCO DI CULO!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {

    }
}