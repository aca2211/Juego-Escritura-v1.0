Aquí tienes el `README.md` listo para pegar en la raíz del repositorio y subir a GitHub. Copia todo el contenido a un archivo llamado `README.md`.

```markdown
# VelociTexto

**VelociTexto** — aplicación educativa en JavaFX para practicar escritura rápida.  
Muestra palabras, el jugador las escribe antes de que se agote el tiempo, gana puntos y avanza niveles. Ideal como mini-proyecto o práctica de JavaFX, FXML y diseño UI.

---

## Contenido del repositorio

```

juego-escritura/
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ com/example/juegoescritura/
│  │  │     ├─ Main.java
│  │  │     ├─ controller/
│  │  │     │  ├─ MenuController.java
│  │  │     │  ├─ GameController.java
│  │  │     │  └─ InstructionsController.java
│  │  │     └─ model/
│  │  │        ├─ GameModel.java
│  │  │        └─ HighScoreManager.java
│  │  ├─ resources/
│  │  │  └─ com/example/juegoescritura/
│  │  │     ├─ menu.fxml
│  │  │     ├─ game.fxml
│  │  │     ├─ instructions.fxml
│  │  │     ├─ styles.css
│  │  │     └─ assets/
│  │  │        ├─ images/
│  │  │        └─ fonts/
├─ pom.xml
├─ module-info.java
└─ README.md

````

---

## Requisitos

- **JDK 17** (o superior).  
- **Maven** si vas a usar `mvn` (opcional si ejecutas desde IntelliJ).  
- JavaFX (OpenJFX) 17 — en este repositorio se usa la dependencia OpenJFX vía Maven.

---

## module-info (obligatorio con módulos)

Asegúrate de que `module-info.java` incluye `java.prefs` (para `Preferences`) y que abre los paquetes al FXMLLoader. Ejemplo mínimo:

```java
module com.example.juegoescritura {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;

    opens com.example.juegoescritura to javafx.fxml;
    opens com.example.juegoescritura.controller to javafx.fxml;
}
````

---

## Compilar y ejecutar

### Opción A — IntelliJ (recomendado para desarrollo)

1. Importa el proyecto como Maven project (si usas `pom.xml`).
2. Marca `src/main/resources` como **Resources Root** (clic derecho → *Mark Directory as* → *Resources Root*).
3. Build → Rebuild Project.
4. Ejecuta la clase `com.example.juegoescritura.Main` como aplicación Java.

> Si IntelliJ te pide opciones VM para JavaFX, usa la configuración de Maven (recomendado) o añade el SDK JavaFX al Module path.

### Opción B — Maven (línea de comandos)

Si tu `pom.xml` usa `org.openjfx:javafx-maven-plugin`:

```bash
mvn clean javafx:run
```

O empaqueta y ejecuta (si usas dependencias modulares manualmente):

```bash
mvn clean package
# Ejecuta con module-path apuntando a tu JavaFX SDK
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -m com.example.juegoescritura/com.example.juegoescritura.Main
```

---

## Generar Javadoc

* Desde IntelliJ: *Tools → Generate JavaDoc...* (asegúrate `javadoc` en tu PATH).
* CLI (ejemplo):

```bash
javadoc -d docs -sourcepath src/main/java -subpackages com.example.juegoescritura
```

Si `javadoc` no se reconoce, añade la ruta a `jdk/bin` al `PATH`.

---

## Recursos (imagenes / css / fonts)

* Coloca imágenes en:
  `src/main/resources/com/example/juegoescritura/assets/images/`
* Fuentes en:
  `src/main/resources/com/example/juegoescritura/assets/fonts/`
* CSS:
  `src/main/resources/com/example/juegoescritura/styles.css`

El `Main` y controladores cargan recursos con `getResource(...)` o `getResourceAsStream(...)`. Verifica rutas exactas y que `src/main/resources` esté marcado como Resources Root.

---

## Detalles de implementación importantes

* **HighScore persistence**: `HighScoreManager` usa `java.util.prefs.Preferences`. Por eso el módulo debe `requires java.prefs;`.
* **Tiempo por nivel**: `GameModel.calculateTimeForLevel(level)` implementa la regla: base 20s, cada 5 niveles -2s, mínimo 2s.
* **Validación de palabra**: `GameController` valida el `txtInput` al pulsar Enter y **también automáticamente** cuando el tiempo llega a 0. (La comparación puede ser sensible o no a mayúsculas; el controlador actual puede usar `equals` para distinguir mayúsculas/minúsculas).
* **Dialogos**: Los `Alert` se muestran dentro de `Platform.runLater(...)` si se invocan desde un `Timeline` para evitar `showAndWait` durante el pulse de animación.

---

## Ficheros FXML: buenas prácticas

* Cuando declares padding con múltiples valores, usa `<padding><Insets top="..." right="..." .../></padding>` en lugar de `padding="8 0 0 0"` para evitar problemas de coerción en algunas versiones de JavaFX.
* No pongas texto plano (ej. `z`) directamente dentro de contenedores (`HBox`, `VBox`) — FXML espera nodos, eso causa `Unable to coerce ... to javafx.scene.Node`.
* Cada `onAction="#methodName"` debe corresponder a un método `@FXML` en el controlador.

---

## Problemas frecuentes y soluciones rápidas

* **`Invalid resource ... not found on the classpath`**
  → Asegúrate de que el recurso esté en `src/main/resources` y que ese folder esté marcado como Resources Root. Rebuild.

* **`Unable to coerce 8 0 0 0 to class javafx.geometry.Insets`**
  → Reemplaza `padding="8 0 0 0"` por:

  ```xml
  <padding>
    <Insets top="8" right="0" bottom="0" left="0"/>
  </padding>
  ```

* **`showAndWait is not allowed during animation`**
  → Muestra el diálogo desde `Platform.runLater(() -> alert.showAndWait());` o usa `alert.setOnHidden(...)`.

* **`Package 'java.util.prefs' is declared in module 'java.prefs'`**
  → Añade `requires java.prefs;` en `module-info.java`.

* **FXML handler/method missing**
  → Cada `onAction="#name"` debe tener `@FXML private void name()` en el controlador.

* **FXML version warning**

  > `Loading FXML document with JavaFX API of version 24 by JavaFX runtime of version 17` → Es un warning cuando usas SceneBuilder/Editor de versión distinta. No siempre bloquea la ejecución, pero si ves incompatibilidades usa Scene Builder compatible con JavaFX 17 o guarda el FXML compatible.

---

## Cómo contribuir / mejorar

* Añadir más palabras desde archivo (JSON/CSV) en lugar de la lista embebida en `GameModel`.
* Mejorar UI: añadir animaciones (fade/scale), transiciones, o una pantalla "Game Over" FXML.
* Guardar historial de partidas y estadísticas (local JSON / SQLite).
* Internacionalización (i18n) con `ResourceBundle`.

---

## Licencia (sugerida)

Si no tienes preferencia, usa **MIT License**. Crea un fichero `LICENSE` con el texto MIT para permitir que otros usen/contribuyan fácilmente.

---

## Contacto / notas finales

Este README refleja la versión actual del mini-proyecto VelociTexto, incluyendo correcciones y soluciones a problemas comunes que surgieron durante el desarrollo (rutas de recursos, módulos, FXML quirks y uso de `Preferences`). Si ves algo que no coincide con tu copia local (nombres de `fx:id`, rutas o versiones de JavaFX), adáptalo en el código y en `module-info.java`.

¡Listo para subir a GitHub! Si quieres, añado badges (build status, license) o genero `LICENSE` y `pom.xml` listos para `mvn javafx:run`.

```

Si quieres que añada badges (por ejemplo build status o license) o que cree un `LICENSE` (MIT) y un `pom.xml` compatible, te los genero ahora mismo. ¿Quieres MIT y `pom.xml`?
```

