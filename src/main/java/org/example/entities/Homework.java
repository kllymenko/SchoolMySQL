package org.example.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Homework {
    private int homeworkId;
    private int lessonId;
    private String description;
    private Timestamp dueDateTime;

}
