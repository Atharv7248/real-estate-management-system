package com.reindeer.trail.util;

import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/realestate_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ MySQL JDBC Driver not found!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ✅ Login by username and password
    public static String validateUser(String username, String password) {
        String role = null;
        String sql = "SELECT r.name FROM users u " +
                     "JOIN user_roles ur ON u.id = ur.user_id " +
                     "JOIN roles r ON ur.role_id = r.id " +
                     "WHERE u.username = ? AND u.password = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                role = rs.getString("name").toLowerCase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

    // ✅ Optional: fetch full user row (if needed)
    public static ResultSet getUserByUsernameAndPassword(String username, String password) throws SQLException {
        Connection conn = getConnection();
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        return stmt.executeQuery();
    }

    // ✅ Get role of a user by username
    public static String getUserRole(String username) throws SQLException {
        String role = null;
        String sql = "SELECT r.name FROM user_roles ur " +
                     "JOIN roles r ON ur.role_id = r.id " +
                     "JOIN users u ON ur.user_id = u.id " +
                     "WHERE u.username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                role = rs.getString("name");
            }
        }

        return role;
    }

    // ✅ Register a new user and assign a role (e.g., seller, buyer)
    public static boolean registerUser(String email, String password, String fullName, String roleName) {
        try (Connection conn = getConnection()) {
            // 1. Check if user already exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT id FROM users WHERE email = ?");
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) return false; // email already in use

            // 2. Insert user
            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO users (email, password, full_name, username) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, email);
            insertStmt.setString(2, password); // Optional: hash this
            insertStmt.setString(3, fullName);
            insertStmt.setString(4, email); // using email as username
            insertStmt.executeUpdate();

            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (!generatedKeys.next()) return false;
            int userId = generatedKeys.getInt(1);

            // 3. Get role ID
            PreparedStatement roleStmt = conn.prepareStatement("SELECT id FROM roles WHERE name = ?");
            roleStmt.setString(1, roleName);
            ResultSet roleRs = roleStmt.executeQuery();
            if (!roleRs.next()) return false;
            int roleId = roleRs.getInt("id");

            // 4. Link user with role
            PreparedStatement linkStmt = conn.prepareStatement(
                "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)");
            linkStmt.setInt(1, userId);
            linkStmt.setInt(2, roleId);
            linkStmt.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
