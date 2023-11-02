package org.example.entities.enums;

public enum Role {
    STUDENT("Student"),
    HEADTEACHER("Headteacher");
    private String role;

    Role(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
