package net.slc.dv.view.classroom.create.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.IconStorage;
import net.slc.dv.storage.TextStorage;

public class CreateClassNav extends HBox {

    private Button closeBtn;

    public CreateClassNav() {
        initialize();
        actions();
        this.getStyleClass().add("nav-bar");
    }

    private void initialize() {
        HBox leftNav = new HBox(20);

        ImageView close = ImageViewBuilder.create()
                .bindImageProperty(IconStorage.getIcon(Icon.CLOSE))
                .setFitHeight(17)
                .setFitWidth(17)
                .build();

        closeBtn = new Button();
        closeBtn.setGraphic(close);
        closeBtn.getStyleClass().add("image-button");

        Label title = new Label(TextStorage.getText(Text.CREATE_CLASS));
        title.getStyleClass().add("title");
        title.setPadding(new Insets(15, 0, 15, 0));

        Button joinBtn = new Button(TextStorage.getText(Text.CREATE));
        joinBtn.getStyleClass().add("primary-button");

        this.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(title, Priority.ALWAYS);
        HBox.setHgrow(joinBtn, Priority.NEVER);

        leftNav.getChildren().addAll(closeBtn, title);
        leftNav.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftNav, Priority.ALWAYS);

        this.getChildren().addAll(leftNav);
    }

    private void actions() {
        closeBtn.setOnMouseClicked(e -> {
            Stage createClassStage = (Stage) getScene().getWindow();
            createClassStage.close();
        });
    }
}
