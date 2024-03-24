package com.example.timestorm;

import com.example.timestorm.edtutils.Teacher;
import com.example.timestorm.edtutils.TeacherCollection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.Collection;

public class InputPage {

    @FXML
    private TextField inputField;

    @FXML
    private Text seeChoice;
    private AutoCompletionBinding<String> autoCompletionBinding;

    @FXML
    private void handleInput(KeyEvent event) {
        String currentText = inputField.getText();
        System.out.println("Input: " + currentText);
        TeacherCollection instance = TeacherCollection.getInstance();
        ArrayList<Teacher> teacherSuggestions = instance.getTeacherLike(currentText);
        Collection<String> suggestions = new ArrayList<>();
        for (Teacher t : teacherSuggestions) {
            suggestions.add(t.getName());
            System.out.println(t.getName());
        }

        // Dispose of the old autocompletion binding if it exists
        if (autoCompletionBinding != null) {
            autoCompletionBinding.dispose();
        }


        Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>> suggestionProvider =
                request -> suggestions.stream()
                        .filter(suggestion -> suggestion.toLowerCase().contains(request.getUserText().toLowerCase()))
                        .toList();

        autoCompletionBinding = TextFields.bindAutoCompletion(inputField, suggestionProvider);

        autoCompletionBinding.setOnAutoCompleted(autoCompleteEvent -> {
            String selectedItem = autoCompleteEvent.getCompletion();
            seeChoice.setText(selectedItem);
        });
    }



}
