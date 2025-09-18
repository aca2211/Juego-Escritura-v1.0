package com.example.juegoescritura;

import com.example.juegoescritura.controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

/**
 * Main es la clase de entrada de la aplicación VelociTexto.
 *
 * <p>Responsabilidades principales:
 * <ul>
 *   <li>Cargar recursos globales (fuentes, CSS, icono)</li>
 *   <li>Inicializar y mostrar la escena del menú principal (menu.fxml)</li>
 *   <li>Proporcionar mensajes de diagnóstico cuando faltan recursos</li>
 * </ul>
 *
 * <p>Uso:
 * <pre>{@code
 * java -jar VelociTexto.jar
 * }</pre>
 *
 * <p>Notas de implementación:
 * <ul>
 *   <li>Busca recursos en el classpath bajo /com/example/juegoescritura/.</li>
 *   <li>Si faltan fuentes o imágenes, se escribe la advertencia en stderr y la aplicación continúa.</li>
 * </ul>
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadFontSafe("/com/example/juegoescritura/assets/fonts/Inter-Regular.ttf");
        loadFontSafe("/com/example/juegoescritura/assets/fonts/Inter-Bold.ttf");

        String iconPath = "/com/example/juegoescritura/assets/images/icon.png";
        try (InputStream is = Main.class.getResourceAsStream(iconPath)) {
            if (is != null) {
                primaryStage.getIcons().add(new Image(is));
            } else {
                System.err.println("RESOURCE MISSING: " + iconPath);
            }
        } catch (Exception e) {
            System.err.println("ERROR loading icon: " + e.getMessage());
        }

        URL menuUrl = Main.class.getResource("/com/example/juegoescritura/menu.fxml");
        if (menuUrl == null) {
            throw new IllegalStateException("No se encontró menu.fxml en /com/example/juegoescritura/. Verifica resources y rutas.");
        }

        FXMLLoader loader = new FXMLLoader(menuUrl);
        Parent root = loader.load();
        MenuController menuController = loader.getController();
        menuController.setPrimaryStage(primaryStage);

        Scene scene = new Scene(root);
        URL css = Main.class.getResource("/com/example/juegoescritura/styles.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.err.println("WARNING: styles.css no encontrado en /com/example/juegoescritura/");
        }

        primaryStage.setTitle("VelociTexto");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void loadFontSafe(String resourcePath) {
        try (InputStream is = Main.class.getResourceAsStream(resourcePath)) {
            if (is != null) {
                Font.loadFont(is, 12);
            } else {
                System.err.println("FONT MISSING: " + resourcePath);
            }
        } catch (Exception e) {
            System.err.println("Error cargando fuente " + resourcePath + " : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

