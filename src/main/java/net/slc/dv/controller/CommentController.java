package net.slc.dv.controller;

import java.util.ArrayList;
import net.slc.dv.database.CommentQuery;
import net.slc.dv.model.ForumComment;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.TaskComment;

public class CommentController {

    private final CommentQuery commentQuery;

    public CommentController() {
        this.commentQuery = new CommentQuery();
    }

    public ForumComment createForumComment(String text, String forumid, String userid) {

        ForumComment forumComment = new ForumComment(text, userid, LoggedUser.getInstance(), forumid);

        return this.commentQuery.createForumComment(forumComment);
    }

    public ArrayList<ForumComment> getForumComments(String forumid, int offset) {
        return this.commentQuery.getForumComments(forumid, offset);
    }

    public TaskComment createTaskComment(String text, String taskid, String userid) {

        if (text == null || text.isEmpty()) return null;

        TaskComment taskComment = new TaskComment(text, userid, LoggedUser.getInstance(), taskid);

        return this.commentQuery.createTaskComment(taskComment);
    }

    public ArrayList<TaskComment> getTaskComments(String taskid) {
        return this.commentQuery.getTaskComments(taskid);
    }

    public ArrayList<TaskComment> getStudentTaskComments(String taskid) {
        return this.commentQuery.getStudentTaskComments(taskid);
    }

    public TaskComment replyComment(String text, String commentid) {

        if (text.isEmpty()) {
            return null;
        }

        TaskComment taskComment =
                new TaskComment(commentid, text, LoggedUser.getInstance().getId(), LoggedUser.getInstance());

        return this.commentQuery.replyComment(taskComment);
    }

    public ArrayList<TaskComment> getReplyTaskComment(String commentid) {
        return this.commentQuery.getReplyTaskComment(commentid);
    }
}
