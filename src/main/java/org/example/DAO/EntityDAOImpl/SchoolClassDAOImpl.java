package org.example.DAO.EntityDAOImpl;

import org.example.DAO.EntityDAO.ClassDAO;
import org.example.DAO.ConnectionManager;
import org.example.DAO.MySQLDAOConfig;

import java.util.List;

public class ClassDAOImpl implements ClassDAO {
    private final ConnectionManager connectionManager;
    public ClassDAOImpl(MySQLDAOConfig config) {
        connectionManager = new ConnectionManager(config);
    }

    @Override
    public int insert(Class entity) {
        return 0;
    }

    @Override
    public boolean update(Class entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<Class> findAll() {
        return null;
    }

    @Override
    public Class findById(int id) {
        return null;
    }
}
