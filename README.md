# App de Notas en Java

Aplicación móvil desarrollada en **Android (Java)** que se conecta a una API REST desplegada en **Render** con persistencia de datos en **PostgreSQL**.

## Características
* **Diseño Moderno y Responsivo:** Interfaz adaptativa en modo oscuro (*Dark Theme*) construida con `ConstraintLayout` y `CardView`, optimizada con `fitsSystemWindows` y manejo avanzado del teclado suave (`adjustResize`).
* **Búsqueda en Tiempo Real:** Filtrado dinámico de notas mediante `SearchView`.
* **Lista Dinámica con RecyclerView:** Renderizado eficiente de notas breves y bloques extensos de texto mediante tarjetas (`CardView`) y adaptador personalizado (`NotasAdapter`).
* **Soporte Multilenguaje (i18n):** Detección automática del idioma del sistema (Español e Inglés) utilizando recursos `strings.xml`.
* **Conexión a Backend en la nube:** Consume la API desplegada en Render (`https://mis-notas-api.onrender.com/`).
* **Peticiones HTTP con Retrofit:** Consumo de endpoints `GET /notas` y `POST /notas` de forma asíncrona.
* **Persistencia permanente:** Los datos persisten en una base de datos PostgreSQL alojada en Render.

## Tecnologías Utilizadas
* **Lenguaje:** Java
* **UI/UX:** `ConstraintLayout`, `RecyclerView`, `CardView`, `SearchView`, Material Design.
* **Librerías principales:**
    * [Retrofit 2](https://square.github.io/retrofit/) - Cliente HTTP para Android.
    * [Gson Converter](https://github.com/google/gson) - Conversión automática de JSON a objetos Java.
* **Backend compatible:** Node.js / Express con PostgreSQL en Render.

## Instalación y Uso
1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/EmmaPena/Android.git](https://github.com/EmmaPena/Android.git)