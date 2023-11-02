package org.example.DAO.EntityDAO;

import org.example.DAO.CRUDRepository;
import org.example.entities.Homework;
import org.example.entities.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface HomeworkDAO extends CRUDRepository<Homework> {
    public boolean updateDueDateTime(Homework entity, Timestamp dueDateTime);
}
