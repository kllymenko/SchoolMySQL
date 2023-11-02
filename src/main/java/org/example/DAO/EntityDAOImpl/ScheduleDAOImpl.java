package org.example.DAO.EntityDAOImpl;

import org.example.DAO.EntityDAO.ScheduleDAO;
import org.example.DAO.ConnectionManager;
import org.example.DAO.MySQLDAOConfig;
import org.example.entities.Schedule;
import org.example.entities.SchoolClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAOImpl implements ScheduleDAO {
    private static final String ADD_SCHEDULE = "INSERT INTO school.schedule (user_id, lesson_id, class_id, grade, is_present) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_SCHEDULE_BY_USER_ID = "SELECT * from school.schedule WHERE user_id=?";
    private static final String DELETE_SCHEDULE_BY_USER_ID = "DELETE FROM school.schedule WHERE user_id = ?";
    private static final String GET_ALL_SCHEDULE = "SELECT * FROM school.schedule";
    private static final String UPDATE_GRADE_BY_USER_AND_LESSON = "UPDATE school.schedule SET grade=? WHERE user_id=? AND lesson_id=?";
    private final ConnectionManager connectionManager;
    public ScheduleDAOImpl(MySQLDAOConfig config) {
        connectionManager = new ConnectionManager(config);
    }

    @Override
    public int insert(Schedule entity) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(ADD_SCHEDULE, Statement.RETURN_GENERATED_KEYS)) {
                int k = 0;
                ps.setInt(++k, entity.getUserId());
                ps.setInt(++k, entity.getLessonId());
                ps.setInt(++k, entity.getClassId());
                ps.setInt(++k, entity.getGrade());
                ps.setBoolean(++k, entity.isPresent());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        entity.setClassId(keys.getInt(1));
                    }
                }
            }
            return entity.getClassId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Schedule entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(DELETE_SCHEDULE_BY_USER_ID)) {
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
    public List<Schedule> findAll() {
        List<Schedule> schedules = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(GET_ALL_SCHEDULE)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        schedules.add(mapSchedules(rs));
                    }
                    return schedules;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Schedule findById(int id) {
        Schedule schedule = new Schedule();
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(GET_SCHEDULE_BY_USER_ID)) {
                int k = 0;
                ps.setLong(++k, id);
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        schedule = mapSchedules(resultSet);
                    }
                    return schedule;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateGradeByUserAndLesson(int userId, int lessonId, int newGrade) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(UPDATE_GRADE_BY_USER_AND_LESSON)) {
                int k = 0;
                ps.setInt(++k, newGrade);
                ps.setInt(++k, userId);
                ps.setInt(++k, lessonId);
                int rowsUpdated = ps.executeUpdate();
                return rowsUpdated > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Schedule mapSchedules(ResultSet rs) throws SQLException {
        Schedule s = new Schedule();
        s.setUserId(rs.getInt("user_id"));
        s.setLessonId(rs.getInt("lesson_id"));
        s.setClassId(rs.getInt("class_id"));
        s.setGrade(rs.getInt("grade"));
        s.setPresent(rs.getBoolean("is_present"));
        return s;
    }


}
