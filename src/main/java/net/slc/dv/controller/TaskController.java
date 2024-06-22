package net.slc.dv.controller;

import java.util.ArrayList;
import java.util.List;
import net.slc.dv.database.TaskQuery;
import net.slc.dv.enums.TaskType;
import net.slc.dv.model.*;

public class TaskController {

    private final TaskQuery taskQuery;

    public TaskController() {
        this.taskQuery = new TaskQuery();
    }

    public void createFileTask(String title, String description, String deadlineAt, boolean scored, String classId) {
        if (title.isEmpty() || description.isEmpty() || deadlineAt.isEmpty()) {
            return;
        }

        Task newTask = new Task(LoggedUser.getInstance(), title, description, deadlineAt, scored, TaskType.FILE);

        this.taskQuery.createFileTask(newTask, classId);
    }

    public void createQuestionTask(
            List<Question> questions,
            String title,
            String description,
            String deadlineAt,
            boolean scored,
            String classId) {
        if (questions.isEmpty() || deadlineAt.isEmpty()) {
            return;
        }

        Task newTask = new Task(LoggedUser.getInstance(), title, description, deadlineAt, scored, TaskType.QUESTION);

        questions.forEach(question -> question.setTaskID(newTask.getId()));

        this.taskQuery.createQuestionTask(newTask, questions, classId);
    }

    public ArrayList<Task> getClassroomTask(String classid) {
        return taskQuery.getClassroomTask(classid);
    }

    public ArrayList<Task> getScoredClassroomTask(String classid) {
        return taskQuery.getScoredClassroomTask(classid);
    }

    public ArrayList<Task> fetchTaskByDate(String date) {
        return taskQuery.fetchTaskByDate(date);
    }

    public ArrayList<Task> fetchTaskByDate(int day, int month, int year) {
        String date = year + "-" + month + "-" + day;
        return taskQuery.fetchTaskByDate(date);
    }

    public ArrayList<Task> fetchUserPendingTask(String userid) {
        return taskQuery.fetchUserPendingTask(userid);
    }

    public ArrayList<Task> fetchUserFinishedTask(String userid) {
        return taskQuery.fetchUserFinishedTask(userid);
    }

    public ArrayList<Task> fetchClassroomPendingTask(String classid) {
        return taskQuery.fetchClassroomPendingTask(classid);
    }

    public List<Question> fetchQuestion(String taskid) {
        return taskQuery.fetchQuestions(taskid);
    }
}
