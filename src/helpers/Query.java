package helpers;

import java.sql.*;

import com.mysql.jdbc.Driver;


public class Query {

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "vereinskasse";
    private static final String USERNAME = "root";
    private static final String PASS = "1234";

    private static final String CONN_URL = "jdbc:mysql://localhost/"+DATABASE+"?allowMultiQueries=true";
    private static Connection conn;

    public static boolean isConnected() {
        return conn != null;
    }

    public static void connect() throws SQLException {
        if(isConnected()) return;
        conn = DriverManager.getConnection(CONN_URL, USERNAME, PASS);
    }

    public static void disconnect() throws SQLException {
        if(!isConnected()) return;
        conn.close();
    }

    public static void update(String query) throws SQLException {
        CallableStatement statement = conn.prepareCall(query);
        statement.execute();
    }

    public static ResultSet getSet(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

}
