package com.example.juegoescritura.controller;

import com.example.juegoescritura.model.GameModel;
import com.example.juegoescritura.model.HighScoreManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

/**
 * GameController maneja la escena de juego (game.fxml) de VelociTexto.
 *
 * <p>Responsabilidades principales:
 * <ul>
 *   <li>Controlar el flujo de niveles: presentar palabra, recibir input y validar</li>
 *   <li>Manejar el temporizador por nivel y la barra de progreso vinculada</li>
 *   <li>Calcular y actualizar la puntuación y el highscore</li>
 *   <li>Validar automáticamente la entrada cuando el tiempo se agota</li>
 *   <li>Proveer métodos para reiniciar, finalizar partida y volver al menú</li>
 * </ul>
 *
 * <p>Nota de comportamiento (ajustada): una validación manual incorrecta ya no termina la partida.
 * La partida solo termina cuando el tiempo llega a 0 y la entrada del usuario es incorrecta.
 */
public class GameController {

    @FXML private Label lblWord;
    @FXML private TextField txtInput;
    @FXML private Label lblTimer;
    @FXML private Label lblLevel;
    @FXML private Label lblFeedback;
    @FXML private Button btnValidate;
    @FXML private Button btnRestart;
    @FXML private ProgressBar progressBar;
    @FXML private Label lblScore;

    private final GameModel model = new GameModel();
    private final HighScoreManager hsManager = new HighScoreManager();

    private Timeline timeline;
    private int secondsLeft;
    private String currentWord = "";
    private int level = 1;
    private int score = 0;

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    private class ValidateHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent e) {
            validateInput();
        }
    }


    @FXML
    private void initialize() {
        ValidateHandler validateHandler = new ValidateHandler();
        btnValidate.setOnAction(validateHandler);
        txtInput.setOnAction(validateHandler);

        btnRestart.setOnAction(e -> endGameEarly());
        lblFeedback.setText("");
        progressBar.setProgress(1.0);
        startNewLevel();
    }

    private boolean isInputCorrect(String typed, String expected) {
        if (typed == null || expected == null) return false;
        return typed.trim().equals(expected.trim());
    }

    private void startNewLevel() {
        stopTimer();
        currentWord = model.nextWord();
        lblWord.setText(currentWord);
        txtInput.setText("");
        txtInput.requestFocus();
        secondsLeft = model.calculateTimeForLevel(level);
        lblLevel.setText(String.valueOf(level));
        lblScore.setText(String.valueOf(score));
        lblTimer.setText(secondsLeft + "s");
        progressBar.setProgress(1.0);
        lblFeedback.setText("");
        startTimer();
    }

    private void startTimer() {
        stopTimer();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            secondsLeft--;
            lblTimer.setText(secondsLeft + "s");
            double total = model.calculateTimeForLevel(level);
            progressBar.setProgress(Math.max(0.0, (double) secondsLeft / total));
            if (secondsLeft <= 0) {
                String typed = txtInput.getText();
                if (isInputCorrect(typed, currentWord)) {
                    handleSuccess();
                } else {
                    handleFail("Tiempo agotado");
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }
    private void validateInput() {
        String typed = txtInput.getText();
        if (isInputCorrect(typed, currentWord)) {
            handleSuccess();
        } else {
            lblFeedback.setText("Incorrecto. Intenta de nuevo.");
            txtInput.requestFocus();
            txtInput.selectAll();
        }
    }

    private void handleSuccess() {
        stopTimer();
        score += 10;
        level++;
        lblFeedback.setText("¡Correcto! +10");
        lblScore.setText(String.valueOf(score));
        startNewLevel();
    }

    private void handleFail(String reason) {
        stopTimer();
        lblFeedback.setText("Fallaste: " + reason);
        int previousHigh = hsManager.loadHighScore();
        if (score > previousHigh) {
            hsManager.saveHighScore(score);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Partida finalizada");
        alert.setHeaderText("Perdiste");
        alert.setContentText("Puntuación final: " + score + "\nMotivo: " + reason);
        try {
            var dp = alert.getDialogPane();
            URL css = getClass().getResource("/com/example/juegoescritura/styles.css");
            if (css != null) {
                dp.getStylesheets().add(css.toExternalForm());
                dp.getStyleClass().add("dialog-pane");
            }
        } catch (Exception ignored) {}
        Platform.runLater(() -> {
            try {
                alert.showAndWait();
            } catch (Exception ex) {
                alert.show();
            } finally {
                returnToMenu();
            }
        });
    }

    private void endGameEarly() {
        handleFail("Reiniciado por usuario");
    }

    private void returnToMenu() {
        if (primaryStage == null) {
            System.err.println("PrimaryStage no establecido; no se puede volver al menú.");
            return;
        }
        try {
            URL menuUrl = getClass().getResource("/com/example/juegoescritura/menu.fxml");
            FXMLLoader loader = new FXMLLoader(menuUrl);
            Parent menuRoot = loader.load();
            MenuController menuController = loader.getController();
            menuController.setPrimaryStage(primaryStage);
            Scene menuScene = new Scene(menuRoot);
            URL css = getClass().getResource("/com/example/juegoescritura/styles.css");
            if (css != null) menuScene.getStylesheets().add(css.toExternalForm());
            primaryStage.setScene(menuScene);
            primaryStage.setTitle("VelociTexto - Menú");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
