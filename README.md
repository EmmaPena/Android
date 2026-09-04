# App de Notas en Java 

Aplicación móvil desarrollada en **Android (Java)** que se conecta a una API REST desplegada en **Render** con persistencia de datos en **PostgreSQL**.

## Características
* **Conexión a Backend en la nube:** consume la API desplegada en Render (`https://mis-notas-api.onrender.com/`).
* **Peticiones HTTP con Retrofit:** consumo de endpoints `GET /notas` y `POST /notas` de forma asíncrona.
* **Persistencia permanente:** los datos persisten en base de datos PostgreSQL alojada en Render.
* **Interfaz limpia:** ingreso de texto mediante `EditText`, envío vía `Button` y despliegue de lista mediante `TextView`.

## Tecnologías Utilizadas
* **Lenguaje:** Java
* **Librerías principales:**
  * [Retrofit 2](https://square.github.io/retrofit/) - Cliente HTTP para Android.
  * [Gson Converter](https://github.com/google/gson) - Conversión automática de JSON a objetos Java.
* **Backend compatible:** Node.js / Express con PostgreSQL en Render.

## Instalación
1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/EmmaPena/Android.git](https://github.com/EmmaPena/Android.git)