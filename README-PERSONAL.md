# README - Uso personal (Library System)

Resumen rápido: este README describe los pasos y comandos para compilar y ejecutar la aplicación localmente en modo H2 (por defecto) y opciones útiles.

## Requisitos
- Java 17+ (se probó con Java 21)
- Maven instalado o usar `mvn` (el proyecto incluye `.mvn/wrapper` pero no `mvnw`)
- (Opcional) Docker y Docker Compose si quieres usar PostgreSQL en contenedores

## Credenciales por defecto (modo desarrollo)
- Usuario: `admin`
- Contraseña: `admin123`

> Archivo con las credenciales: `src/main/resources/application.properties`

## Comandos básicos
Abrir una terminal en la raíz del proyecto (`libreria-project`).

1) Compilar e instalar en el repositorio local (sin ejecutar tests):

```bash
mvn clean install -DskipTests
```

2) Ejecutar la aplicación con Maven (arranca Tomcat embebido en el puerto 8080):

```bash
mvn spring-boot:run
```

3) Generar el JAR y ejecutarlo:

```bash
mvn package
java -jar target/library-system-0.0.1-SNAPSHOT.jar
```

4) Forzar otro puerto (ejecutando con Maven o con JAR):

```bash
# con maven
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"

# con jar
java -jar target/library-system-0.0.1-SNAPSHOT.jar --server.port=9090
```

## Endpoints principales
- GET  /api/v1/books         — listar todos (requiere autenticación por defecto)
- GET  /api/v1/books/{id}    — obtener libro por id
- POST /api/v1/books         — crear libro
- PUT  /api/v1/books/{id}    — actualizar
- DELETE /api/v1/books/{id}  — eliminar
- GET  /                     — redirige a `/api/v1/books`

Ejemplo usando `curl` (autenticación básica):

```bash
curl -u admin:admin123 http://localhost:8080/api/v1/books
```

Ejemplo crear libro (JSON):

```bash
curl -u admin:admin123 -H "Content-Type: application/json" -d '{"title":"Mi Libro","author":"Autor","isbn":"12345"}' -X POST http://localhost:8080/api/v1/books
```

## H2 Console (base de datos en memoria)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:librarydb`
- User: `SA`
- Password: (vacío)

## Usar PostgreSQL con Docker Compose (opcional)
El proyecto incluye `docker-compose.yml`. Si prefieres PostgreSQL en contenedores:

1. Ajusta `pom.xml` para usar el driver `postgresql` (reemplaza `h2` por `postgresql`).
2. Restaura `src/main/resources/application.properties` con la URL de PostgreSQL:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/librarydb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

3. Ejecuta (requiere Docker):

```bash
docker-compose up -d
```
mvn spring-boot:run
4. Espera que el servicio esté disponible y luego arranca la app.

(Nota: la versión actual del proyecto está configurada para H2 por conveniencia.)

## Troubleshooting rápido
- Puerto 8080 ocupado: identifica proceso y termínalo

```powershell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

- Si ves errores de dependencias: limpia y reconstruye

```bash
mvn clean
mvn install -DskipTests
```

- Ver registros: revisa la salida del comando `mvn spring-boot:run` o el archivo `target/*` si ejecutas el JAR.

## Notas de seguridad
- Las credenciales `admin/admin123` están solo para desarrollo. Cámbialas antes de exponer la app.
- Para producción, configura un almacén de contraseñas seguro y no uses H2 en memoria.

---
Archivo creado por uso personal. Ajusta cualquier comando según tu entorno.
