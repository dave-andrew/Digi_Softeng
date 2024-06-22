package net.slc.dv.view.classroom.detail.component;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.controller.AnswerController;
import net.slc.dv.controller.MemberController;
import net.slc.dv.controller.TaskController;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.ClassroomMember;
import net.slc.dv.model.Task;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.IconStorage;
import net.slc.dv.storage.TextStorage;

public class ClassScore extends HBox {

    private final Classroom classroom;

    private MemberController memberController;
    private TaskController taskController;
    private AnswerController answerController;
    private VBox scoreContainer;
    private VBox memberList;
    private int idx = 1;
    private ClassroomMember selectedMember;

    public ClassScore(Classroom classroom) {
        this.classroom = classroom;
        init();

        setLayout();
    }

    private void init() {
        this.memberController = new MemberController();
        this.taskController = new TaskController();
        this.answerController = new AnswerController();

        ScrollPane members = new ScrollPane();
        members.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        members.setFitToWidth(true);
        members.setPannable(true);

        VBox memberContainer = new VBox();
        memberContainer.setPrefWidth(450);

        this.scoreContainer = new VBox();
        this.scoreContainer.setAlignment(Pos.TOP_CENTER);

        ScrollPane taskScores = new ScrollPane();
        taskScores.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        taskScores.setFitToWidth(true);
        taskScores.setPannable(true);

        this.memberList = new VBox();
        this.memberList.setPadding(new Insets(20));
        memberContainer.setStyle("-fx-border-color: transparent #e0e0e0 transparent transparent;");

        members.setContent(memberList);
        memberContainer.getChildren().add(members);

        members.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        this.getChildren().addAll(memberContainer, scoreContainer);
    }

    private void setLayout() {
        fetchMember();
    }

    private void fetchMember() {
        this.memberList.getChildren().clear();

        ArrayList<ClassroomMember> memberList = this.memberController.getClassMember(classroom.getClassId());

        for (ClassroomMember member : memberList) {
            if (!member.getRole().equals("Student")) continue;

            HBox scoreContainer = new HBox();
            Label score = new Label("Score: ");
            scoreContainer.setAlignment(Pos.CENTER_LEFT);

            Label scoreValue = new Label("0");

            scoreContainer.getChildren().addAll(score, scoreValue);

            MemberItem memberItem = new MemberItem(member, idx);
            HBox spacer = new HBox();

            HBox.setHgrow(spacer, Priority.ALWAYS);

            memberItem.getChildren().add(spacer);
            memberItem.getChildren().add(scoreContainer);

            memberItem.getStyleClass().add("task-item");

            fetchAnswer(member, memberItem, scoreContainer, scoreValue);

            this.memberList.getChildren().addAll(memberItem);
            idx++;
        }

        if (idx == 1) {
            Label noMember = new Label(TextStorage.getText(Text.NO_STUDENT_YET));
            noMember.setPadding(new Insets(20));
            this.memberList.getChildren().add(noMember);
            this.memberList.setAlignment(Pos.CENTER);
        }
    }

    private void fetchAnswer(ClassroomMember member, MemberItem memberItem, HBox scoreContainer, Label scoreSideValue) {

        double score = 0;
        int taskCount = 0;

        ArrayList<Task> taskList = taskController.getScoredClassroomTask(classroom.getClassId());
        HashMap<String, Double> taskHash = new HashMap<>();

        for (Task task : taskList) {

            Double taskScore = this.answerController.getAnswerScore(
                    task.getId(), member.getUser().getId());

            taskHash.put(task.getTitle(), taskScore);

            if (taskScore != null) {
                score += taskScore;
                taskCount++;
            }
        }

        if (taskCount != 0) {
            score /= taskCount;
        }

        scoreContainer.getChildren().remove(scoreContainer.getChildren().size() - 1);

        Label scoreValue = new Label(String.format("%.2f", score * 100));
        scoreValue.setPrefWidth(60);
        scoreValue.setAlignment(Pos.CENTER_RIGHT);

        scoreContainer.getChildren().add(scoreValue);
        scoreSideValue.setText(String.format("%.2f", score * 100));

        double averageScore = score;
        memberItem.setOnMouseClicked(e -> {
            this.selectedMember = member;
            displayMemberScore(taskHash, averageScore, memberItem, scoreContainer);
        });
    }

    private void displayMemberScore(
            HashMap<String, Double> taskHash, double averageScore, MemberItem memberItem, HBox scoreContainer) {
        this.scoreContainer.getChildren().clear();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        this.scoreContainer.getChildren().add(scrollPane);

        Label scoreTitle = new Label(TextStorage.getText(Text.AVERAGE_STUDENT_SCORE) + ": ");
        Label scoreValue = new Label(String.format("%.2f", averageScore * 100));

        VBox scoreInnerContainer = new VBox();
        scoreInnerContainer.setAlignment(Pos.CENTER_LEFT);
        scoreInnerContainer.setPadding(new Insets(20));
        scoreInnerContainer.setSpacing(20);
        scoreInnerContainer.setPrefWidth(870);

        scrollPane.setContent(scoreInnerContainer);

        VBox averageScoreContainer = new VBox();
        averageScoreContainer.setAlignment(Pos.CENTER);
        averageScoreContainer.getStyleClass().add("card");

        scoreValue.getStyleClass().add("title");

        averageScoreContainer.getChildren().addAll(scoreTitle, scoreValue);

        scoreInnerContainer.getChildren().addAll(averageScoreContainer);

        for (String taskTitle : taskHash.keySet()) {
            Label taskTitleLbl = new Label(taskTitle);
            taskTitleLbl.setPrefWidth(200);
            taskTitleLbl.setWrapText(true);

            HBox taskScoreContainer = new HBox();
            taskScoreContainer.setAlignment(Pos.CENTER_LEFT);

            taskScoreContainer.getChildren().addAll(taskTitleLbl);

            HBox container = new HBox();
            container.setAlignment(Pos.CENTER_LEFT);

            Label taskScoreLbl = new Label(TextStorage.getText(Text.SCORE) + ": ");
            container.getChildren().add(taskScoreLbl);

            if (taskHash.get(taskTitle) != null) {
                Label scoreLbl = new Label(String.valueOf(taskHash.get(taskTitle) * 100));
                scoreLbl.setPrefWidth(60);
                scoreLbl.setAlignment(Pos.CENTER_RIGHT);
                container.getChildren().add(scoreLbl);

                HBox spacer = new HBox();
                spacer.setPrefWidth(50);

                container.getChildren().add(spacer);

            } else {
                TextField taskScoreInput = new TextField();
                taskScoreInput.setText(Integer.toString(0));
                taskScoreInput.setAlignment(Pos.CENTER_RIGHT);

                taskScoreInput.setOnMouseClicked(e -> {
                    taskScoreInput.clear();
                });

                taskScoreInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue && taskScoreInput.getText().isEmpty()) {
                        taskScoreInput.setText("0");
                    }
                });

                taskScoreInput.setPrefWidth(60);

                container.getChildren().add(taskScoreInput);

                ImageView saveIcon = ImageViewBuilder.create()
                        .bindImageProperty(IconStorage.getIcon(Icon.SAVE))
                        .setFitHeight(20)
                        .setFitWidth(20)
                        .build();

                Button saveBtn = new Button();
                saveBtn.setGraphic(saveIcon);

                HBox.setMargin(saveBtn, new Insets(0, 0, 0, 10));

                saveBtn.setStyle(
                        "-fx-cursor: hand;-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;");

                container.getChildren().add(saveBtn);

                saveBtn.setOnMouseClicked(e -> {
                    if (this.answerController.scoreQuestionAnswer(
                            taskTitle, selectedMember.getUser().getId(), taskScoreInput.getText())) {
                        ToastBuilder.buildNormal().setText("Score saved!").build();
                        container.getChildren().removeAll(taskScoreInput, saveBtn);
                        Label scoreInput = new Label(taskScoreInput.getText());
                        scoreInput.setPrefWidth(60);
                        HBox.setMargin(scoreInput, new Insets(0, 50, 0, 0));
                        scoreInput.setAlignment(Pos.CENTER_RIGHT);

                        container.getChildren().add(scoreInput);

                        fetchAnswer(selectedMember, memberItem, scoreContainer, scoreValue);
                    } else {
                        ToastBuilder.buildNormal()
                                .setText("Student have no answer!")
                                .build();
                    }
                });
            }

            HBox spacer = new HBox();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            taskScoreContainer.getChildren().add(spacer);

            taskScoreContainer.getChildren().add(container);
            taskScoreContainer.setMaxWidth(400);

            scoreInnerContainer.getChildren().add(taskScoreContainer);
        }
    }
}
