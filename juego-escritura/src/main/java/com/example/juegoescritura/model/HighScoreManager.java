package com.example.juegoescritura.model;

import java.util.prefs.Preferences;

/**
 * HighScoreManager gestiona la persistencia del mejor puntaje (high score)
 * alcanzado en VelociTexto.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Almacena el valor de forma persistente usando {@link java.util.prefs.Preferences}.</li>
 *   <li>La clave de almacenamiento es fija: {@code velocitexto.highscore}.</li>
 *   <li>Los valores negativos se corrigen automáticamente a 0.</li>
 *   <li>Se puede leer el valor con {@link #loadHighScore()} y escribir con {@link #saveHighScore(int)}.</li>
 * </ul>
 *
 * <p>Ejemplo de uso:
 * <pre>{@code
 * HighScoreManager manager = new HighScoreManager();
 * int current = manager.loadHighScore();
 * if (newScore > current) {
 *     manager.saveHighScore(newScore);
 * }
 * }</pre>
 *
 * <p>Notas:
 * <ul>
 *   <li>El almacenamiento depende de la implementación de Preferences en el sistema operativo.</li>
 *   <li>En Windows, se guarda en el Registro; en Linux, en archivos de usuario bajo ~/.java/.userPrefs.</li>
 * </ul>
 */
public class HighScoreManager {

    private static final String PREF_KEY = "velocitexto.highscore";
    private final Preferences prefs = Preferences.userNodeForPackage(HighScoreManager.class);

    /**
     * Carga el high score almacenado.
     * Si no existe aún, devuelve 0.
     *
     * @return valor entero del high score actual, o 0 si no hay.
     */
    public int loadHighScore() {
        return prefs.getInt(PREF_KEY, 0);
    }

    /**
     * Guarda un nuevo high score.
     * Si el valor proporcionado es negativo, se guarda como 0.
     *
     * @param score valor de puntuación a almacenar
     */
    public void saveHighScore(int score) {
        prefs.putInt(PREF_KEY, Math.max(0, score));
    }
}
