package com.logme;

import com.logme.file.LogMe;

import java.sql.*;

public class Database {
    final static String url = "jdbc:sqlite:/tmp/logMe.db";
    public Connection connection;

    public Database() throws SQLException {
        this.connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table if not exists logs (id integer primary key, content text, date text, level text, type text)");
    }

    public void insert(LogMe.AddLogRequest request) throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        String insertQuery = "insert into logs (content, date, level, type) values (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, request.getContent());
            preparedStatement.setString(2, request.getDate());
            preparedStatement.setString(3, request.getLevel());
            preparedStatement.setInt(4, request.getType().getNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cannot insert log at this time.");
            System.exit(0);
        }
    }
}
