package com.trustmarco.bewerbungsplugin.mysql;

import java.sql.*;

public class MySQL {
    public static String HOST = "";
    public static String DATABASE = "";
    public static String PORT = "3306";
    public static String USER = "";
    public static String PASSWORD = "";

    public static Connection con;



    public MySQL(String host, String database, String user, String password) {
        HOST = host;
        DATABASE = database;
        USER = user;
        PASSWORD = password;

        connect();
    }

    public static void connect() {
        System.out.println("Connect to MySQL...");
        try {
            con = (Connection) DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?autoReconnect=true", USER, PASSWORD);
            System.out.println("Connection to MySQL!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot connect to MySQL!");
        }
    }

    public static boolean isConnected() {
        if (con == null)
            connect();

        if (con != null) {
            return true;
        } else {
            return false;
        }

    }

    public static void close() {
        try {
            if (isConnected()) {
                con.close();
                System.out.println("MySQL Connection was successfully closed.");
            }
        } catch (SQLException localSQLException) {
        }
    }

    public static void update(String qry) {
        if (isConnected())
            try {
                Statement st = con.createStatement();

                st.executeUpdate(qry);
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static ResultSet query(String qry) {
        ResultSet rs = null;
        try {
            Statement st = con.createStatement();
            rs = st.executeQuery(qry);
        } catch (SQLException e) {
            connect();
            System.err.println(e);
        }
        return rs;
    }

    public void createTable() {
        if (isConnected()) {
            try {
                PreparedStatement preparedStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS points (UUID VARCHAR(100), POINTS TEXT(500000000))");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
