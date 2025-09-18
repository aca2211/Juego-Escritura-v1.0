package com.example.juegoescritura.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * InstructionsController controla la ventana modal de instrucciones.
 *
 * <p>Responsabilidades:
 * <ul>
 *   <li>Proveer una acción de cierre para la ventana modal de instrucciones</li>
 *   <li>Ser un punto de extensión para añadir manipulación dinámica del texto o imágenes de ayuda</li>
 * </ul>
 *
 * <p>Interacción con FXML:
 * <ul>
 *   <li>Se espera fx:id btnClose en el FXML y un botón que invoque {@code onClose()}.</li>
 * </ul>
 */
public class InstructionsController {

    @FXML private Button btnClose;

    @FXML
    private void onClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}


