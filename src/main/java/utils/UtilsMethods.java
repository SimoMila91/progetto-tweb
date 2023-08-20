package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilsMethods {

    public static int countRows(ResultSet rs) {
        int response = 0;
        while(true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            response++;
        }
        return response;
    }
}
