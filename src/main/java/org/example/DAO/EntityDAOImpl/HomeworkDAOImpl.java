package org.example.DAO.EntityDAOImpl;

import org.example.DAO.EntityDAO.HomeworkDAO;
import org.example.DAO.ConnectionManager;
import org.example.DAO.MySQLDAOConfig;
import org.example.entities.Homework;
import org.example.entities.Lesson;
import org.example.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomeworkDAOImpl implements HomeworkDAO {
    private static final String ADD_HOMEWORK = "INSERT INTO school.homework (lesson_id, description, due_datetime) VALUES (?, ?, ?)";
    private static final String GET_HOMEWORK_BY_ID = "SELECT * from school.homework WHERE homework_id=?";
    private static final String DELETE_HOMEWORK_BY_ID = "DELETE FROM school.homework WHERE homework_id = ?";
    private static final String GET_ALL_HOMEWORKS = "SELECT * FROM school.homework";
    private static final String UPDATE_DUE_DATE_TIME = "UPDATE school.homework SET due_datetime=? WHERE homework_id=?";
    private final ConnectionManager connectionManager;
    public HomeworkDAOImpl(MySQLDAOConfig config) {
        connectionManager = new ConnectionManager(config);
    }

    @Override
    public int insert(Homework entity) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(ADD_HOMEWORK, Statement.RETURN_GENERATED_KEYS)) {
                int k = 0;
                ps.setInt(++k, entity.getLessonId());
                ps.setString(++k, entity.getDescription());
                ps.setTimestamp(++k, entity.getDueDateTime());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        entity.setHomeworkId(keys.getInt(1));
                    }
                }
            }
            return entity.getLessonId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Homework entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(DELETE_HOMEWORK_BY_ID)) {
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
    public List<Homework> findAll() {
        List<Homework> homeworks = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(GET_ALL_HOMEWORKS)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        homeworks.add(mapHomeworks(rs));
                    }
                    return homeworks;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Homework findById(int id) {
        Homework homework = new Homework();
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(GET_HOMEWORK_BY_ID)) {
                int k = 0;
                ps.setLong(++k, id);
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        homework = mapHomeworks(resultSet);
                    }
                    return homework;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateDueDateTime(Homework entity, Timestamp dueDateTime) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(UPDATE_DUE_DATE_TIME)) {
                int k = 0;
                ps.setTimestamp(++k, dueDateTime);
                ps.setInt(++k, entity.getHomeworkId());
                int rowsUpdated = ps.executeUpdate();
                return rowsUpdated > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Homework mapHomeworks(ResultSet rs) throws SQLException {
        Homework h = new Homework();
        h.setHomeworkId(rs.getInt("homework_id"));
        h.setLessonId(rs.getInt("lesson_id"));
        h.setDescription(rs.getString("description"));
        h.setDueDateTime(rs.getTimestamp("due_datetime"));
        return h;
    }
}
