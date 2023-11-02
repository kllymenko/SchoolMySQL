package org.example.DAO.EntityDAOImpl;

import org.example.DAO.EntityDAO.LessonDAO;
import org.example.DAO.ConnectionManager;
import org.example.DAO.MySQLDAOConfig;
import org.example.entities.Lesson;
import org.example.entities.User;
import org.example.entities.enums.Role;
import org.example.entities.enums.Sex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LessonDAOImpl implements LessonDAO {
    private static final String ADD_LESSON = "INSERT INTO school.lesson (name, date, time_start, time_end) VALUES (?, ?, ?, ?)";
    private static final String GET_LESSON_BY_ID = "SELECT * from school.lesson WHERE lesson_id=?";
    private static final String DELETE_LESSON_BY_ID = "DELETE FROM school.lesson WHERE lesson_id=?";
    private static final String GET_ALL_LESSONS = "SELECT * FROM school.lesson";
    private static final String UPDATE_NAME = "UPDATE school.lesson SET name=? WHERE lesson_id=?";
    private final ConnectionManager connectionManager;
    public LessonDAOImpl(MySQLDAOConfig config) {
        connectionManager = new ConnectionManager(config);
    }

    @Override
    public int insert(Lesson entity) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(ADD_LESSON, Statement.RETURN_GENERATED_KEYS)) {
                int k = 0;
                ps.setString(++k, entity.getName());
                ps.setDate(++k, Date.valueOf(entity.getDate()));
                ps.setTime(++k, entity.getTimeStart());
                ps.setTime(++k, entity.getTimeEnd());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        entity.setLessonId(keys.getInt(1));
                    }
                }
            }
            return entity.getLessonId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Lesson entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(DELETE_LESSON_BY_ID)) {
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
    public List<Lesson> findAll() {
        List<Lesson> lessons = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(GET_ALL_LESSONS)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lessons.add(mapLessons(rs));
                    }
                    return lessons;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Lesson findById(int id) {
        Lesson lesson = new Lesson();
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(GET_LESSON_BY_ID)) {
                int k = 0;
                ps.setLong(++k, id);
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        lesson = mapLessons(resultSet);
                    }
                    return lesson;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateName(Lesson entity, String newName) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(UPDATE_NAME)) {
                int k = 0;
                ps.setString(++k, newName);
                ps.setInt(++k, entity.getLessonId());
                int rowsUpdated = ps.executeUpdate();
                return rowsUpdated > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Lesson mapLessons(ResultSet rs) throws SQLException {
        Lesson l = new Lesson();
        l.setLessonId(rs.getInt("lesson_id"));
        l.setName(rs.getString("name"));
        l.setDate(rs.getDate("date").toLocalDate());
        l.setTimeStart(rs.getTime("time_start"));
        l.setTimeEnd(rs.getTime("time_end"));
        return l;
    }
}
