package net.slc.dv.builder;

import java.util.function.Consumer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ButtonBuilder {

    private final Button button;

    private ButtonBuilder(String text) {
        this.button = new Button(text);
    }

    private ButtonBuilder(Button button) {
        this.button = button;
    }

    public static ButtonBuilder modify(Button button) {
        return new ButtonBuilder(button);
    }

    public static ButtonBuilder create(String text) {
        return new ButtonBuilder(text);
    }

    public static ButtonBuilder create() {
        return new ButtonBuilder("");
    }

    public ButtonBuilder setGraphic(ImageView image) {
        this.button.setGraphic(image);

        return this;
    }

    public ButtonBuilder setStyle(String style) {
        this.button.setStyle(style);

        return this;
    }

    public ButtonBuilder setPadding(double top, double right, double bottom, double left) {
        this.button.setPadding(new Insets(top, right, bottom, left));

        return this;
    }

    public ButtonBuilder setStyleClass(String styleClass) {
        this.button.getStyleClass().add(styleClass);

        return this;
    }

    public ButtonBuilder setPrefWidth(double prefWidth) {
        this.button.setPrefWidth(prefWidth);

        return this;
    }

    public ButtonBuilder setMinWidth(double width) {
        this.button.setMinWidth(width);

        return this;
    }

    public ButtonBuilder bindPrefWidth(VBox vBox) {
        this.button.prefWidthProperty().bind(vBox.widthProperty());

        return this;
    }

    public ButtonBuilder bindPrefWidth(ScrollPane vBox, double substract) {
        this.button.prefWidthProperty().bind(vBox.widthProperty().subtract(substract));

        return this;
    }

    public ButtonBuilder setPrefWidth(int width) {
        this.button.setPrefWidth(width);

        return this;
    }

    public ButtonBuilder setVMargin(int margin1, int margin2, int margin3, int margin4) {
        VBox.setMargin(this.button, new Insets(margin1, margin2, margin3, margin4));

        return this;
    }

    public ButtonBuilder setOnAction(Consumer<Button> consumer) {
        this.button.setOnAction(e -> consumer.accept(this.button));

        return this;
    }

    public ButtonBuilder setPrefSize(int width, int height) {
        this.button.setPrefSize(width, height);

        return this;
    }

    public ButtonBuilder setAlignment(Pos pos) {
        this.button.setAlignment(pos);

        return this;
    }

    public ButtonBuilder setOnMouseClick(EventHandler<MouseEvent> eventHandler) {
        this.button.setOnMouseClicked(e -> eventHandler.handle(e));

        return this;
    }

    public ButtonBuilder setDisable(boolean disable) {
        this.button.setDisable(disable);

        return this;
    }

    public Button build() {
        return this.button;
    }
}
