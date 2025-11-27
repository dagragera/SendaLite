# **SendaLite**

## LOGO

  ![](img/logo/logo.png)

**Descripción del logo:**  

El logo representa una **chincheta de ubicación** sobre una **montaña**, simbolizando la idea de marcar rutas y destinos en entornos naturales.

## Integrantes

- **David Gragera Fernández** — DNI: 80085386F  
  ![Foto carnet David](img/carne/david.png)

- **Shunya Zhan** — DNI: Y1346365M  
  ![Foto carnet Shunya](img/carne/shunya.png)

## Eslogan

*“Explora. Valora. Comparte”*

## Resumen

Aplicación web minimalista para amantes de la montaña y el senderismo en general.

## Descripción

La idea es que nuestra app, **SendaLite**, sea una aplicación web ligera y minimalista para descubrir y compartir rutas al aire libre, ya sea montaña o senderos.  

Permitirá a los usuarios:

- Consultar rutas clasificadas por **dificultad** (fácil, media, difícil).  
- Ver detalles (distancia, desnivel y mapa).  
- Valorar con una **puntuación del 1 al 10**.  
- Los usuarios autenticados podrán **crear, editar y eliminar** sus rutas, así como **puntuar rutas de otros**.  

## Funcionalidades, Requisitos, “Pliego de condiciones”

### Ver listado de rutas

- La pantalla principal mostrará todas las rutas disponibles en formato de **tarjetas o lista**.  
- Cada ruta incluirá información básica para facilitar una preselección rápida.  

### Filtrar rutas por dificultad

- Filtrado por **fácil**, **media** y **difícil**, según nivel de preparación o preferencia de desafío.  

### Ver ficha de ruta

Pantalla detallada con la información de una ruta específica, similar a una ficha técnica:

- **Título y descripción:** información general.  
- **Mapa:** ubicación geográfica con posible trazado.  
- **Datos técnicos:** distancia, desnivel, tiempo estimado.  
- **Galería de fotos:** imágenes de referencia.  
- **Información del autor:** quién compartió la ruta.  
- **Historial:** fechas de creación y actualización.  
- **Sistema de valoraciones:**  
  - Solo usuarios registrados pueden votar.  
  - Escala de **1 a 10** (10 es excelente).  
  - **Puntuación media** calculada automáticamente.  
  - **Transparencia:** se muestra cuántas personas han votado.  

### Registro e inicio de sesión (email + contraseña)

- Sistema de autenticación tradicional.  
- Email y contraseña obligatorios.  
- Email de verificación (opcional pero recomendado).  
- Tras la verificación, acceso al resto de funcionalidades.  

### Perfil de usuario

Espacio personal donde cada usuario gestiona su identidad y contenido:  
- **Información pública:** nombre visible y avatar (opcional).  
- **Estadísticas:** número de rutas creadas y valoraciones realizadas.  

### Crear ruta (usuarios autenticados)

- Formulario con campos obligatorios y opcionales.  
- Validación de datos.  
- Previsualización antes de publicar.  
- Confirmación de publicación.  

### Editar/eliminar rutas propias

- Solo el **autor** puede editar o eliminar sus rutas.  

### Búsqueda por texto

Motor de búsqueda con varios criterios:  
- **Título:** palabras clave en el nombre.  
- **Ubicación:** zona geográfica o nombre del lugar.  
- **Etiquetas:** palabras clave asociadas (montaña,     bosque, etc.).  

## Funcionalidades opcionales, recomendables o futuribles


- Ordenar por puntuación media (desc/asc) y por más recientes.  
- Formularios con validaciones avanzadas y subida de fotos.  
- Mapas interactivos.  
- Búsqueda por ubicación y/o etiquetas.  
- Ver rutas del propio usuario (**Mi cuenta**).  
- Gestión básica de errores (404, 500) y mensajes de validación.  
- Marcar ruta como favorita.  
- Comentarios en rutas.

## Diagrama Entidad - Relación
![](img/entidad_relacion_sendalite.jpeg)

## Arrancar la base de datos con Docker (MySQL)

Este repositorio incluye un `docker-compose.yml` preparado para levantar una instancia de MySQL y Adminer para administración.

Pasos rápidos:

1. Arrancar los servicios:

   docker compose up -d

   (o `docker-compose up -d` si tu versión de Docker lo requiere).

2. Variables por defecto (fijadas en `docker-compose.yml`):

   - MYSQL_DATABASE=sendalite
   - MYSQL_USER=sendalite
   - MYSQL_PASSWORD=sendalite
   - MYSQL_ROOT_PASSWORD=root

3. El directorio `docker/mysql/init/` contiene los scripts `01_schema.sql` y `02_seed.sql` que se ejecutarán en el primer arranque y crearán la estructura y datos iniciales. También hay un archivo my.cnf para configurar la base de datos (acentos, ñ).

4. Para acceder a la base de datos desde Adminer: abre http://localhost:8081 y conéctate a `sendalite` usando las credenciales anteriores.

Nota: la aplicación por defecto (archivo `src/main/resources/application.properties`) está configurada para MySQL en `jdbc:mysql://localhost:3306/sendalite`.

## Ejecutar tests

- Los tests unitarios/DB usan H2 embebida (`@DataJpaTest`) por lo que no es necesario tener Docker corriendo para ejecutar `mvn test`.

- Ejecutar la suite desde la raíz del proyecto con el wrapper de Maven:

```sh
./mvnw test
```

o con Maven instalado:

```sh
mvn test
```

- Si quieres ejecutar la aplicación contra la base de datos MySQL arrancada con Docker, arranca los contenedores y luego lanza la aplicación (por ejemplo desde tu IDE) usando las credenciales del `application.properties`.

## Ejecutar la aplicación Spring Boot (Maven)

Problema común: PowerShell puede responder "'.\mvnw.cmd' no se reconoce..." al ejecutar:
.\mvnw.cmd spring-boot:run

Soluciones rápidas:
1. Asegúrate de estar en la carpeta raíz del proyecto (por ejemplo la carpeta que contiene `pom.xml`) y abre como proyecto Maven.
2. Si el wrapper existe (mvnw.cmd, mvnw, .mvn/wrapper), ejecuta:
   .\mvnw.cmd spring-boot:run

3. Si no existe el wrapper pero tienes Maven instalado:
   mvn spring-boot:run
   —o para generar el wrapper (desde una máquina con Maven):
   mvn -N io.takari:maven:wrapper
   Esto crea mvnw, mvnw.cmd y .mvn/wrapper/* en el proyecto.

4. Si no tienes mvn ni wrapper, instala Maven: https://maven.apache.org/install.html

Helper incluido:
- run-maven.ps1: detecta mvnw.cmd o mvn y ejecuta spring-boot:run automáticamente.
  Uso:
    PowerShell> .\run-maven.ps1
  Para pasar argumentos:
    PowerShell> .\run-maven.ps1 -ExtraArgs 'spring-boot:run'

## Notas finales

- He añadido scripts de inicialización en `docker/mysql/init/` y un `src/main/resources/data.sql` con ejemplos que se pueden usar para desarrollo.
- Si queréis usar PostgreSQL en lugar de MySQL, puedo añadir un `docker-compose.postgres.yml` y un `application-dev.properties` alternativo.

## Entrega2: Acceso a datos

**Referencia de entrega:** Tag: `entrega2` — Commit: `f00676a` — Fecha: `2025-11-04T19:40:30+01:00`

Para la entrega de la práctica, se ha preparado un conjunto de datos de ejemplo y scripts de inicialización que permiten levantar la base de datos y cargar datos de prueba fácilmente.

### Archivos incluidos

- `docker/mysql/init/01_schema.sql`: script para crear la estructura de tablas.  
- `docker/mysql/init/02_seed.sql`: script para cargar datos de prueba.  
- `src/main/resources/data.sql`: datos de ejemplo adicionales.  

### Instrucciones

1. Asegúrate de tener Docker corriendo con la base de datos MySQL levantada. Consulta la sección anterior si tienes dudas.  
2. Los scripts en `docker/mysql/init/` se ejecutan automáticamente en el primer arranque. Si ya has arrancado la base de datos antes, puedes reiniciarla para que se apliquen los cambios:  

   ```sh
   docker compose down
   docker compose up -d
   ```

3. Accede a Adminer y verifica que las tablas y datos se han creado correctamente. Usa las credenciales habituales para conectar.  
4. Los datos de ejemplo incluyen rutas, usuarios y valoraciones. Puedes usarlos para explorar la aplicación y realizar pruebas.

### Artifacts y release

- ZIP de la entrega: `entrega2.zip` (generado desde HEAD, en la raíz del proyecto).
- Tag publicado en GitHub: `entrega2` (referencia: https://github.com/dagragera/SendaLite/releases/tag/entrega2 ).

Notas rápidas para el profesor/ayudante:
- Para inspeccionar el ZIP: descomprimir `entrega2.zip` y abrir el proyecto con IntelliJ o usar los comandos de Maven indicados en este README.

Cómo ejecutar desde IntelliJ (Windows):
1. File → Open... → seleccionar la carpeta raíz del proyecto (por ejemplo la carpeta que contiene `pom.xml`) y abrir como proyecto Maven.
2. Esperar a que el IDE importe el proyecto y descargue dependencias.
3. Para ejecutar los tests: Run → Run 'All Tests' o abrir la clase de test y hacer Run.
4. Para ejecutar la aplicación: abrir la clase `src/main/java/unex/cume/mdai/SendaLite/SendaLiteApplication.java` y pulsar el botón Run (o Run 'SendaLiteApplication').

Cómo ejecutar desde línea de comandos (Windows PowerShell / cmd.exe):

- Ejecutar tests (sin Docker, usa H2 embebida):

```powershell
# PowerShell (desde la raíz del proyecto)
.\mvnw.cmd test
```

```cmd
REM cmd.exe
mvnw.cmd test
```

- Ejecutar la aplicación contra MySQL levantado con Docker:

```powershell
# PowerShell (desde la raíz del proyecto)
.\mvnw.cmd spring-boot:run
```

```cmd
REM cmd.exe
mvnw.cmd spring-boot:run
```
