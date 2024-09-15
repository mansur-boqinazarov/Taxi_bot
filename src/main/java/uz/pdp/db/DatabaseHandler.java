package uz.pdp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/tg_db_3";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";

    public static void storeUsername(String username) {
        String insertQuery = "INSERT INTO drivers_username(username) VALUES(?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isDriver(String username) {
        String selectQuery = "SELECT COUNT(*) FROM drivers_username WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Add this method to the DatabaseHandler class
    public static void deleteDriver(String username) {
        String deleteQuery = "DELETE FROM drivers_username WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, username);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Driver deleted successfully.");
            } else {
                System.out.println("Driver not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
