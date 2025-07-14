

# 🎾 Tennis Tournaments API

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?logo=spring)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue?logo=mysql)
![Security](https://img.shields.io/badge/Security-JWT%20%7C%20Spring%20Security-red?logo=springsecurity)
![Last Commit](https://img.shields.io/github/last-commit/RodriLang/tennis-tournaments-API)
![License](https://img.shields.io/github/license/RodriLang/tennis-tournaments-API)

> API REST para gestionar torneos de tenis, con control de usuarios, roles y seguridad basada en JWT.

---

## 🚀 Tecnologías utilizadas

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Security**
- **JWT (JSON Web Token)**
- **Lombok**
- **MySQL / H2 (dev)**
- **Swagger / OpenAPI**
- **Maven**

---

## 🎯 Funcionalidades principales

- ABM de jugadores
- Gestión de torneos: crear, iniciar, avanzar rondas
- Generación de partidos por eliminación directa
- Carga y modificación de resultados (individual o por ronda)
- Determinación automática de ganadores
- Control de usuarios con roles (`ROOT`, `ADMIN`, `JUDGE`, `PLAYER`)
- Autenticación vía JWT
- Seguridad declarativa con `@PreAuthorize`

---

## 📁 Estructura del proyecto

src/
└── main/
├── java/dev/rodrilang/tennis_tournaments/
│ ├── controllers/
│ ├── configs/
│ ├── dtos/
│ ├── enums/
│ ├── exceptions/
│ ├── mappers/
│ ├── models/
│ ├── repositories/
│ ├── security/
│ ├── services/
│ ├── strategy/
│ ├── utils/
│ ├── validations/
│ └── TennisTournamentsApplication.java
└── resources/
└── application.properties


---

## 📚 API Reference

> Todas las rutas están versionadas bajo `/api/v1/*`  
> Las rutas protegidas requieren token JWT

### 🧑‍⚖️ Autenticación y Credenciales

| Método | Endpoint                           | Descripción                      | Rol necesario
|--------|------------------------------------|----------------------------------|--------------
| POST   | `/auth/login`                      | Iniciar sesión y obtener token   | —
| POST   | `/credentials/register`            | Registrar nuevo usuario          | ROOT / ADMIN
| GET    | `/credentials`                     | Listar usuarios                  | ROOT
| DELETE | `/credentials/revoke/{id}`         | Eliminar usuario                 | ROOT
| PATCH  | `/credentials/change-password`     | Cambiar contraseña               | Autenticado

### 🎾 Torneos

| Método | Endpoint                                             | Descripción                           | Rol necesario
|--------|------------------------------------------------------|---------------------------------------|---------------
| POST   | `/tournaments`                                       | Crear torneo                          | ADMIN
| GET    | `/tournaments`                                       | Listar torneos                        | —
| GET    | `/tournaments/{id}`                                  | Detalle torneo                        | —
| PUT    | `/tournaments/{id}`                                  | Actualizar torneo                     | ADMIN
| DELETE | `/tournaments/{id}`                                  | Eliminar torneo                       | ADMIN
| PATCH  | `/tournaments/{id}/start`                            | Iniciar torneo                        | ADMIN / JUDGE
| PATCH  | `/tournaments/{id}/next-round`                       | Avanzar ronda                         | ADMIN / JUDGE
| GET    | `/tournaments/{id}/players`                          | Jugadores del torneo                  | —
| GET    | `/tournaments/{id}/rounds`                           | Rondas del torneo                     | —
| GET    | `/tournaments/{id}/winner`                           | Ganador del torneo                    | —
| POST   | `/tournaments/{id}/players/{dni}`                    | Registrar jugador                     | ADMIN
| DELETE | `/tournaments/{id}/players/{dni}`                    | Desinscribir jugador                  | ADMIN
| PATCH  | `/tournaments/{id}/matches/{mId}/result`             | Asignar resultado de partido          | ADMIN / JUDGE
| PUT    | `/tournaments/{id}/matches/{mId}/result`             | Modificar resultado de partido        | ADMIN
| PATCH  | `/tournaments/{id}/rounds/{roundType}/results`       | Asignar resultados de ronda           | ADMIN / JUDGE
| PUT    | `/tournaments/{id}/rounds/{roundType}/results`       | Modificar resultados de ronda         | ADMIN

### 👤 Jugadores

| Método | Endpoint                          | Descripción                         | Rol necesario
|--------|-----------------------------------|-------------------------------------|---------------
| POST   | `/players`                        | Crear jugador                       | ADMIN
| PUT    | `/players/{dni}`                  | Actualizar jugador                  | ADMIN
| PATCH  | `/players/{dni}/points?delta=n`   | Ajustar puntaje                     | ADMIN
| DELETE | `/players/{dni}`                  | Baja lógica del jugador             | ADMIN
| PATCH  | `/players/{dni}/restore`          | Restaurar jugador                   | ADMIN
| GET    | `/players/{dni}`                  | Obtener por DNI                     | ADMIN
| GET    | `/players`                        | Listar jugadores                    | ADMIN

### 🏟️ Partidos

| Método | Endpoint                         | Descripción                         | Rol necesario
|--------|----------------------------------|-------------------------------------|---------------
| GET    | `/matches`                       | Listar todos los partidos           | —
| GET    | `/matches/{id}`                  | Detalle de un partido               | —
| GET    | `/matches/player/{dni}`          | Partidos jugados por jugador        | —
| GET    | `/matches/{id}/winner`           | Ganador del partido                 | —

---

## 🔐 Seguridad y Autenticación

Este proyecto utiliza **JWT** para autenticación y **Spring Security** para control de acceso.

- Para acceder a rutas protegidas, primero se debe hacer login:

```bash
POST /api/v1/auth/login
{
  "username": "admin",
  "password": "admin123"
}

    El token retornado debe incluirse en los headers:

Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

🧪 Swagger UI

La documentación interactiva de la API está disponible al levantar el proyecto:

    🌐 http://localhost:8080/swagger-ui.html

⚙️ Configuración
Variables importantes (application.properties)

# Puerto
server.port=8080

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/tennis-tournaments
spring.datasource.username=usuario
spring.datasource.password=clave
spring.jpa.hibernate.ddl-auto=update

# JWT
app.jwt.secret=secret-key-super-secreta
app.jwt.expiration=3600000

Requisitos

    JDK 21

    Maven 3+

    MySQL (o H2 para pruebas)

Ejecución local

git clone https://github.com/RodriLang/tennis-tournaments-API.git
cd tennis-tournaments-API
mvn spring-boot:run

📌 Posibles mejoras futuras

    Validación de inscripciones en base a cupos

    Sistema de ranking por puntos acumulados

    Soporte para torneos en grupos (no eliminatorios)

    Cliente frontend con React

    Carga de fixture desde archivo

👨‍💻 Autor

Rodrigo Lang
Desarrollador Backend | UTN Mar del Plata
📘 LinkedIn
📂 Repositorio
📄 Licencia

Este proyecto está licenciado bajo los términos de la MIT License.