package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.geometry.Pos;
import javafx.geometry.Insets;


public class TicTacToe extends Application {
    private final Button[][] buttons = new Button[3][3];
    private boolean isXTurn = true;

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(5);

        // Создаем кнопки 3x3
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button();
                button.setMinSize(100, 100);
                button.setStyle("-fx-font-size: 24;");
                buttons[row][col] = button;

                final int r = row;
                final int c = col;
                button.setOnAction(e -> handleButtonClick(r, c));

                grid.add(button, col, row);
            }
        }

        GridPane secondGrid = new GridPane();
        secondGrid.setAlignment(Pos.BOTTOM_CENTER);
        secondGrid.setHgap(5);
        secondGrid.setVgap(5);
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> resetBoard());
        secondGrid.add(exitButton, 0, 0);
        secondGrid.add(restartButton, 1, 0);
        secondGrid.setPadding(new Insets(10));

        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setHgap(5);
        mainGrid.setVgap(5);
        mainGrid.setPadding(new Insets(10));
        mainGrid.add(grid, 0, 0);
        mainGrid.add(secondGrid, 0, 1);

        Scene scene = new Scene(mainGrid, 450, 450);
        primaryStage.setTitle("Крестики-Нолики");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleButtonClick(int row, int col) {
        if (buttons[row][col].getText().isEmpty()) {
            buttons[row][col].setText(isXTurn ? "X" : "O");
            if (checkWin()) {
                showWinMessage(isXTurn ? "X" : "O");
                // поток блокирется, пока не нажмем ОК
                resetBoard();
            } else if (checkDraw()) {
                showDrawMessage();
                // поток блокирется, пока не нажмем ОК
                resetBoard();
            } else {
                isXTurn = !isXTurn;
            }
        }
    }

    private boolean checkWin() {
        // Проверка строк
        for (int row = 0; row < 3; row++) {
            if (checkLine(buttons[row][0], buttons[row][1], buttons[row][2])) {
                return true;
            }
        }

        // Проверка столбцов
        for (int col = 0; col < 3; col++) {
            if (checkLine(buttons[0][col], buttons[1][col], buttons[2][col])) {
                return true;
            }
        }

        // Проверка диагоналей
        return checkLine(buttons[0][0], buttons[1][1], buttons[2][2]) ||
                checkLine(buttons[0][2], buttons[1][1], buttons[2][0]);
    }

    private boolean checkLine(Button b1, Button b2, Button b3) {
        return !b1.getText().isEmpty() &&
                b1.getText().equals(b2.getText()) &&
                b1.getText().equals(b3.getText());
    }

    private boolean checkDraw() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                if (button.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showWinMessage(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Победа!");
        alert.setHeaderText(null);
        alert.setContentText("Игрок " + winner + " победил!");
        alert.showAndWait();
    }

    private void showDrawMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ничья!");
        alert.setHeaderText(null);
        alert.setContentText("Игра закончилась вничью!");
        alert.showAndWait();
    }

    private void resetBoard() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setText("");
            }
        }
        isXTurn = true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}