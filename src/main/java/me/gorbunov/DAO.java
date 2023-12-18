package me.gorbunov;

import java.sql.*;
public class DAO {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/user/sqlite/graph.db";
    private Connection c = null;
    private Statement stmt = null;

    public void setConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(DB_URL);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM main_edges;");
            while (rs.next()) {
                int id = rs.getInt("id");
                int out_vertex = rs.getInt("out");
                int to_vertex = rs.getInt("to");
                float weigth = rs.getFloat("weigth");

                System.out.println("id - " + id);
                System.out.println("out_vertex - " + out_vertex);
                System.out.println("to_vertex - " + to_vertex);
                System.out.println("weigth - " + weigth);
                System.out.println("**********************");
        }
        rs.close();
        stmt.close();
        c.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
    }
        System.out.println("Operation done successfully");
    }
}
