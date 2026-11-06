# TiendaApp - API REST con JWT y Roles

API REST de una tienda en línea con autenticación JWT y control de roles (ADMIN, CLIENTE, VENDEDOR).

## Requisitos

- Java 17+
- Maven
- MySQL

## Variables de entorno

Antes de correr el proyecto configura estas variables de entorno:

DB_URL=jdbc:mysql://localhost:3306/tienda_db
DB_USERNAME=tu_usuario_mysql
DB_PASSWORD=tu_contraseña_mysql
JWT_SECRET=tu_secreto_base64
JWT_EXPIRATION=600000


Para generar el secreto JWT puedes usar:

openssl rand -base64 32


## Configuración de la base de datos

1. Crea el schema en MySQL:
```sql
CREATE SCHEMA tienda_db;
```

2. Ejecuta el script `tienda.sql` que está en la raíz del proyecto.

## Cómo correr el proyecto

1. Clona el repositorio:

git clone <url-del-repositorio>


2. Entra a la carpeta del proyecto:

cd tiendaApp


3. Configura las variables de entorno.

4. Corre el proyecto:

mvn spring-boot:run


La API estará disponible en:

http://localhost:9090/api/v1/tienda


## Credenciales del ADMIN inicial

| Campo    | Valor             |
|----------|-------------------|
| Username | admin             |
| Email    | admin@tienda.com  |
| Password | 123456            |

## Endpoints principales

| Método | Ruta                        | Rol                        |
|--------|-----------------------------|----------------------------|
| POST   | /auth/register              | Público                    |
| POST   | /auth/login                 | Público                    |
| POST   | /auth/refreshtoken          | Público                    |
| GET    | /products                   | Público                    |
| GET    | /products/{id}              | Público                    |
| GET    | /products/mine              | VENDEDOR                   |
| POST   | /products                   | VENDEDOR                   |
| PUT    | /products/{id}              | VENDEDOR, ADMIN            |
| DELETE | /products/{id}              | VENDEDOR, ADMIN            |
| GET    | /users/me                   | ADMIN, CLIENTE, VENDEDOR   |
| GET    | /users                      | ADMIN                      |
| POST   | /users                      | ADMIN                      |
| PATCH  | /users/{id}/role            | ADMIN                      |
| PATCH  | /users/{id}/status          | ADMIN                      |
| GET    | /categories                 | ADMIN, CLIENTE, VENDEDOR   |
| POST   | /categories                 | ADMIN                      |
| DELETE | /categories/{id}            | ADMIN                      |
| POST   | /orders                     | CLIENTE                    |
| GET    | /orders/mine                | CLIENTE                    |
| GET    | /orders/sales               | VENDEDOR                   |
| GET    | /orders                     | ADMIN                      |
| PATCH  | /orders/{id}/status         | VENDEDOR, ADMIN            |
| PATCH  | /orders/{id}/cancel         | CLIENTE        