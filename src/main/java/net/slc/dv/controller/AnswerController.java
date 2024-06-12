package net.slc.dv.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import net.slc.dv.database.AnswerQuery;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.AnswerDetail;
import net.slc.dv.model.AnswerHeader;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;
import org.jetbrains.annotations.Nullable;

public class AnswerController {

    private final AnswerQuery answerQuery;

    public AnswerController() {
        this.answerQuery = new AnswerQuery();
    }

    public ArrayList<File> getMemberFileAnswer(String taskid, String userid) {
        return answerQuery.getMemberFileAnswer(taskid, userid);
    }

    public List<AnswerDetail> getMemberQuestionAnswer(String taskid, String userid) {
        return answerQuery.getMemberQuestionAnswer(taskid, userid);
    }

    public Double getAnswerScore(String taskid, String userid) {
        return answerQuery.getAnswerScore(taskid, userid);
    }

    public boolean checkAnswer(String taskid, String userid) {
        return answerQuery.checkAnswer(taskid, userid);
    }

    public void downloadAnswer(File file) {
        String userHome = System.getProperty("user.home");
        Path downloadsDirectoryPath = Paths.get(userHome, "Downloads");

        try {
            Files.createDirectories(downloadsDirectoryPath);

            Path targetFilePath = downloadsDirectoryPath.resolve(file.getName()).toAbsolutePath();

            Files.copy(file.toPath(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);

            ToastBuilder.buildNormal().setText("File Downloaded!").build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadAllAnswer(ArrayList<File> fileList) {
        String userHome = System.getProperty("user.home");
        Path downloadsDirectoryPath = Paths.get(userHome, "Downloads");

        try {
            Files.createDirectories(downloadsDirectoryPath);

            for (File file : fileList) {
                Path targetFilePath =
                        downloadsDirectoryPath.resolve(file.getName()).toAbsolutePath();

                Files.copy(file.toPath(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            ToastBuilder.buildNormal()
                    .setText(TextStorage.getText(Text.FILES_DOWNLOADED_SUCCESSFULLY))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void markAsDone(String taskid, String userid) {
        answerQuery.markAsDone(taskid, userid);
    }

    public void markUndone(String taskid, String userid) {
        answerQuery.markUndone(taskid, userid);
    }

    public boolean scoreQuestionAnswer(String taskid, String userid, String score) {
        if (score.isEmpty()) {
            ToastBuilder.buildNormal()
                    .setText(TextStorage.getText(Text.SCORE_CANNOT_BE_EMPTY))
                    .build();
            return false;
        }

        if (!score.matches("[0-9]+")) {
            ToastBuilder.buildNormal()
                    .setText(TextStorage.getText(Text.SCORE_MUST_BE_A_NUMBER))
                    .build();
            return false;
        }

        if (Integer.parseInt(score) < 0 || Integer.parseInt(score) > 100) {
            ToastBuilder.buildNormal()
                    .setText(TextStorage.getText(Text.SCORE_MUST_BE_BETWEEN_0_AND_100))
                    .build();
            return false;
        }

        return answerQuery.scoreAnswer(taskid, userid, Double.parseDouble(score));
    }

    public AnswerHeader saveAnswer(
            @Nullable AnswerHeader answerHeader, String taskid, String userid, List<AnswerDetail> answerList) {
        if (answerHeader == null) {
            answerHeader = new AnswerHeader(taskid, userid, false, null, null);
        }

        AnswerHeader finalAnswerHeader = answerHeader;
        answerList.forEach(answerDetail -> answerDetail.setAnswerId(finalAnswerHeader.getId()));

        answerQuery.createAnswerHeader(answerHeader);
        answerQuery.createAnswerDetails(answerList);

        return answerHeader;
    }

    public void submitTest(AnswerHeader answerHeader) {
        answerQuery.submitTest(answerHeader);
    }

    @Nullable
    public AnswerHeader fetchAnswerHeader(String taskid, String userid) {
        return answerQuery.getAnswerHeader(taskid, userid);
    }

    public boolean checkTest(String taskid, String userid) {
        return answerQuery.checkTest(taskid, userid);
    }

    public List<AnswerDetail> fetchAnswerDetails(AnswerHeader answerHeader) {
        try {
            return answerQuery.getAnswerDetails(answerHeader.getId());
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    public void scoreQuestionAnswer(List<AnswerDetail> answerDetails) {
        answerQuery.scoreQuestionAnswer(answerDetails);
    }

    public void finishScoring(String taskId, String userId, String answerid, Double score) {
        answerQuery.finishScoring(taskId, userId, answerid, score);
    }
}
