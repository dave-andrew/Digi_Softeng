package net.slc.dv.view.task.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.slc.dv.controller.AnswerController;
import net.slc.dv.controller.FileController;
import net.slc.dv.helper.StageManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.helper.toast.Toast;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.IconStorage;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.task.upload.component.FileItem;

public class UploadFileModal {
    private final String taskId;
    private final FileController fileController;
    private final AnswerController answerController;
    private final VBox fileContainer;
    private final VBox sideContent;
    private final List<File> uploadedFiles = new ArrayList<>();
    private final Button downloadBtn;
    private VBox mainVbox;
    private Label errorLabel;
    private GridPane fileGrid;

    public UploadFileModal(String taskid, VBox fileContainer, VBox sideContent, Button downloadBtn) {
        this.taskId = taskid;
        this.fileController = new FileController();
        this.answerController = new AnswerController();
        this.fileContainer = fileContainer;
        this.sideContent = sideContent;
        this.downloadBtn = downloadBtn;
        initialize();
        setLayout();
        showAndWait();
    }

    private void initialize() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(StageManager.getInstance());
        dialogStage.initStyle(StageStyle.TRANSPARENT);

        mainVbox = new VBox(20);
        mainVbox.getStyleClass().add("modal");

        Scene scene = new Scene(mainVbox, 600, 600);
        scene.setFill(Color.TRANSPARENT);
        ThemeManager.getInstance().getTheme(scene);
        dialogStage.setScene(scene);
    }

    private void setLayout() {
        mainVbox.setAlignment(Pos.CENTER);
        VBox.setMargin(mainVbox, new Insets(20));
        ImageView uploadIcon = new ImageView(IconStorage.getIcon(Icon.CLOSE).getValue());
        uploadIcon.setFitWidth(12);
        uploadIcon.setFitHeight(12);

        HBox closeBox = new HBox();
        closeBox.setAlignment(Pos.CENTER);

        Label uploadLabel = new Label(TextStorage.getText(Text.UPLOAD_FILE));
        uploadLabel.getStyleClass().add("title");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button();
        closeBtn.setGraphic(uploadIcon);
        closeBtn.setStyle(
                "-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;-fx-cursor: hand");

        closeBox.getChildren().addAll(uploadLabel, spacer, closeBtn);

        closeBox.setAlignment(Pos.TOP_RIGHT);

        closeBtn.setOnMouseClicked(e -> {
            Stage stage = (Stage) mainVbox.getScene().getWindow();
            stage.close();
        });

        mainVbox.getChildren().add(closeBox);

        VBox uploadBox = new VBox(10);
        uploadBox.setAlignment(Pos.CENTER);
        uploadBox.setPadding(new Insets(120, 0, 120, 0));

        Label dropLabel = new Label(TextStorage.getText(Text.DROP_YOUR_FILE_HERE));
        uploadBox.setOnDragOver(this::handleDragOver);
        uploadBox.setOnDragDropped(this::handleDragDropped);

        uploadBox.getChildren().add(dropLabel);
        uploadBox.getStyleClass().add("upload-box");

        uploadBox.setOnMouseClicked(e -> {
            chooseFile();
        });

        Button uploadBtn = new Button("+ " + TextStorage.getText(Text.UPLOAD));
        uploadBtn.setStyle("-fx-text-fill: white");
        uploadBtn.prefWidthProperty().bind(mainVbox.widthProperty());
        VBox.setMargin(mainVbox, new Insets(0, 0, 20, 0));

        uploadBtn.setOnMouseClicked(e -> {
            if(uploadedFiles.isEmpty()) {
                errorLabel.setVisible(true);
                return;
            }
            this.fileController.uploadTaskAnswer(uploadedFiles, taskId);

            fetchAnswer();

            Stage stage = (Stage) mainVbox.getScene().getWindow();
            stage.close();

        });

        uploadBtn.getStyleClass().add("primary-button");

        VBox fileContainer = new VBox();
        fileContainer.setAlignment(Pos.CENTER_LEFT);

        ScrollPane files = new ScrollPane();
        files.setPrefHeight(200);
        Label fileLabel = new Label(TextStorage.getText(Text.FILES) + ":");

        fileContainer.getChildren().addAll(fileLabel, files);

        fileContainer.getStyleClass().add("top-border");

        this.fileGrid = new GridPane();
        fileGrid.setHgap(10);
        fileGrid.setVgap(10);

        files.setContent(fileGrid);
        errorLabel = new Label(TextStorage.getText(Text.NO_FILES_SELECTED));
        errorLabel.setStyle("-fx-text-fill: red");
        errorLabel.setVisible(false);

        mainVbox.getChildren().addAll(uploadBox, fileContainer, uploadBtn, errorLabel);
        mainVbox.setAlignment(Pos.TOP_CENTER);
        mainVbox.setPadding(new Insets(20));
    }

    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != event.getTarget()
                && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasFiles()) {
            success = true;
            List<File> droppedFiles = db.getFiles();

            for (File file : droppedFiles) {
                if (!uploadedFiles.contains(file)) {
                    if (!isFileSizeValid(file, 100)) {
                        Toast.makeError(
                                StageManager.getInstance(), TextStorage.getText(Text.FILE_TOO_BIG), 2000, 500, 500);
                        continue;
                    }
                    uploadedFiles.add(file);
                }
            }

            updateFileGrid();
        }

        event.setDropCompleted(success);
        event.consume();
    }

    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(TextStorage.getText(Text.CHOOSE_FILE));
        List<File> selectedFiles =
                fileChooser.showOpenMultipleDialog(mainVbox.getScene().getWindow());

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                if (!uploadedFiles.contains(file)) {
                    if (!isFileSizeValid(file, 100)) {
                        Toast.makeError(
                                StageManager.getInstance(), TextStorage.getText(Text.FILE_TOO_BIG), 2000, 500, 500);
                        continue;
                    }
                    uploadedFiles.add(file);
                }
            }

            updateFileGrid();
        }
    }

    private boolean isFileSizeValid(File file, long maxSizeInKb) {
        return file.length() <= maxSizeInKb * 1024;
    }

    private void showAndWait() {
        Stage dialogStage = (Stage) mainVbox.getScene().getWindow();
        dialogStage.setTitle(TextStorage.getText(Text.UPLOAD_FILE));
        dialogStage.showAndWait();
    }

    private void updateFileGrid() {
        fileGrid.getChildren().clear();

        int columnIndex = 0;
        int rowIndex = 0;

        for (File file : uploadedFiles) {

            FileItem fileItem = new FileItem(file);

            fileItem.getRemoveBtn().setOnMouseClicked(e -> {
                uploadedFiles.remove(file);
                updateFileGrid();
            });

            fileGrid.add(fileItem, columnIndex, rowIndex);

            columnIndex++;

            if (columnIndex == 2) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

    private void fetchAnswer() {
        ArrayList<File> fileList = this.answerController.getMemberFileAnswer(
                taskId, LoggedUser.getInstance().getId());

        sideContent.getChildren().add(fileContainer);

        if (!fileList.isEmpty()) {
            downloadBtn.getStyleClass().add("primary-button");
            downloadBtn.setOnMouseClicked(e -> this.answerController.downloadAllAnswer(fileList));
        }
    }
}
