package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TicTacToe extends Application {
    private final Button[][] buttons = new Button[3][3];
    private boolean isXTurn = true;               // true → ход X (игрок), false → ход O (компьютер)
    private final Random random = new Random();

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(5);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button();
                button.setMinSize(100, 100);
                button.setStyle("-fx-font-size: 24;");
                buttons[row][col] = button;

                final int r = row;
                final int c = col;
                button.setOnAction(e -> handlePlayerMove(r, c));

                grid.add(button, col, row);
            }
        }

        GridPane controlBar = new GridPane();
        controlBar.setAlignment(Pos.BOTTOM_CENTER);
        controlBar.setHgap(5);
        controlBar.setVgap(5);

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> resetBoard());

        controlBar.add(exitButton, 0, 0);
        controlBar.add(restartButton, 1, 0);
        controlBar.setPadding(new Insets(10));

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(5);
        root.setVgap(5);
        root.setPadding(new Insets(10));
        root.add(grid, 0, 0);
        root.add(controlBar, 0, 1);

        Scene scene = new Scene(root, 450, 450);

        primaryStage.setTitle("Крестики‑нолики");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

// Человек
    private void handlePlayerMove(int row, int col) {
        if (!isXTurn || !buttons[row][col].getText().isEmpty()) {
            // либо сейчас не очередь игрока, либо клетка занята
            return;
        }

        buttons[row][col].setText("X");

        if (checkWin("X")) {
            showWinMessage("X");
            resetBoard();
            return;
        }

        if (checkDraw()) {
            showDrawMessage();
            resetBoard();
            return;
        }

        isXTurn = false;  
        computerMove();
    }

// Компьютер
    private void computerMove() {
        int[] move = findBestMove();
        if (move != null) {
            buttons[move[0]][move[1]].setText("O");
        }

        if (checkWin("O")) {
            showWinMessage("O");
            resetBoard();
            return;
        }

        if (checkDraw()) {
            showDrawMessage();
            resetBoard();
            return;
        }

        isXTurn = true;   
    }

// Анализ выгодного хода
    private int[] findBestMove() {

        int[] winMove = findCriticalMove("O");
        if (winMove != null) return winMove;

        int[] blockMove = findCriticalMove("X");
        if (blockMove != null) return blockMove;

        // Центральная часть
        if (buttons[1][1].getText().isEmpty()) {
            return new int[]{1, 1};
        }

        // Углы
        int[][] corners = {{0,0},{0,2},{2,0},{2,2}};
        List<int[]> freeCorners = new ArrayList<>();
        for (int[] c : corners) {
            if (buttons[c[0]][c[1]].getText().isEmpty()) freeCorners.add(c);
        }
        if (!freeCorners.isEmpty()) {
            return freeCorners.get(random.nextInt(freeCorners.size()));
        }

        // Если все выгодные позиции заняты занимаем оставшуюся свободную клетку
        List<int[]> freeCells = new ArrayList<>();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (buttons[r][c].getText().isEmpty()) freeCells.add(new int[]{r,c});
            }
        }
        if (freeCells.isEmpty()) return null;

        return freeCells.get(random.nextInt(freeCells.size()));
    }

  // Анализ выйгришного хода
    private int[] findCriticalMove(String symbol) {
        // проверяем каждую пустую клетку
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (buttons[r][c].getText().isEmpty()) {
                    buttons[r][c].setText(symbol);
                    boolean isWinning = checkWin(symbol);
                    buttons[r][c].setText(""); // откатываем
                    if (isWinning) {
                        return new int[]{r, c};
                    }
                }
            }
        }
        return null;
    }


    private boolean checkWin(String symbol) {
        // Проверка строк и столбцов
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(symbol) &&
                buttons[i][1].getText().equals(symbol) &&
                buttons[i][2].getText().equals(symbol))
                return true;

            if (buttons[0][i].getText().equals(symbol) &&
                buttons[1][i].getText().equals(symbol) &&
                buttons[2][i].getText().equals(symbol))
                return true;
        }

        // Диагонали
        if (buttons[0][0].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][2].getText().equals(symbol))
            return true;

        if (buttons[0][2].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][0].getText().equals(symbol))
            return true;

        return false;
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
        alert.setContentText("Победил игрок \"" + winner + "\"!");
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
