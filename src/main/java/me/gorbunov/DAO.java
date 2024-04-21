package me.gorbunov;

import me.gorbunov.dto.GraphVertex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/test";
    private static final String DB_USER = "test";
    private static final String DB_PASSWORD = "test";

    public List<GraphVertex> getAllVertex() {
        List<GraphVertex> vertexList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM main_edges;")) {

            connection.setAutoCommit(false);
            System.out.println("База данных успешно открыта");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int out_vertex = resultSet.getInt("out");
                int to_vertex = resultSet.getInt("too");
                float weight = resultSet.getFloat("weight");

                vertexList.add(new GraphVertex(id, out_vertex, to_vertex, weight));
            }
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return vertexList;
    }
}