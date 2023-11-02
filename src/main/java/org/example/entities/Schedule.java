package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private int userId;
    private int lessonId;
    private int classId;
    private int grade;
    private boolean isPresent;

}
