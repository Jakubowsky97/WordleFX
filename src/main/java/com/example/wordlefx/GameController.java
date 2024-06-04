package com.example.wordlefx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.List;

public class GameController {

    @FXML private TextField field00;
    @FXML private TextField field01;
    @FXML private TextField field02;
    @FXML private TextField field03;
    @FXML private TextField field04;
    @FXML private TextField field10;
    @FXML private TextField field11;
    @FXML private TextField field12;
    @FXML private TextField field13;
    @FXML private TextField field14;
    @FXML private TextField field20;
    @FXML private TextField field21;
    @FXML private TextField field22;
    @FXML private TextField field23;
    @FXML private TextField field24;
    @FXML private TextField field30;
    @FXML private TextField field31;
    @FXML private TextField field32;
    @FXML private TextField field33;
    @FXML private TextField field34;
    @FXML private TextField field40;
    @FXML private TextField field41;
    @FXML private TextField field42;
    @FXML private TextField field43;
    @FXML private TextField field44;

    private TextField[][] fields;
    private List<String> wordList;
    private RandomWord randomWordGenerator;
    @FXML
    private Label lblInfo;
    @FXML
    private Button reloadBtn;
    private String randomWord;

    @FXML
    public void initialize() {

        reloadBtn.setVisible(false);
        fields = new TextField[][]{
                {field00, field01, field02, field03, field04},
                {field10, field11, field12, field13, field14},
                {field20, field21, field22, field23, field24},
                {field30, field31, field32, field33, field34},
                {field40, field41, field42, field43, field44}
        };

        WordLoader wordLoader = new WordLoader();
        try {
            wordList = wordLoader.loadWordsFromFile("src\\main\\resources\\com\\example\\wordlefx\\sgb-words.txt");
            randomWordGenerator = new RandomWord(wordList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        randomWord = randomWordGenerator.getRandomWord();
        System.out.println(randomWord);

        for (int row = 0; row < fields.length; row++) {
            for (int col = 0; col < fields[row].length; col++) {
                if (row != 0) {
                    fields[row][col].setDisable(true);
                }
                addTextFieldListener(fields[row][col]);
            }
        }

        reloadBtn.setOnAction(event -> {
            resetGame();
        });
    }

    private void addTextFieldListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleTextChange(textField, newValue);
            if (!newValue.isEmpty()) {
                focusNextField(textField);
            }
        });
    }

    private void handleTextChange(TextField textField, String newValue) {
        if (newValue.length() > 1) {
            textField.setText(newValue.substring(0, 1));
        }
        if (newValue.length() > 0) {
            textField.setText(newValue.toUpperCase());
        }
    }

    private void focusNextField(TextField currentField) {
        for (int row = 0; row < fields.length; row++) {
            boolean rowCompleted = true;
            for (int col = 0; col < fields[row].length; col++) {
                if (fields[row][col] == currentField) {
                    if (col < fields[row].length - 1) {
                        fields[row][col + 1].setDisable(false);
                        fields[row][col + 1].requestFocus();
                    } else if (row < fields.length - 1) {
                        fields[row + 1][0].setDisable(false);
                        checkWord(row);
                        fields[row + 1][0].requestFocus();
                    }
                }
                if (fields[row][col].getText().isEmpty()) {
                    rowCompleted = false;
                }
            }
            if (rowCompleted) {
                checkWord(row);
            }
        }
    }

    private void checkWord(int row) {
        StringBuilder wordBuilder = new StringBuilder();
        for (int col = 0; col < fields[row].length; col++) {
            wordBuilder.append(fields[row][col].getText());
        }
        String word = wordBuilder.toString().toLowerCase();
        System.out.println("Wylosowane słowo to:" + randomWord);

        if (!wordList.contains(word)) {
            lblInfo.setText("Nie ma takiego słowa!");
            // Wyczyść i odblokuj pola w aktualnym rzędzie
            for (int col = 0; col < fields[row].length; col++) {
                fields[row][col].setText("");
                fields[row][col].setDisable(false);
            }

            fields[row][0].requestFocus();

            // Zablokuj następny rząd
            if (row + 1 < fields.length) {
                for (int col = 0; col < fields[row + 1].length; col++) {
                    fields[row + 1][col].setDisable(true);
                }
            }
            fields[row][0].requestFocus();
            System.out.println(fields[row][0].isFocused());
            return;
        }

        boolean[] matchedInRandomWord = new boolean[randomWord.length()];
        boolean[] matchedInUserWord = new boolean[word.length()];

        // First pass: Check for correct positions (green)
        for (int i = 0; i < randomWord.length(); i++) {
            char userChar = word.charAt(i);
            char randomChar = randomWord.charAt(i);
            if (userChar == randomChar) {
                fields[row][i].setStyle("-fx-control-inner-background: green;");
                lblInfo.setText("");
                matchedInRandomWord[i] = true;
                matchedInUserWord[i] = true;
            }
        }

        // Second pass: Check for wrong positions (yellow) and non-existing letters (red)
        for (int i = 0; i < randomWord.length(); i++) {
            if (!matchedInUserWord[i]) {
                char userChar = word.charAt(i);
                boolean found = false;
                for (int j = 0; j < randomWord.length(); j++) {
                    if (!matchedInRandomWord[j] && userChar == randomWord.charAt(j)) {
                        fields[row][i].setStyle("-fx-control-inner-background: yellow;");
                        lblInfo.setText("");
                        matchedInRandomWord[j] = true;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    fields[row][i].setStyle("-fx-control-inner-background: red;");
                }
            }
        }
        if (word.equals(randomWord)) {
            lblInfo.setText("Zgadłeś/aś!!");
            reloadBtn.setVisible(true);
        }

        for (int col = 0; col < fields[row].length; col++) {
            fields[row][col].setDisable(true);
        }

        // Check if all rows are used
        if (row == fields.length - 1 && !word.equals(randomWord)) {
            lblInfo.setText("Koniec gry! Prawidłowe słowo: " + randomWord);
            reloadBtn.setVisible(true);
        }
    }

    private void resetGame() {
        lblInfo.setText("");
        randomWord = randomWordGenerator.getRandomWord();
        for (int row = 0; row < fields.length; row++) {
            for (int col = 0; col < fields[row].length; col++) {
                fields[row][col].setText("");
                fields[row][col].setDisable(false);
                fields[row][col].setStyle("");
                if (row != 0) {
                    fields[row][col].setDisable(true);
                }
            }
        }
        fields[0][0].requestFocus();
        reloadBtn.setVisible(false);
    }
}
