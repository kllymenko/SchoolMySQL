package org.example;

import org.example.DAO.DAOFactory;
import org.example.DAO.EntityDAO.*;
import org.example.DAO.MySQLDAOConfig;
import org.example.entities.*;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.sql.Timestamp;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MySQLDAOConfig mySQLDAOConfig = new MySQLDAOConfig();
        mySQLDAOConfig.setType("MySQL");
        mySQLDAOConfig.setUrl("jdbc:mysql://localhost:3306/school?sslMode=DISABLED&serverTimezone=UTC");
        mySQLDAOConfig.setUser("root");
        mySQLDAOConfig.setPassword("root");

        DAOFactory daoFactory = new DAOFactory();

        // USER
        UserDAO userDAO = daoFactory.getUserDAOInstance(mySQLDAOConfig);

        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 6);
        EasyRandom generator = new EasyRandom(parameters);

        List<User> users = generator.objects(User.class, 10).toList();

        System.out.println("User data:");

        // insert and find
        for (User user : users) {
            int userId = userDAO.insert(user);
            User insertedUser = userDAO.findById(userId);
            System.out.println("Inserted User: " + insertedUser);
        }

        // findAll
        List<User> allUsers = userDAO.findAll();
        System.out.println("All Users: " + allUsers);

        //delete
        System.out.println("User delete result: " + userDAO.delete(allUsers.get(1).getUser_id()));

        // update
        User userToUpdate = users.get(2);
        userToUpdate.setPassword("newPassword");
        boolean updateResult = userDAO.updatePassword(userToUpdate, userToUpdate.getPassword());
        System.out.println("User update result: " + updateResult);
        User updatedUser = userDAO.findById(userToUpdate.getUser_id());
        System.out.println("Updated User: " + updatedUser);

        // SCHOOL CLASS
        SchoolClassDAO schoolClassDAO = daoFactory.getClassDAOInstance(mySQLDAOConfig);

        List<SchoolClass> schoolClasses = generator.objects(SchoolClass.class, 10).toList();

        System.out.println("School Class data:");

        // insert and find
        for (SchoolClass schoolClass : schoolClasses) {
            int classId = schoolClassDAO.insert(schoolClass);
            SchoolClass insertedClass = schoolClassDAO.findById(classId);
            System.out.println("Inserted School Class: " + insertedClass);
        }

        // findAll
        List<SchoolClass> allSchoolClasses = schoolClassDAO.findAll();
        System.out.println("All School Classes: " + allSchoolClasses);

        //delete
        System.out.println("Class delete result: " + schoolClassDAO.delete(allSchoolClasses.get(1).getClassId()));

        // update
        SchoolClass classToUpdate = schoolClasses.get(2);
        classToUpdate.setClassNumber(12345);
        boolean classUpdateResult = schoolClassDAO.updateClassNumber(classToUpdate, classToUpdate.getClassNumber());
        System.out.println("School Class update result: " + classUpdateResult);
        SchoolClass updatedClass = schoolClassDAO.findById(classToUpdate.getClassId());
        System.out.println("Updated School Class: " + updatedClass);

        // LESSON
        LessonDAO lessonDAO = daoFactory.getLessonDAOInstance(mySQLDAOConfig);

        List<Lesson> lessons = generator.objects(Lesson.class, 10).toList();

        System.out.println("Lesson data:");

        // insert and find
        for (Lesson lesson : lessons) {
            int lessonId = lessonDAO.insert(lesson);
            Lesson insertedLesson = lessonDAO.findById(lessonId);
            System.out.println("Inserted Lesson: " + insertedLesson);
        }

        // delete
        Lesson lessonToDelete = lessons.get(1);
        boolean deleteResult = lessonDAO.delete(lessonToDelete.getLessonId());
        System.out.println("Lesson delete result: " + deleteResult);

        // findAll
        List<Lesson> allLessons = lessonDAO.findAll();
        System.out.println("All Lessons: " + allLessons);

        // update
        Lesson lessonToUpdate = lessons.get(2);
        lessonToUpdate.setName("newName");
        boolean lessonUpdateResult = lessonDAO.updateName(lessonToUpdate, lessonToUpdate.getName());
        System.out.println("Lesson update result: " + lessonUpdateResult);
        Lesson updatedLesson = lessonDAO.findById(lessonToUpdate.getLessonId());
        System.out.println("Updated Lesson: " + updatedLesson);

        // HOMEWORK
        HomeworkDAO homeworkDAO = daoFactory.getHomeworkDAOInstance(mySQLDAOConfig);

        Homework homework1 = new Homework(1, lessons.get(2).getLessonId(), generator.nextObject(String.class), new Timestamp(System.currentTimeMillis()));
        Homework homework2 = new Homework(2, lessons.get(3).getLessonId(), generator.nextObject(String.class), new Timestamp(System.currentTimeMillis()));
        Homework homework3 = new Homework(3, lessons.get(4).getLessonId(), generator.nextObject(String.class), new Timestamp(System.currentTimeMillis()));

        System.out.println("Homework data:");

        // insert and find
        homeworkDAO.insert(homework1);
        homeworkDAO.insert(homework2);
        homeworkDAO.insert(homework3);

        // delete
        System.out.println("Homework delete result: " + homeworkDAO.delete(homework1.getHomeworkId()));

        // findAll
        List<Homework> homeworks = homeworkDAO.findAll();
        System.out.println("All Homeworks: " + homeworks);

        // update
        System.out.println("Homework update result: " + homeworkDAO.updateDueDateTime(homework2, new Timestamp(System.currentTimeMillis())));
        Homework updatedHomework = homeworkDAO.findById(homework2.getHomeworkId());
        System.out.println("Updated Homework: " + updatedHomework);

        // SCHEDULE
        ScheduleDAO scheduleDAO = daoFactory.getScheduleDAOInstance(mySQLDAOConfig);

        Schedule schedule1 = new Schedule(users.get(4).getUser_id(), lessons.get(2).getLessonId(), schoolClasses.get(4).getClassId(), 1, true);
        Schedule schedule2 = new Schedule(users.get(5).getUser_id(), lessons.get(3).getLessonId(), schoolClasses.get(5).getClassId(), 2, false);
        Schedule schedule3 = new Schedule(users.get(6).getUser_id(), lessons.get(4).getLessonId(), schoolClasses.get(6).getClassId(), 3, true);

        System.out.println("Schedule data:");

        // insert and find
        scheduleDAO.insert(schedule1);
        scheduleDAO.insert(schedule2);
        scheduleDAO.insert(schedule3);

        // delete
        boolean scheduleDeleteResult = scheduleDAO.delete(schedule1.getUserId());
        System.out.println("Schedule delete result: " + scheduleDeleteResult);

        // findAll
        List<Schedule> schedules = scheduleDAO.findAll();
        System.out.println("All Schedules: " + schedules);

        // update
        Schedule scheduleToUpdate = schedule2;
        scheduleToUpdate.setGrade(7);
        boolean scheduleUpdateResult = scheduleDAO.updateGradeByUserAndLesson(scheduleToUpdate.getUserId(), scheduleToUpdate.getLessonId(), scheduleToUpdate.getGrade());
        System.out.println("Schedule update result: " + scheduleUpdateResult);
        Schedule updatedSchedule = scheduleDAO.findById(scheduleToUpdate.getUserId());
        System.out.println("Updated Schedule: " + updatedSchedule);


        System.out.println("Розклад:");

        List<Schedule> schedules1 = scheduleDAO.findAll();

        for (Schedule schedule : schedules1) {
            int userId = schedule.getUserId();
            int lessonId = schedule.getLessonId();
            int classId = schedule.getClassId();
            int grade = schedule.getGrade();

            // Отримайте додаткову інформацію з інших таблиць
            User user = userDAO.findById(userId);
            Lesson lesson = lessonDAO.findById(lessonId);
            SchoolClass schoolClass = schoolClassDAO.findById(classId);

            // Знайдіть домашнє завдання вручну
            Homework homework = findHomeworkByLessonId(homeworkDAO, lessonId);

            // Виведіть всю необхідну інформацію
            System.out.println("Учень: " + user.getName());
            System.out.println("Урок: " + lesson.getName());
            System.out.println("Клас: " + schoolClass.getClassNumber());
            System.out.println("Дата: " + lesson.getDate());
            System.out.println("Оцінка: " + grade);
            if (homework != null) {
                System.out.println("Домашнє завдання: " + homework.getDescription());
            } else {
                System.out.println("Домашнє завдання відсутнє");
            }

            System.out.println();
        }
    }
        private static Homework findHomeworkByLessonId(HomeworkDAO homeworkDAO, int lessonId) {
            List<Homework> allHomeworks = homeworkDAO.findAll();
            for (Homework homework : allHomeworks) {
                if (homework.getLessonId() == lessonId) {
                    return homework;
                }
            }
            return null;
        }
}
