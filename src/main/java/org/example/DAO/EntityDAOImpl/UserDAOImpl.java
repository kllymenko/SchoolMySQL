package org.example.DAO.EntityDAOImpl;

import org.example.DAO.EntityDAO.UserDAO;
import org.example.DAO.ConnectionManager;
import org.example.DAO.MySQLDAOConfig;
import org.example.entities.User;
import org.example.entities.enums.Role;
import org.example.entities.enums.Sex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static final String ADD_USER = "INSERT INTO school.user (name, surname, date_registration, phone, email, password, sex, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_USER_BY_ID = "SELECT * from school.user WHERE user_id=?";
    private static final String DELETE_USER_BY_ID = "CALL deleteUserAndSchedule(?)";
    private static final String GET_ALL_USERS = "SELECT * FROM school.user";
    private static final String UPDATE_PASSWORD = "UPDATE school.user SET password=? WHERE user_id=?";
    private final ConnectionManager connectionManager;

    public UserDAOImpl(MySQLDAOConfig config) {
        connectionManager = new ConnectionManager(config);
    }

    @Override
    public int insert(User entity) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
                int k = 0;
                ps.setString(++k, entity.getName());
                ps.setString(++k, entity.getSurname());
                ps.setTimestamp(++k, entity.getDate_registration());
                ps.setString(++k, entity.getPhone());
                ps.setString(++k, entity.getEmail());
                ps.setString(++k, entity.getPassword());
                ps.setString(++k, entity.getSex().toString());
                ps.setString(++k, entity.getRole().toString());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        entity.setUser_id(keys.getInt(1));
                    }
                }
            }
            return entity.getUser_id();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(User entity) {
        return false;
    }

    @Override
    public boolean updatePassword(User entity, String newPassword) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(UPDATE_PASSWORD)) {
                int k = 0;
                ps.setString(++k, newPassword);
                ps.setInt(++k, entity.getUser_id());
                int rowsUpdated = ps.executeUpdate();
                return rowsUpdated > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(DELETE_USER_BY_ID)) {
                int k = 0;
                ps.setInt(++k, id);
                int rowsDeleted = ps.executeUpdate();
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(GET_ALL_USERS)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        users.add(mapUsers(rs));
                    }
                    return users;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findById(int id) {
        User user = new User();
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(GET_USER_BY_ID)) {
                int k = 0;
                ps.setLong(++k, id);
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        user = mapUsers(resultSet);
                    }
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User mapUsers(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUser_id(rs.getInt("user_id"));
        u.setName(rs.getString("name"));
        u.setSurname(rs.getString("surname"));
        u.setDate_registration(rs.getTimestamp("date_registration"));
        u.setPhone(rs.getString("phone"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setSex(Sex.valueOf(rs.getString("sex").toUpperCase()));
        u.setRole(Role.valueOf(rs.getString("role").toUpperCase()));
        return u;
    }
}
