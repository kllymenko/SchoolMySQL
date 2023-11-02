package org.example.entities;

import lombok.Data;
import org.example.entities.enums.Role;
import org.example.entities.enums.Sex;

import java.sql.Timestamp;
@Data
public class User {
    private int user_id;
    private String name;
    private String surname;
    private Timestamp date_registration;
    private String phone;
    private String email;
    private String password;
    private Sex sex;
    private Role role;

}
