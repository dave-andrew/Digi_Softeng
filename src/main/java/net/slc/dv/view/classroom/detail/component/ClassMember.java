package net.slc.dv.view.classroom.detail.component;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import net.slc.dv.controller.MemberController;
import net.slc.dv.model.Classroom;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

public class ClassMember extends ClassBase {

    private MemberController memberController;
    private VBox teacherContainer, studentContainer;
    private int teacherCount = 1;
    private int studentCount = 1;

    public ClassMember(Classroom classroom) {
        super(classroom);

        init();
        fetchClassMember();
    }

    @Override
    public void init() {
        this.memberController = new MemberController();

        SplitPane container = new SplitPane();
        container.setStyle("-fx-background-color: transparent;");

        SplitPane outerContainer = new SplitPane();
        outerContainer.setStyle("-fx-background-color: transparent;");
        outerContainer.setPadding(new Insets(20));

        VBox teacherBox = new VBox(10);
        VBox studentBox = new VBox(10);

        teacherContainer = new VBox(10);
        studentContainer = new VBox(10);

        teacherContainer.setPadding(new Insets(7, 20, 20, 20));
        studentContainer.setPadding(new Insets(7, 20, 20, 20));

        Label teacherTitle = new Label(TextStorage.getText(Text.TEACHER));
        teacherTitle.getStyleClass().add("title");

        Label studentTitle = new Label(TextStorage.getText(Text.STUDENT));
        studentTitle.getStyleClass().add("title");

        VBox teacherTitleBox = new VBox();
        teacherTitleBox.getStyleClass().add("bottom-border");
        teacherTitleBox.getChildren().add(teacherTitle);

        teacherContainer.getChildren().add(teacherTitleBox);
        teacherContainer.getStyleClass().add("border");
        teacherContainer.setStyle("-fx-effect: null");

        VBox studentTitleBox = new VBox();
        studentTitleBox.getStyleClass().add("bottom-border");
        studentTitleBox.getChildren().add(studentTitle);

        studentContainer.getChildren().add(studentTitleBox);
        studentContainer.getStyleClass().add("border");
        studentContainer.setStyle("-fx-effect: null");

        teacherBox.getChildren().add(teacherContainer);
        studentBox.getChildren().add(studentContainer);

        outerContainer.getItems().addAll(teacherBox, studentBox);

        outerContainer.setDividerPositions(0.5);

        container.getItems().add(outerContainer);
        container.prefWidthProperty().bind(this.widthProperty());

        container.setPadding(new Insets(10, 40, 10, 40));

        this.setContent(container);
    }

    private void fetchClassMember() {
        AtomicBoolean hasStudents = new AtomicBoolean(false);

        memberController.getClassMember(classroom.getClassId()).forEach(classroomMember -> {
            if (classroomMember.getRole().equals("Teacher")) {
                teacherContainer.getChildren().add(new ClassMemberItem(classroomMember, teacherCount));
                teacherCount++;
            } else {
                studentContainer.getChildren().add(new ClassMemberItem(classroomMember, studentCount));
                hasStudents.set(true);
                studentCount++;
            }
        });

        if (!hasStudents.get()) {
            Label noStudentsLabel = new Label(TextStorage.getText(Text.NO_STUDENT_TO_TEACH));
            studentContainer.getChildren().add(noStudentsLabel);
        }
    }
}
