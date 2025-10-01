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
