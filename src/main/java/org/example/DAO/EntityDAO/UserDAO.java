package org.example.DAO.EntityDAO;

import org.example.DAO.CRUDRepository;
import org.example.entities.User;

public interface UserDAO extends CRUDRepository<User> {
    public boolean updatePassword(User entity, String newPassword);
}
