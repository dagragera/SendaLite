Guía rápida: Cómo resolver "Unknown database 'sendalite'" al arrancar la app SendaLite

Problema:
- Spring arranca y ejecuta los scripts SQL (configuración en `src/main/resources/application.properties` con `spring.sql.init.mode=always`). El error `Unknown database 'sendalite'` indica que no existe la base de datos cuando Spring intenta conectarse.

Opciones para resolverlo (elige una):

1) Arrancar la base de datos incluida en el proyecto con Docker (recomendado si usas Docker)

Desde la raíz del proyecto (cmd.exe):

```cmd
docker-compose up -d
```

Esto crea un contenedor MySQL que, en la primera ejecución, inicializa la base `sendalite` ejecutando los scripts en `docker/mysql/init/`.

Comprobar estado del contenedor y logs (cmd.exe):

```cmd
docker-compose ps
docker-compose logs db --tail=200
```

Acceder con Adminer (navegador): http://localhost:8081
Usuario/BD/contraseña por defecto (según `docker-compose.yml`):
- Usuario: sendalite
- Contraseña: sendalite
- Base: sendalite

-- Script para crear la base de datos 'sendalite' y el usuario localmente
2) Crear la base localmente si no usas Docker

- Usa el script `scripts/create_sendalite_db.sql` creado en este repo.

Ejemplo (cmd.exe) si tienes cliente mysql instalado y acceso root:

```cmd
REM Ir a la carpeta del proyecto
cd C:\path\to\SendaLite

REM Ejecutar el script que crea la BD y el usuario
mysql -u root -p < "scripts\create_sendalite_db.sql"

REM Importar esquema y datos (si no se importaron automáticamente):
mysql -u root -p < "docker\mysql\init\01_schema.sql"
mysql -u root -p < "docker\mysql\init\02_seed.sql"
```

También puedes ejecutar las instrucciones manualmente en un cliente SQL:

```sql
CREATE DATABASE IF NOT EXISTS `sendalite` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'sendalite'@'localhost' IDENTIFIED BY 'sendalite';
GRANT ALL PRIVILEGES ON `sendalite`.* TO 'sendalite'@'localhost';
FLUSH PRIVILEGES;
```

3) Alternativa temporal: evitar la inicialización automática de `data.sql`

Si quieres que la app no falle por ahora mientras solucionas la BD, edita `src/main/resources/application.properties` y cambia:

spring.sql.init.mode=always

por

spring.sql.init.mode=never

Con esto la app no intentará ejecutar `data.sql` automáticamente (pero la base debe existir para que la app funcione).

Diagnóstico rápido que hice en tu repo:
- `application.properties` apunta a `jdbc:mysql://localhost:3306/sendalite` y tiene `spring.sql.init.mode=always` (por eso Spring intenta ejecutar scripts en arranque).
- En `docker-compose.yml` está configurado para crear la BD `sendalite` y montar `docker/mysql/init` para ejecutar `01_schema.sql` y `02_seed.sql` al inicializar el contenedor.
- Los scripts en `docker/mysql/init/` usan `USE sendalite;`, por eso MySQL debe tener ya la base creada o bien el contenedor debe ejecutar estos scripts en su primera inicialización.

Siguiente paso que puedo hacer por ti (elige):
- Añadir instrucciones al `README.md` (ya añadí `docs/DB_SETUP.md`) y/o modificar `application.properties` temporalmente para evitar la excepción.
- Crear y ejecutar comandos en el repositorio (por ejemplo combinar/importar los SQL) — necesito tu confirmación para modificar archivos del proyecto o ejecutar comandos aquí.

¿Qué prefieres que haga ahora? Puedo:
- (A) Instruirte para arrancar Docker y comprobar que el contenedor crea la BD (te mostré los comandos), o
- (B) Cambiar `application.properties` para desactivar la inicialización automática (temporal), o
- (C) Crear un script adicional/Makefile para facilitar la importación local y actualizar el README.

Indica la opción (A/B/C) y lo hago automáticamente en el repo (crear archivo o editar). Si prefieres, dime también si usas Docker o MySQL local.
-- Ejecutar con un cliente mysql (como root) o con las credenciales adecuadas

-- 1) Crear la base de datos con codificación UTF-8
CREATE DATABASE IF NOT EXISTS `sendalite` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2) Crear/asegurar el usuario y permisos (ajusta host si es necesario)
-- Si ya existe el usuario, la instrucción siguiente puede fallar en algunas versiones; en ese caso usa ALTER USER o DROP+CREATE.
CREATE USER IF NOT EXISTS 'sendalite'@'localhost' IDENTIFIED BY 'sendalite';
GRANT ALL PRIVILEGES ON `sendalite`.* TO 'sendalite'@'localhost';
FLUSH PRIVILEGES;

-- 3) Importar esquema y datos (si tienes los archivos 01_schema.sql y 02_seed.sql)
-- Desde cmd.exe (ejemplo):
-- mysql -u root -p < "C:\path\to\SendaLite\docker\mysql\init\01_schema.sql"
-- mysql -u root -p < "C:\path\to\SendaLite\docker\mysql\init\02_seed.sql"

-- Nota: los archivos de la carpeta docker/mysql/init ya usan `USE sendalite;` por lo que no es necesario especificar la base en la línea de comando.

