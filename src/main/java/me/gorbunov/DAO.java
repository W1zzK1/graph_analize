package me.gorbunov;

import me.gorbunov.dto.GraphVertex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/user/sqlite/graph.db";
    private Connection c = null;
    private Statement stmt = null;

    private ResultSet setConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(DB_URL);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            return stmt.executeQuery("SELECT * FROM main_edges;");
//            while (rs.next()) {
////                int id = rs.getInt("id");
////                int out_vertex = rs.getInt("out");
////                int to_vertex = rs.getInt("to");
////                float weigth = rs.getFloat("weigth");
////
////                System.out.println("id - " + id);
////                System.out.println("out_vertex - " + out_vertex);
////                System.out.println("to_vertex - " + to_vertex);
////                System.out.println("weigth - " + weigth);
////                System.out.println("**********************");
//        }
//        rs.close();
//        stmt.close();
//        c.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
    }
        System.out.println("Operation done successfully");
        return null;
    }

    public List<GraphVertex> getAllVertex() {
        List<GraphVertex> vertexList = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(DB_URL);
             Statement stmt = c.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT * FROM main_edges;")) {

            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int out_vertex = resultSet.getInt("out");
                int to_vertex = resultSet.getInt("to");
                float weigth = resultSet.getFloat("weigth");

                vertexList.add(new GraphVertex(
                        id,
                        out_vertex,
                        to_vertex,
                        weigth));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return vertexList;
    }
}
