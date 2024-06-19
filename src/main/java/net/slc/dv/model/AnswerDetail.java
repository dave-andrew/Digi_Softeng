package net.slc.dv.model;

import java.sql.ResultSet;
import lombok.Data;

@Data
public class AnswerDetail {
    private String answerId;
    private String questionId;
    private String answerText;
    private Double answerScore;

    public AnswerDetail(String answerId, String questionId, String answerText, Double answerScore) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.answerText = answerText;
        this.answerScore = answerScore;
    }

    public AnswerDetail(String questionId, String answerText, Double answerScore) {
        this("", questionId, answerText, answerScore);
    }

    public AnswerDetail(ResultSet set) {
        try {
            this.answerId = set.getString("AnswerID");
            this.questionId = set.getString("QuestionID");
            this.answerText = set.getString("AnswerText");
            this.answerScore = set.getDouble("Score");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
