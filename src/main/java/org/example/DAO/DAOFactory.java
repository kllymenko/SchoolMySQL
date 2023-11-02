package org.example.DAO;

import org.example.DAO.EntityDAO.*;
import org.example.DAO.EntityDAOImpl.*;

public class DAOFactory {
    public UserDAO getUserDAOInstance(MySQLDAOConfig config) {
        return new UserDAOImpl(config);
    }
    public SchoolClassDAO getClassDAOInstance(MySQLDAOConfig config) {
        return new SchoolClassDAOImpl(config);
    }
    public HomeworkDAO getHomeworkDAOInstance(MySQLDAOConfig config) {
        return new HomeworkDAOImpl(config);
    }
    public LessonDAO getLessonDAOInstance(MySQLDAOConfig config) {
        return new LessonDAOImpl(config);
    }
    public ScheduleDAO getScheduleDAOInstance(MySQLDAOConfig config) {
        return new ScheduleDAOImpl(config);
    }
}
