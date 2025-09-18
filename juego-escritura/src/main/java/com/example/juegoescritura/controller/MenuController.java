package com.example.juegoescritura.controller;

import com.example.juegoescritura.model.HighScoreManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

/**
 * MenuController controla la escena del menú principal de VelociTexto.
 *
 * <p>Responsabilidades:
 * <ul>
 *   <li>Cargar y mostrar el highscore almacenado</li>
 *   <li>Cargar el logotipo de la aplicación de forma segura</li>
 *   <li>Navegar a la escena del juego y abrir el diálogo de instrucciones</li>
 *   <li/>(Re)inicializar recursos del menú cuando se retorna desde el juego</li>
 * </ul>
 *
 * <p>Interacción con FXML:
 * <ul>
 *   <li>Se esperan métodos {@code onStartClicked}, {@code onInstructionsClicked} y {@code onResetHighScore}</li>
 *   <li>Se espera que el FXML tenga fx:ids: lblHighScore, btnStart, btnReset, imgLogo</li>
 * </ul>
 */
public class MenuController {

    @FXML private Label lblHighScore;
    @FXML private Button btnStart;
    @FXML private Button btnReset;
    @FXML private ImageView imgLogo;

    private Stage primaryStage;
    private final HighScoreManager hsManager = new HighScoreManager();

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        int hs = hsManager.loadHighScore();
        lblHighScore.setText(String.valueOf(hs));
        loadLogoSafely();
    }

    private void loadLogoSafely() {
        String logoPath = "/com/example/juegoescritura/assets/images/logo.png";
        try (InputStream is = getClass().getResourceAsStream(logoPath)) {
            if (is != null) {
                imgLogo.setImage(new Image(is));
                imgLogo.setVisible(true);
            } else {
                imgLogo.setVisible(false);
                System.err.println("RESOURCE MISSING: " + logoPath);
            }
        } catch (Exception e) {
            imgLogo.setVisible(false);
            System.err.println("ERROR loading logo: " + e.getMessage());
        }
    }

    @FXML
    private void onStartClicked() {
        try {
            URL gameUrl = getClass().getResource("/com/example/juegoescritura/game.fxml");
            FXMLLoader loader = new FXMLLoader(gameUrl);
            Parent root = loader.load();
            GameController gameController = loader.getController();
            gameController.setPrimaryStage(primaryStage);
            Scene gameScene = new Scene(root);
            URL css = getClass().getResource("/com/example/juegoescritura/styles.css");
            if (css != null) gameScene.getStylesheets().add(css.toExternalForm());
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("VelociTexto - Juego");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onInstructionsClicked() {
        try {
            URL fxml = getClass().getResource("/com/example/juegoescritura/instructions.fxml");
            if (fxml == null) {
                System.err.println("instructions.fxml no encontrado");
                return;
            }
            Parent root = FXMLLoader.load(fxml);
            Scene scene = new Scene(root);
            URL css = getClass().getResource("/com/example/juegoescritura/styles.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            Stage dialog = new Stage();
            dialog.initOwner(primaryStage);
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setTitle("Instrucciones");
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onResetHighScore() {
        hsManager.saveHighScore(0);
        lblHighScore.setText("0");
    }
}

