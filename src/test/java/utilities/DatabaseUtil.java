package utilities;

import java.sql.*;
import java.util.HashMap;

public class DatabaseUtil {
    static Connection conn = null;
    Statement stmt = null;
    String DB_URL;
    String driver = null;
    ResultSet results = null;

    private String dbHostname, dbServerName, dbUserName, dbPassword, dbPort;

    public DatabaseUtil(String dbType, String host, String port, String serviceName, String user, String pass){

        this.dbHostname = host;
        this.dbServerName = serviceName;
        this.dbUserName = user;
        this.dbPassword = pass;
        this.dbPort = port;

        if(dbType.equalsIgnoreCase("Oracle")){
            this.DB_URL = "jdbc:oracle:thin:@" + host + ":" + port + "/" + serviceName;
            driver = "oracle.jdbc.driver.OracleDriver";
        }if(dbType.equalsIgnoreCase("Mysql")){
            this.DB_URL = "jdbc:mysql://" + host + ":" + port + "/" + serviceName;
            driver = "com.mysql.jdbc.Driver";
        }if(dbType.equalsIgnoreCase("Maria")){
            this.DB_URL = "jdbc:mariadb://" + host + ":" + port + "/" + serviceName;
            driver = "com.mariadb.jdbc.Driver";
        }if(dbType.equalsIgnoreCase("Postgres")){
            this.DB_URL = "jdbc:postgresql://" + host + ":" + port + "/" + serviceName;
            driver = "org.postgresql.Driver";
        }

        try{
            //Step 1: Register JDBC driver
            Class.forName(driver);

            //Step 2: Get connection DB
            conn = DriverManager.getConnection(DB_URL, user, pass);
            System.out.println("Connected to database successfully...........!");

            //Step 3: Statement Object to send the SQL statement to the Database
//            System.out.println("Creating Statement ......!");
            stmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ResultSet executeQueryGetResultSet(String queryWithReturn)
    {
        try{
            results = stmt.executeQuery(queryWithReturn);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return results;
    }

    public void executeUpdateInsertQuery(String query)
    {
        int updatedRows = 0;
        try{
            results = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        System.out.println(updatedRows + "  rows updated");
    }

    public void commitQuery()
    {
        try{
            results = stmt.executeQuery("commit");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        System.out.println("Commit query executed successfully");
    }

    public void closeDBConnection()
    {
        try{
            stmt.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        System.out.println("Database connection closed......!");
    }

    public String getDataByColumnIndex(ResultSet rs, int index)
    {
        String result = null;
        try{
            rs.next();
            result = rs.getString(index);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return result;
    }

    public String getDataByColumnName(ResultSet rs, String columnName)
    {
        String result = null;
        try{
            rs.next();
            result = rs.getString(columnName);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return result;
    }


    public HashMap<String, String> getAllDatatByQuery(String query)
    {
        HashMap<String, String> map = new HashMap<>();
        ResultSet rs = executeQueryGetResultSet(query);
        try{
            rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();
            int lengthOfTable = rsmd.getColumnCount();
            for (int i =1; i<= lengthOfTable; i++){
                String columnName = rsmd.getColumnName(i);
                map.put(columnName, rs.getString(columnName));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
