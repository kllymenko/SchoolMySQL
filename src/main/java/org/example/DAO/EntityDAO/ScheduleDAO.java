package org.example.DAO.EntityDAO;

import org.example.DAO.CRUDRepository;
import org.example.entities.Homework;
import org.example.entities.Schedule;

import java.sql.Timestamp;

public interface ScheduleDAO extends CRUDRepository<Schedule> {
    public boolean updateGradeByUserAndLesson(int userId, int lessonId, int newGrade);


}
