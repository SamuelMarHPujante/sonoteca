# SonoTeca

Aplicación de escritorio para gestionar una biblioteca musical (géneros, artistas, álbumes, canciones y listas de reproducción), usada como proyecto base en el módulo de Acceso a Datos (2º DAM).

## Cómo está organizado el proyecto

- **`modelo`** — Las clases del dominio (`Genero`, `Artista`, `Album`, `Cancion`, `ListaReproduccion`, `Biblioteca`). Se usan en todas las unidades; en la unidad 4 se les añaden anotaciones JPA.
- **`front`** — La interfaz gráfica (Swing), **ya completa**. No hace falta tocar nada de este paquete.
- **`repositorio`** — Contiene la interfaz `RepositorioSonoTeca` (el contrato que usa el front) y una implementación por unidad:
  - `repositorio.memoria.RepositorioMemoria` — completa, con datos de ejemplo, para poder ejecutar la aplicación desde el primer día.
  - `repositorio.ficheros` — **Unidad 2**.
  - `repositorio.jdbc` — **Unidad 3**.
  - `repositorio.hibernate` — **Unidad 4**.
  - `repositorio.mongo` — **Unidad 6**.
- **`util`** — `Duracion` (unidad 1) y `JsonUtils` (unidades 1 y 2).

La **unidad 5** (Spring) es un proyecto aparte: `sonoteca-api` (junto a este).

## Cómo ejecutar la aplicación

```bash
mvn compile exec:java -Dexec.mainClass=es.iesejemplo.sonoteca.Main
```

o, desde el IDE, ejecuta directamente la clase `es.iesejemplo.sonoteca.Main`.

Por defecto se usa `RepositorioMemoria`, así que la aplicación funciona sin ninguna configuración adicional. A medida que completes cada unidad, cambia la línea señalada en `Main.java` por tu propia implementación.

## Cómo saber qué tengo que hacer en cada unidad

Busca `TODO: implementar (unidad N...)` en el código: cada uno indica qué método tienes que completar y en qué ejercicio de la práctica corresponde. Si ejecutas la aplicación y pulsas un botón cuya funcionalidad depende de un método sin implementar, verás un aviso claro en pantalla (no un error críptico) indicando que esa parte todavía no está hecha.
