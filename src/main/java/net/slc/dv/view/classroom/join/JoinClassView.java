package net.slc.dv.view.classroom.join;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.slc.dv.controller.ClassController;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.classroom.join.component.ChangeAccountBox;
import net.slc.dv.view.classroom.join.component.GroupCodeForm;
import net.slc.dv.view.classroom.join.component.JoinClassNav;
import net.slc.dv.view.home.Home;

public class JoinClassView {

    private final Stage dialogStage;

    private ClassController classController;
    private Scene scene;
    private BorderPane borderPane;
    private VBox mainVbox;
    private JoinClassNav topBar;
    private VBox userInfoBox;
    private GroupCodeForm classFormBox;
    private VBox joinInfo;
    private Label errorLbl;
    private HBox buttonBox;
    private Button joinBtn;

    public JoinClassView(Stage ownerStage) {
        this.dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(ownerStage);
        //        dialogStage.initStyle(StageStyle.TRANSPARENT);

        initialize(ownerStage);
        setLayout();
        setActions();

        dialogStage.setScene(scene);

        dialogStage.setTitle(TextStorage.getText(Text.JOIN_CLASS));
        dialogStage.showAndWait();
    }

    private void initialize(Stage stage) {
        classController = new ClassController();

        borderPane = new BorderPane();
        mainVbox = new VBox(20);
        topBar = new JoinClassNav();

        buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red;");

        userInfoBox = new ChangeAccountBox(dialogStage);
        userInfoBox.setAlignment(Pos.CENTER_LEFT);
        userInfoBox.getStyleClass().add("card");
        userInfoBox.setMaxWidth(800);

        joinBtn = new Button(TextStorage.getText(Text.JOIN));
        joinBtn.getStyleClass().add("primary-button");
        joinBtn.setPrefWidth(120);
        joinBtn.setStyle("-fx-text-fill: white;-fx-background-radius: 5px");
        HBox.setHgrow(joinBtn, Priority.NEVER);

        buttonBox.getChildren().add(joinBtn);

        classFormBox = new GroupCodeForm(errorLbl, buttonBox);
        classFormBox.getStyleClass().add("card");
        classFormBox.setMaxWidth(800);

        joinInfo = new VBox(5);
        joinInfo.setPadding(new Insets(20));

        Label joinInfoSub = new Label(TextStorage.getText(Text.TO_JOIN_CLASS));

        VBox joinInfoList = new VBox(2);

        Label lbl1 = new Label(TextStorage.getText(Text.JOIN_CLASS_REQUIREMENT_ONE));
        Label lbl2 = new Label(TextStorage.getText(Text.JOIN_CLASS_REQUIREMENT_TWO));

        joinInfoSub.setStyle("-fx-font-size: 17px;");
        lbl1.setStyle("-fx-font-size: 14px; -fx-font-family: 'Nunito Light'");
        lbl2.setStyle("-fx-font-size: 14px; -fx-font-family: 'Nunito Light'");

        joinInfoList.getChildren().addAll(lbl1, lbl2);

        joinInfo.getChildren().addAll(joinInfoSub, joinInfoList);
        joinInfo.getStyleClass().add("card");
        joinInfo.setMaxWidth(800);
    }

    private void setLayout() {
        mainVbox.setPadding(new Insets(20, 20, 20, 20));

        mainVbox.getChildren().addAll(userInfoBox, classFormBox, joinInfo);
        mainVbox.setAlignment(Pos.TOP_CENTER);

        borderPane.setTop(topBar);
        borderPane.setCenter(mainVbox);

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getInstance().getTheme(scene);
    }

    private void setActions() {
        joinBtn.setOnMouseClicked(e -> {
            String message = classController.checkJoinClass(classFormBox.getGroupCode());

            if (message.equals(TextStorage.getText(Text.CLASS_JOINED_SUCCESSFULLY))) {
                Home.fetchClass();
                dialogStage.close();
            }

            errorLbl.setText(message);
        });
    }
}
