package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionHandler {

    public static boolean checkSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        System.out.println(session);
        return session != null;
    }

    public static boolean checkIsAdmin(HttpSession session) {
        return session.getAttribute("role").equals(1);
    }
}
