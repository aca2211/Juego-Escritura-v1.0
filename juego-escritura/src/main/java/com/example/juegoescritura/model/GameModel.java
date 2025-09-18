package com.example.juegoescritura.model;

import java.util.List;
import java.util.Random;

/**
 * GameModel encapsula la lógica de datos del juego VelociTexto.
 *
 * <p>Responsabilidades:
 * <ul>
 *   <li>Proveer una lista de palabras seleccionables aleatoriamente.</li>
 *   <li>Calcular el tiempo disponible para cada nivel de juego según reglas configurables.</li>
 * </ul>
 *
 * <p>Reglas de tiempo implementadas:
 * <ul>
 *   <li>Tiempo base por nivel: 20 segundos.</li>
 *   <li>Cada 5 niveles se reduce 2 segundos del tiempo base.</li>
 *   <li>El tiempo mínimo permitido es 2 segundos.</li>
 * </ul>
 *
 * <p>Notas:
 * <ul>
 *   <li>La lista de palabras está incluida directamente; puedes sustituirla por lectura desde archivo o servicio.</li>
 * </ul>
 */
public class GameModel {

    private final List<String> words = List.of(
            "Hola", "Programación", "Java", "Escritura rápida", "Scene Builder",
            "Interfaz gráfica", "JavaFX", "Evento", "Teclado", "Contador",
            "Correcto", "Incorrecto", "Nivel", "Puntuación", "Persistencia",
            "VelociTexto", "Aplicación", "Desafío", "Rendimiento", "Practica"
    );

    private final Random random = new Random();

    public String nextWord() {
        return words.get(random.nextInt(words.size()));
    }

    public int calculateTimeForLevel(int level) {
        int baseTime = 20;
        int reductions = (level - 1) / 5;
        int newTime = baseTime - (reductions * 2);
        return Math.max(newTime, 2);
    }
}






