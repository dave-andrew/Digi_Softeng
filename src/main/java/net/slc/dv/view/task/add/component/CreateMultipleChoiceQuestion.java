package net.slc.dv.view.task.add.component;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.*;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.interfaces.CreateQuestionBox;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

@Getter
public class CreateMultipleChoiceQuestion extends VBox implements CreateQuestionBox {
    private final TextArea questionField;
    private final List<TextField> answerFields;
    private final ComboBox<String> answerKey;

    public CreateMultipleChoiceQuestion() {
        Label questionLbl = LabelBuilder.create(TextStorage.getText(Text.ENTER_QUESTION_HERE))
                .build();

        this.questionField = TextAreaBuilder.create()
                .setPromptText(TextStorage.getText(Text.ENTER_QUESTION_HERE))
                .setWrapText(true)
                .setMaxHeight(200)
                .build();

        VBox questionContainer = VBoxBuilder.create()
                .addChildren(questionLbl, questionField)
                .setSpacing(10)
                .build();

        Label answerLbl =
                LabelBuilder.create(TextStorage.getText(Text.ENTER_ANSWER_HERE)).build();

        this.answerFields = new ArrayList<>();

        List<HBox> answerFields = List.of(
                createFieldLabelPair("A", TextStorage.getText(Text.ENTER_CHOICE_HERE)),
                createFieldLabelPair("B", TextStorage.getText(Text.ENTER_CHOICE_HERE)),
                createFieldLabelPair("C", TextStorage.getText(Text.ENTER_CHOICE_HERE)),
                createFieldLabelPair("D", TextStorage.getText(Text.ENTER_CHOICE_HERE)));

        GridPane answerGrid = GridPaneBuilder.create()
                .addChildren(answerFields.get(0), 0, 0)
                .addChildren(answerFields.get(1), 1, 0)
                .addChildren(answerFields.get(2), 0, 1)
                .addChildren(answerFields.get(3), 1, 1)
                .setHGap(20)
                .setVGap(5)
                .build();

        VBox answerContainer = VBoxBuilder.create()
                .addChildren(answerLbl, answerGrid)
                .setSpacing(10)
                .build();

        Label answerKeyLbl =
                LabelBuilder.create(TextStorage.getText(Text.ANSWER_KEY)).build();

        answerKey = ComboBoxBuilder.<String>create()
                .setItems("A", "B", "C", "D")
                .setValue("A")
                .build();

        HBox answerKey = HBoxBuilder.create()
                .addChildren(answerKeyLbl, this.answerKey)
                .setSpacing(10)
                .setAlignment(Pos.CENTER_LEFT)
                .build();

        VBoxBuilder.modify(this)
                .addChildren(questionContainer, answerContainer, answerKey)
                .setSpacing(30)
                .build();
    }

    private HBox createFieldLabelPair(String label, String promptText) {
        Label lbl = LabelBuilder.create(label).build();

        TextField field = TextFieldBuilder.create().setPromptText(promptText).build();

        answerFields.add(field);

        return HBoxBuilder.create()
                .addChildren(lbl, field)
                .setAlignment(Pos.CENTER_LEFT)
                .setSpacing(10)
                .build();
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public String getQuestionText() {
        return this.questionField.getText();
    }

    @Override
    public String getQuestionAnswer() {
        StringBuilder questionAnswer = new StringBuilder();

        for (int i = 0; i < answerFields.size(); i++) {
            if (answerFields.get(i).getText().isEmpty()) {
                questionAnswer.append(" ");
            } else {
                questionAnswer.append(answerFields.get(i).getText());
            }
            if (i != answerFields.size() - 1) {
                questionAnswer.append(";");
            }
        }

        return questionAnswer.toString();
    }

    @Override
    public String getQuestionKey() {
        String key = this.answerKey.getValue();

        if (key.equals("A")) {
            return this.answerFields.get(0).getText();
        }
        if (key.equals("B")) {
            return this.answerFields.get(1).getText();
        }
        if (key.equals("C")) {
            return this.answerFields.get(2).getText();
        }
        if (key.equals("D")) {
            return this.answerFields.get(3).getText();
        }

        return this.answerKey.getValue();
    }
}
