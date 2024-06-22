package net.slc.dv.view.task.add;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.slc.dv.builder.*;
import net.slc.dv.controller.TaskController;
import net.slc.dv.enums.TaskCenter;
import net.slc.dv.helper.DateManager;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.Question;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.IconStorage;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.home.component.Profile;
import net.slc.dv.view.task.add.component.CreateGeneralTask;
import net.slc.dv.view.task.add.component.CreateQuestionTask;
import net.slc.dv.view.task.add.component.TimeSpinner;

public class AddTaskView extends BorderPane {

    private Classroom classroom;
    private Stage stage;
    private TaskController taskController;

    private Stage dialogStage;
    private Scene dialogScene;

    private BorderPane root;

    //    NAVBAR
    private HBox leftNav;
    private ImageView close;
    private Label title;
    private Button joinBtn;
    private Button closeBtn;

    //  CENTER FORM
    private CheckBox scored;
    private DatePicker datePicker;
    private TimeSpinner timeSpinner;
    private TaskCenter centerType;
    private final CreateGeneralTask createFileTask;
    private final CreateQuestionTask createQuestionTask;

    public AddTaskView(Stage stage, Classroom classroom) {
        this.classroom = classroom;
        this.stage = stage;
        this.taskController = new TaskController();
        this.centerType = TaskCenter.FILE;
        this.createFileTask = new CreateGeneralTask();
        this.createQuestionTask = new CreateQuestionTask();
        init();
    }

    private void init() {
        this.dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(stage);

        this.root = new BorderPane();
        this.root.setRight(rightBar());
        this.root.setTop(navBar());
        this.changeCenter();

        dialogScene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        dialogStage.setScene(dialogScene);

        ThemeManager.getInstance().getTheme(dialogScene);

        dialogStage.setTitle(TextStorage.getText(Text.ADD_TASK));
        dialogStage.showAndWait();
    }

    private void changeCenter() {
        if (this.centerType == TaskCenter.FILE) {
            this.root.setCenter(this.createFileTask);
            return;
        }
        this.root.setCenter(this.createQuestionTask);
    }

    private HBox navBar() {
        close = ImageViewBuilder.create()
                .bindImageProperty(IconStorage.getIcon(Icon.CLOSE))
                .setFitWidth(20)
                .setPreserveRatio(true)
                .build();

        closeBtn = ButtonBuilder.create()
                .setGraphic(close)
                .setStyleClass("image-button")
                .setOnAction(e -> dialogStage.close())
                .build();

        title = LabelBuilder.create(TextStorage.getText(Text.CREATE_NEW_TASK))
                .setStyleClass("title")
                .setPadding(10, 0, 10, 0)
                .setHgrow(Priority.ALWAYS)
                .build();

        leftNav = HBoxBuilder.create()
                .addChildren(closeBtn, title)
                .setAlignment(Pos.CENTER_LEFT)
                .setHgrow(Priority.ALWAYS)
                .setSpacing(20)
                .build();

        return HBoxBuilder.create()
                .addChildren(leftNav)
                .setStyleClass("nav-bar")
                .setAlignment(Pos.CENTER)
                .build();
    }

    private void createTask() {
        if (this.centerType == TaskCenter.FILE) {
            this.submitFormFile();
        } else {
            this.submitFormTest();
        }
    }

    private void submitFormFile() {
        String title = this.createFileTask.getTitleField().getText();
        String description = this.createFileTask.getDescriptionField().getText();
        LocalDate deadline = this.datePicker.getValue();
        LocalTime time = this.timeSpinner.getValue();
        boolean scored = this.scored.isSelected();

        String deadlineAt = DateManager.formatDate(deadline, time);

        if (title.isEmpty() || description.isEmpty()) {
            this.createFileTask.getErrorLbl().setText(TextStorage.getText(Text.PLEASE_FILL_ALL_THE_FIELDS));
            return;
        }

        this.taskController.createFileTask(title, description, deadlineAt, scored, classroom.getClassId());

        dialogStage.close();
    }

    private void submitFormTest() {
        String title = this.createQuestionTask.getTaskTitle();
        String description = this.createQuestionTask.getTaskDescription();
        List<Question> questions = this.createQuestionTask.getQuestions();

        LocalDate deadline = this.datePicker.getValue();
        LocalTime time = this.timeSpinner.getValue();
        boolean scored = this.scored.isSelected();

        String deadlineAt = DateManager.formatDate(deadline, time);

        if (title.isEmpty() || description.isEmpty() || questions.isEmpty()){
            this.createFileTask.getErrorLbl().setText(TextStorage.getText(Text.PLEASE_FILL_ALL_THE_FIELDS));
            return;
        }

        this.taskController.createQuestionTask(
                questions, title, description, deadlineAt, scored, classroom.getClassId());

        dialogStage.close();
    }

    private VBox rightBar() {
        Label dateTimeLabel = new Label(TextStorage.getText(Text.DEADLINE));
        this.datePicker = new DatePicker();

        datePicker.setValue(LocalDate.now());

        Profile.DateFormatter(datePicker);

        this.timeSpinner = new TimeSpinner();
        timeSpinner.getValueFactory().setValue(LocalTime.of(23, 59, 59));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

        timeSpinner.valueProperty().addListener((obs, oldTime, newTime) -> {
            timeSpinner.getEditor().setText(newTime.format(formatter));
        });

        VBox dateTimeBox = VBoxBuilder.create()
                .addChildren(dateTimeLabel, datePicker, timeSpinner)
                .setSpacing(10)
                .setAlignment(Pos.CENTER_LEFT)
                .build();

        Label scoreLabel = new Label(TextStorage.getText(Text.SCORED));
        this.scored = new CheckBox();
        scored.setSelected(false);

        VBox scoreBox = VBoxBuilder.create()
                .addChildren(scoreLabel, scored)
                .setSpacing(10)
                .setAlignment(Pos.CENTER_LEFT)
                .setPadding(30, 40, 0, 40)
                .build();

        VBox spacer = VBoxBuilder.create().setVgrow(Priority.ALWAYS).build();

        joinBtn = ButtonBuilder.create(TextStorage.getText(Text.CREATE_TASK))
                .setStyleClass("primary-button")
                .setStyle("-fx-text-fill: white;")
                .setPrefWidth(300)
                .setOnAction(e -> this.createTask())
                .setVMargin(0, 0, 50, 0)
                .build();

        VBox dateTimeContainer = VBoxBuilder.create().setPadding(30, 40, 0, 40).build();

        Label questionLabel =
                LabelBuilder.create(TextStorage.getText(Text.TASK_TYPE)).build();

        ComboBox<Object> questionBtn = ComboBoxBuilder.create()
                .setItems(TextStorage.getText(Text.TASK), TextStorage.getText(Text.TEST))
                .setValue(TextStorage.getText(Text.TASK))
                .setOnAction(e -> this.changeTaskType())
                .bindWidthProperty(dateTimeContainer, 80)
                .build();

        VBox questionContainer = VBoxBuilder.create()
                .addChildren(questionLabel, questionBtn)
                .setSpacing(10)
                .build();

        VBoxBuilder.modify(dateTimeContainer)
                .addChildren(questionContainer, dateTimeBox)
                .setSpacing(30)
                .build();

        return VBoxBuilder.create()
                .addChildren(dateTimeContainer, scoreBox, spacer, joinBtn)
                .setAlignment(Pos.CENTER)
                .setStyleClass("side-nav")
                .build();
    }

    private void changeTaskType() {
        if (this.centerType == TaskCenter.FILE) {
            this.centerType = TaskCenter.TEST;
        } else {
            this.centerType = TaskCenter.FILE;
        }

        this.changeCenter();
    }
}
