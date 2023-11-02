package org.example.DAO.EntityDAO;

import org.example.DAO.CRUDRepository;
import org.example.entities.Lesson;
import org.example.entities.SchoolClass;

public interface SchoolClassDAO extends CRUDRepository<SchoolClass> {
    public boolean updateClassNumber(SchoolClass entity, int newClassNumber);
}
