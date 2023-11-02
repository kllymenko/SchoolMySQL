package org.example.DAO.EntityDAOImpl;

import org.example.DAO.EntityDAO.SchoolClassDAO;
import org.example.DAO.ConnectionManager;
import org.example.DAO.MySQLDAOConfig;
import org.example.entities.Lesson;
import org.example.entities.SchoolClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SchoolClassDAOImpl implements SchoolClassDAO {
    private static final String ADD_CLASS = "INSERT INTO school.class (primary_school, floor, class_number) VALUES (?, ?, ?)";
    private static final String GET_CLASS_BY_ID = "SELECT * from school.class WHERE class_id=?";
    private static final String DELETE_CLASS_BY_ID = "DELETE FROM school.class WHERE class_id = ?";
    private static final String GET_ALL_CLASSES = "SELECT * FROM school.class";
    private static final String UPDATE_CLASS_NUMBER = "UPDATE school.class SET class_number=? WHERE class_id=?";
    private final ConnectionManager connectionManager;
    public SchoolClassDAOImpl(MySQLDAOConfig config) {
        connectionManager = new ConnectionManager(config);
    }

    @Override
    public int insert(SchoolClass entity) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(ADD_CLASS, Statement.RETURN_GENERATED_KEYS)) {
                int k = 0;
                ps.setBoolean(++k, entity.isPrimarySchool());
                ps.setInt(++k, entity.getFloor());
                ps.setInt(++k, entity.getClassNumber());
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
    public boolean update(SchoolClass entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(DELETE_CLASS_BY_ID)) {
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
    public List<SchoolClass> findAll() {
        List<SchoolClass> schoolClasses = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(GET_ALL_CLASSES)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        schoolClasses.add(mapClasses(rs));
                    }
                    return schoolClasses;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SchoolClass findById(int id) {
        SchoolClass schoolClass = new SchoolClass();
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(GET_CLASS_BY_ID)) {
                int k = 0;
                ps.setLong(++k, id);
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        schoolClass = mapClasses(resultSet);
                    }
                    return schoolClass;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateClassNumber(SchoolClass entity, int newClassNumber) {
        try (Connection con = connectionManager.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(UPDATE_CLASS_NUMBER)) {
                int k = 0;
                ps.setInt(++k, newClassNumber);
                ps.setInt(++k, entity.getClassId());
                int rowsUpdated = ps.executeUpdate();
                return rowsUpdated > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SchoolClass mapClasses(ResultSet rs) throws SQLException {
        SchoolClass s = new SchoolClass();
        s.setClassId(rs.getInt("class_id"));
        s.setPrimarySchool(rs.getBoolean("primary_school"));
        s.setFloor(rs.getInt("floor"));
        s.setClassNumber(rs.getInt("class_number"));
        return s;
    }

}
