import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {


    private static Connection connection;

    private static final String name = "postgres";

    private static final String password = "admin";

    public static Connection getConnection() throws SQLException {

        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", name, password);
        }

        return connection;
    }
}
