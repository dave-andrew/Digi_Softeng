package net.slc.dv.view.task.upload.component;

import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.slc.dv.resources.Icon;
import net.slc.dv.storage.IconStorage;

public class FileItem extends HBox {

    private final Button removeBtn;

    public FileItem(File file) {

        if (getFileType(file).equals("pdf")) {
            ImageView imgView = new ImageView(IconStorage.getIcon(Icon.PDF).getValue());
            imgView.setFitWidth(20);
            imgView.setFitHeight(20);

            this.getChildren().add(imgView);
        } else if (getFileType(file).equals("png")
                || getFileType(file).equals("jpg")
                || getFileType(file).equals("jpeg")) {
            ImageView imgView = new ImageView(IconStorage.getIcon(Icon.IMAGE).getValue());
            imgView.setFitWidth(20);
            imgView.setFitHeight(20);

            this.getChildren().add(imgView);
        } else {
            ImageView imgView = new ImageView(IconStorage.getIcon(Icon.FILE).getValue());
            imgView.setFitWidth(20);
            imgView.setFitHeight(20);

            this.getChildren().add(imgView);
        }

        Label fileName = new Label(file.getName());
        fileName.setStyle("-fx-font-size: 14px;");
        this.getChildren().add(fileName);

        ImageView closeIcon = new ImageView(IconStorage.getIcon(Icon.CLOSE).getValue());

        this.removeBtn = new Button();
        removeBtn.setGraphic(closeIcon);
        removeBtn.setStyle(
                "-fx-background-color: transparent; -fx-cursor: hand;-fx-border-style: none; -fx-border-color: transparent; -fx-border-width: 0;");

        closeIcon.setFitWidth(10);
        closeIcon.setFitHeight(10);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(spacer, removeBtn);

        HBox.setMargin(fileName, new Insets(0, 0, 0, 10));

        //        this.getStyleClass().add("card");
        this.setPadding(new Insets(3, 5, 3, 5));
        this.setPrefWidth(225);
        this.setAlignment(Pos.CENTER_LEFT);
    }

    private String getFileType(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }

        return "";
    }

    public Button getRemoveBtn() {
        return removeBtn;
    }
}
