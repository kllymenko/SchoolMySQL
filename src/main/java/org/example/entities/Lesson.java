package org.example.entities;

import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
@Data
public class Lesson {
    private int lessonId;
    private String name;
    private LocalDate date;
    private Time timeStart;
    private Time timeEnd;

}
