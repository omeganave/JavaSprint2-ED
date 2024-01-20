// DONE

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url = "jdbc:postgresql://localhost:5432/JavaSprint2";
    private static final String user = "postgres";
    private static final String password = "Keyin2021";

    /**
     * Retrieves a connection object to the database.
     *
     * @return The connection object to the database.
     */
    public static Connection getCon() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
