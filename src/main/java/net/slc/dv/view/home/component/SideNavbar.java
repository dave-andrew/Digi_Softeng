package net.slc.dv.view.home.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.Getter;
import net.slc.dv.builder.*;
import net.slc.dv.controller.AuthController;
import net.slc.dv.enums.Theme;
import net.slc.dv.helper.StageManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.IconStorage;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.login.LoginView;

@Getter
public class SideNavbar extends StackPane {
    private final List<SideNavbarButton> sideButtons;
    private final AuthController authController;
    private final VBox sideNavbarContainer;
    private final SideNavbarButton homeButton;
    private final SideNavbarButton calendarButton;
    private final VBox sidebarSpacer;
    private final HBox logoutButton;
    private final Button buttonToggle;
    private boolean sidebarOpen;
    private final Consumer<SideNavbarButton> onButtonClick;

    public SideNavbar(Consumer<SideNavbarButton> onButtonClick) {
        this.onButtonClick = onButtonClick;
        this.sideButtons = new ArrayList<>();
        this.authController = new AuthController();
        this.homeButton =
                new SideNavbarButton(IconStorage.getIcon(Icon.HOME), TextStorage.getText(Text.HOME), onButtonClick);
        this.calendarButton = new SideNavbarButton(
                IconStorage.getIcon(Icon.CALENDAR), TextStorage.getText(Text.CALENDAR), onButtonClick);
        this.sidebarOpen = true;

        this.sideButtons.add(this.homeButton);
        this.sideButtons.add(this.calendarButton);

        this.logoutButton = this.LogoutButton();

        this.sidebarSpacer = VBoxBuilder.create().setVgrow(Priority.ALWAYS).build();

        this.sideNavbarContainer = VBoxBuilder.create()
                .addChildren(this.sideButtons.stream().map(Node.class::cast).toArray(Node[]::new))
                .addChildren(this.sidebarSpacer, logoutButton)
                .setStyleClass("side-nav")
                .build();

        ImageView leftArrow = ImageViewBuilder.create()
                .bindImageProperty(IconStorage.getIcon(Icon.LEFT_ARROW))
                .setPreserveRatio(true)
                .setFitWidth(25)
                .setFitHeight(25)
                .build();

        this.buttonToggle = ButtonBuilder.create()
                .setGraphic(leftArrow)
                .setStyle(
                        "-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0; -fx-cursor: hand")
                .setOnAction(e -> toggleSidebar())
                .build();

        StackPaneBuilder.modify(this)
                .addChildren(sideNavbarContainer, buttonToggle)
                .setAlignment(Pos.CENTER_RIGHT)
                .build();

        this.sideButtons.get(0).setActive();
    }

    public HBox LogoutButton() {
        ImageView icon = ImageViewBuilder.create()
                .bindImageProperty(IconStorage.getIcon(Icon.LOGOUT))
                .setFitWidth(20)
                .setFitHeight(20)
                .setPreserveRatio(true)
                .build();

        Label label = LabelBuilder.create(TextStorage.getText(Text.LOGOUT))
                .setStyleClass("logout")
                .setStyle(
                        (ThemeManager.getInstance().getTheme() == Theme.LIGHT
                                ? "-fx-text-fill: #8B4513;"
                                : "-fx-text-fill: #e16e1a;"))
                .setHMargin(0, 0, 0, 10)
                .build();

        return HBoxBuilder.create()
                .addChildren(icon, label)
                .setSpacing(10)
                .setPrefWidth(250)
                .setMargin(0, 0, 50, 0)
                .setStyleClass("side-nav-item")
                .setOnMouseClicked(e -> logout())
                .setAlignment(Pos.CENTER_LEFT)
                .build();
    }

    private void logout() {
        LoggedUser.getInstance().logout();

        this.authController.removeAuth();

        new LoginView(StageManager.getInstance());
    }

    public void setActive(SideNavbarButton sideNavbarButton) {
        this.sideButtons.forEach(button -> {
            button.setInactive();
            if (button.equals(sideNavbarButton)) {
                button.setActive();
            }
        });
    }

    private void toggleSidebar() {
        if (this.sidebarOpen) {
            closeSidebar();
        } else {
            openSidebar();
        }

        this.sidebarOpen = !this.sidebarOpen;
    }

    void closeSidebar() {
        this.sideNavbarContainer.setTranslateX(0);

        VBoxBuilder.modify(this.sideNavbarContainer).removeAll().build();

        KeyValue keyValue = new KeyValue(this.sideNavbarContainer.prefWidthProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue rotateArrow = new KeyValue(this.buttonToggle.rotateProperty(), 180, Interpolator.EASE_BOTH);

        KeyFrame start = new KeyFrame(
                Duration.ZERO,
                new KeyValue(
                        this.sideNavbarContainer.prefWidthProperty(),
                        this.sideNavbarContainer.getWidth(),
                        Interpolator.LINEAR));

        KeyFrame end = new KeyFrame(Duration.seconds(0.5), keyValue, rotateArrow);

        Timeline timeline = new Timeline(start, end);
        timeline.play();
    }

    void openSidebar() {
        double currentTranslateX = this.sideNavbarContainer.getTranslateX();

        KeyValue keyValue = new KeyValue(this.sideNavbarContainer.prefWidthProperty(), 240, Interpolator.EASE_BOTH);
        KeyValue rotateArrow = new KeyValue(this.buttonToggle.rotateProperty(), 0, Interpolator.EASE_BOTH);

        KeyFrame start = new KeyFrame(
                Duration.ZERO,
                new KeyValue(this.sideNavbarContainer.translateXProperty(), currentTranslateX, Interpolator.LINEAR));

        KeyFrame end = new KeyFrame(Duration.seconds(0.5), keyValue, rotateArrow);

        Timeline timeline = new Timeline(start, end);
        timeline.play();

        timeline.onFinishedProperty().set(e -> VBoxBuilder.modify(this.sideNavbarContainer)
                .addChildren(this.sideButtons.stream().map(Node.class::cast).toArray(Node[]::new))
                .addChildren(this.sidebarSpacer, this.logoutButton)
                .build());
    }
}
