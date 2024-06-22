package net.slc.dv.view.classroom.list.component;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.slc.dv.controller.TaskController;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.Task;

public class ClassCard extends StackPane {

    public ClassCard(Classroom classroom) {
        TaskController taskController = new TaskController();

        this.setPrefSize(300, 250);
        this.getStyleClass().add("class-card");

        VBox cardContent = new VBox(10);

        Label classNameLbl = new Label(classroom.getClassName());
        classNameLbl.getStyleClass().add("bold-text");

        Label classCodeLbl = new Label(classroom.getClassDesc());
        classCodeLbl.setWrapText(true);
        cardContent.getStyleClass().add("blue-bg");

        classNameLbl.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        classCodeLbl.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        cardContent.setMaxHeight(100);
        cardContent.setPadding(new Insets(20));

        cardContent.getChildren().addAll(classNameLbl, classCodeLbl);

        VBox spacer = new VBox();
        spacer.setPadding(new Insets(10));

        ArrayList<Task> pendingTask = taskController.fetchClassroomPendingTask(classroom.getClassId());

        for (Task task : pendingTask) {

            Label taskCard = new Label(" \u2022 " + task.getTitle());
            taskCard.setWrapText(true);
            taskCard.setStyle("-fx-font-size: 12px;");

            HBox taskContainer = new HBox(10);
            taskContainer.getChildren().addAll(taskCard);

            spacer.getChildren().add(taskContainer);
        }

        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox pendingTaskContainer = new VBox(10);
        pendingTaskContainer.setPadding(new Insets(0));
        pendingTaskContainer.getChildren().addAll(cardContent, spacer);

        this.getChildren().addAll(pendingTaskContainer);
        setAlignment(cardContent, Pos.TOP_CENTER);
    }
}
