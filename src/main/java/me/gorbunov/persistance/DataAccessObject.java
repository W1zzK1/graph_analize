package me.gorbunov.persistance;

import java.sql.Connection;

public interface DataAccessObject {
    Connection getConnection();
}
