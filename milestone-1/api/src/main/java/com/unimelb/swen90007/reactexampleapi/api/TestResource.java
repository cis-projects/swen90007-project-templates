package com.unimelb.swen90007.reactexampleapi.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet(name = "test", urlPatterns = "/test")
public class TestResource extends HttpServlet {
    private static final String PROPERTY_JDBC_URI = "jdbc.uri";
    private static final String PROPERTY_JDBC_USERNAME = "jdbc.username";
    private static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";
    private static final String SQL_GET_TEST = "SELECT * FROM app.test;";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // open a connection and fetch the row from the 'test' table and return
        try (Connection connection = DriverManager.getConnection(
                System.getProperty(PROPERTY_JDBC_URI),
                System.getProperty(PROPERTY_JDBC_USERNAME),
                System.getProperty(PROPERTY_JDBC_PASSWORD)))
        {
            try (PreparedStatement statement =  connection.prepareStatement(SQL_GET_TEST)) {
                ResultSet results = statement.executeQuery();
                results.next();
                resp.getWriter().println(results.getString("test_value"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() throws ServletException {
        // load the driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        super.init();
    }
}
