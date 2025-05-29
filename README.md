# CryptoTradeTest
Cryptocurrency exchange platform
---
## ⚙️ Instrucciones de instalación

### Requisitos previos

- **Java 17** o superior instalado y configurado en tu sistema.
- **Maven 3.6+** para gestionar dependencias y construir el proyecto.
- **PostgreSQL 10** o superior instalado y en ejecución.
- **Git** para clonar el repositorio.

### Pasos para instalar y ejecutar la API CryptoTrade

1. **Clona el repositorio del proyecto:**


2. **Configura la base de datos PostgreSQL:**
- Crea la base de datos `CryptoTradeDB` (puedes usar pgAdmin o consola psql):
- Asegúrate que las tablas y esquemas estén creados (puedes usar el script SQL de backup o migraciones si las tienes).

3. **Configura las propiedades de conexión y JWT:**
  Edita el archivo `src/main/resources/application.properties` y ajusta los valores:

4. **Construye y ejecuta la aplicación:**
   **Construir el proyecto**
  Ejecuta alguno de estos comandos en la terminal
    - Para saltar solo la ejecución de tests. >mvn clean install -DskipTests
    - Para saltar compilación y ejecución de tests (más rápido). >mvn clean install -Dmaven.test.skip=true
    - Solo empaquetar sin tests >mvn package -DskipTests
        o >mvn package -Dmaven.test.skip=true      
  **Ejecutar la api** 
      - en la terminal ejecuta java -jar target/crypto-trade-api-0.0.1-SNAPSHOT.jar

5. **Verifica que la API esté corriendo:**
  - Descarga o clona la colección desde Git
  - Importar la colección en Postman
  - Configurar variables de entorno (si aplica)
      Si la colección usa variables (como {{token}} para el JWT), crea un entorno en Postman y define esas variables con los valores correspondientes.
      Por ejemplo, después de hacer login con la API, copia el token JWT y asígnalo a la variable token para usarlo en las demás peticiones.
  - Ejecutar peticiones para probar la API

## 📡 Endpoints de la API CryptoTrade

A continuación se describen los principales endpoints disponibles, su método HTTP, ruta, propósito y ejemplos básicos de uso.

---

### 1. Usuarios y Sesiones

| Método | Ruta                 | Descripción                              |
|--------|--------------------------------------|----------------------------------------|
| POST   | `http://localhost:4668/auth/register`| Registra un nuevo usuario.               |
| POST   | `http://localhost:4668/auth/login`   | Autentica un usuario y devuelve un token JWT.|
| POST   | `http://localhost:4668/auth/logout`  | Cierra una sesión específica.            |

---

**Ejemplo:**
{
"name": "http://localhost:4668/auth/register",
"request": {
"method": "POST",
"header": [
{
"key": "Authorization",
"value": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwic2Vzc2lvbklkIjoiYzJhYjMxZGMtZDIzNy00MzI3LWJmM2EtMmZhNWU4OTg3ZmVkIiwiaWF0IjoxNzQ4NDk2MTY0LCJleHAiOjE3NDg0OTk3NjR9.ohDiwOW4yoru_6SXlVBGg3kYfu6SVrhOSRz32q63Czk",
"type": "text",
"disabled": true
}
],
"body": {
"mode": "raw",
"raw": "{\r\n  \"email\": \"usuario3@example.com\",\r\n  \"password\": \"tu_contraseña123\"\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": "http://localhost:4668/auth/register"
},
"response": []
},
"url": "http://localhost:4668/auth/register"
},
"response": []
}

{
"name": "http://localhost:4668/auth/login",
"request": {
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n  \"email\": \"usuario@example.com\",\r\n  \"password\": \"tu_contraseña123\"\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": "http://localhost:4668/auth/login"
},
"response": []
}

{
"name": "http://localhost:4668/auth/logout",
"request": {
"auth": {
"type": "bearer",
"bearer": {
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwic2Vzc2lvbklkIjoiZjM1N2U2OTMtZDI1Yy00YThkLWI5NGYtOGE1Y2FkMTkwODY0IiwiaWF0IjoxNzQ4NTE1OTgxLCJleHAiOjE3NDg1MTk1ODF9.UQaX_m1QPjSJ3yEa-RO-uFrMEGuoPBy4AhdU30nUYMY"
}
},
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "",
"options": {
"raw": {
"language": "json"
}
}
},
"url": "http://localhost:4668/auth/logout"
},
"response": []
}
---

### 2. Criptomonedas

| Método | Ruta                   | Descripción                                         |
|--------|------------------------|---------------------------------------------------|
| GET    | `http://localhost:4668/api/criptomoneda`| Obtiene lista de criptomonedas, opcionalmente filtradas por moneda. (requiere autenticación). |
| GET    | `http://localhost:4668/api/criptomoneda?moneda=simbolo_moneda`| Obtiene lista de criptomonedas filtrada por simbolo de cripto moneda. (requiere autenticación). |
| POST   | `http://localhost:4668/api/criptomonedas/crear_criptomoneda` | Crea una nueva criptomoneda (requiere autenticación). |

**Ejemplo:**
{
"name": "http://localhost:4668/api/criptomoneda",
"protocolProfileBehavior": {
"disableBodyPruning": true
},
"request": {
"auth": {
"type": "bearer",
"bearer": {
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwic2Vzc2lvbklkIjoiOWRmZGIwNDgtNjAxNS00ZDY4LTkxMzEtYzE5MDU3OTg5YWI0IiwiaWF0IjoxNzQ4NTI4ODI0LCJleHAiOjE3NDg1MzI0MjR9.oqGRL5vJSDOnOirsee0nse3V7mpMSz81cFY0nRGQ0tA"
}
},
"method": "GET",
"header": [],
"body": {
"mode": "raw",
"raw": " {\r\n        \"symbol\": \"MCC\",\r\n        \"name\": \"My Crypto Coin\",\r\n        \"description\": \"Cripto moneda de prueba1\"\r\n    }",
"options": {
"raw": {
"language": "json"
}
}
},
"url": "http://localhost:4668/api/criptomoneda"
},
"response": []
}

{
"name": "http://localhost:4668/api/criptomonedas/crear_criptomoneda",
"request": {
"auth": {
"type": "bearer",
"bearer": {
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwic2Vzc2lvbklkIjoiOWRmZGIwNDgtNjAxNS00ZDY4LTkxMzEtYzE5MDU3OTg5YWI0IiwiaWF0IjoxNzQ4NTI4ODI0LCJleHAiOjE3NDg1MzI0MjR9.oqGRL5vJSDOnOirsee0nse3V7mpMSz81cFY0nRGQ0tA"
}
},
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n  \"symbol\": \"OBC\",\r\n  \"name\": \"Otro Bit Coin\",\r\n  \"description\": \"Cripto moneda OBT\",\r\n  \"currencySymbols\": [\"USD\"]\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": "http://localhost:4668/api/criptomonedas/crear_criptomoneda"
},
"response": []
}

{
"name": "http://localhost:4668/api/criptomoneda?moneda=USD",
"protocolProfileBehavior": {
"disableBodyPruning": true
},
"request": {
"auth": {
"type": "bearer",
"bearer": {
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwic2Vzc2lvbklkIjoiOWRmZGIwNDgtNjAxNS00ZDY4LTkxMzEtYzE5MDU3OTg5YWI0IiwiaWF0IjoxNzQ4NTI4ODI0LCJleHAiOjE3NDg1MzI0MjR9.oqGRL5vJSDOnOirsee0nse3V7mpMSz81cFY0nRGQ0tA"
}
},
"method": "GET",
"header": [],
"body": {
"mode": "raw",
"raw": " {\r\n        \"symbol\": \"MCC\",\r\n        \"name\": \"My Crypto Coin\",\r\n        \"description\": \"Cripto moneda de prueba1\"\r\n    }",
"options": {
"raw": {
"language": "json"
}
}
},
"url": {
"raw": "http://localhost:4668/api/criptomoneda?moneda=USD",
"protocol": "http",
"host": [
"localhost"
],
"port": "4668",
"path": [
"api",
"criptomoneda"
],
"query": [
{
"key": "moneda",
"value": "USD"
}
]
}
},
"response": []
}

---

### 3. Monedas Fiat

| Método | Ruta               | Descripción                              |
|--------|--------------------|----------------------------------------|
| GET    | `http://localhost:4668/api/moneda`  | Lista todas las monedas(requiere autenticación).|
| POST   | `http://localhost:4668/api/moneda/crear_nueva_moneda`  | Crea una nueva moneda(requiere autenticación). |

**Ejemplo:**
{
"name": "http://localhost:4668/api/moneda",
"protocolProfileBehavior": {
"disableBodyPruning": true
},
"request": {
"auth": {
"type": "bearer",
"bearer": {
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwic2Vzc2lvbklkIjoiOWRmZGIwNDgtNjAxNS00ZDY4LTkxMzEtYzE5MDU3OTg5YWI0IiwiaWF0IjoxNzQ4NTI4ODI0LCJleHAiOjE3NDg1MzI0MjR9.oqGRL5vJSDOnOirsee0nse3V7mpMSz81cFY0nRGQ0tA"
}
},
"method": "GET",
"header": [],
"body": {
"mode": "raw",
"raw": "",
"options": {
"raw": {
"language": "json"
}
}
},
"url": "http://localhost:4668/api/moneda"
},
"response": []
}

{
"name": "http://localhost:4668/api/moneda/crear_nueva_moneda",
"request": {
"auth": {
"type": "bearer",
"bearer": {
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwic2Vzc2lvbklkIjoiOWRmZGIwNDgtNjAxNS00ZDY4LTkxMzEtYzE5MDU3OTg5YWI0IiwiaWF0IjoxNzQ4NTI4ODI0LCJleHAiOjE3NDg1MzI0MjR9.oqGRL5vJSDOnOirsee0nse3V7mpMSz81cFY0nRGQ0tA"
}
},
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": " {\r\n        \"symbol\": \"USD\",\r\n        \"name\": \"Dolar Americano\",\r\n        \"country\": \"UUEE\"\r\n    }",
"options": {
"raw": {
"language": "json"
}
}
},
"url": "http://localhost:4668/api/moneda/crear_nueva_moneda"
},
"response": []
}

---

### Notas importantes

- Los endpoints que requieren autenticación deben incluir el token JWT en el encabezado HTTP:

- Códigos de respuesta comunes:
  - `200 OK`: Operación exitosa.
  - `201 Created`: Recurso creado.
  - `400 Bad Request`: Datos inválidos.
  - `401 Unauthorized`: Token inválido o ausente.
  - `403 Forbidden`: Permiso denegado.
  - `404 Not Found`: Recurso no encontrado.

---

## 🗂️ Estructura de paquetes recomendada
Para mantener el proyecto organizado, escalable y fácil de mantener, se estructuro en los paquetes siguiendo una arquitectura por capas, separando claramente responsabilidades:
io.cryptotrade
├── config          // Configuraciones de Spring, seguridad y beans
├── controller      // Controladores REST que exponen los endpoints
├── model           // Entidades JPA que representan las tablas de la base de datos
├── repository      // Repositorios JPA para acceso a datos
├── security        // Clases relacionadas con JWT, filtros de seguridad y servicios de autenticación
├── service         // Lógica de negocio y manejo de transacciones



## 🗂️ Esquema básico de la base de datos

### DataBase: `crypto_trade_db`
### Owner: `crypto_trade_db`
### Password:`12345`

### Schema: `users`

- **user**: tabla que almacena usuarios con columnas:
  - `id` (integer, clave primaria, autoincremental)
  - `email` (varchar(255), único, no nulo)
  - `password_hash` (text, no nulo)
  - `password_expires_at` (timestamp, no nulo, valor por defecto: `now() + interval '90 days'`)
  - `created_at` (timestamp, valor por defecto: `now()`)

- **password_histories**: historial de contraseñas con columnas:
  - `id` (integer, clave primaria, autoincremental)
  - `user_id` (integer, clave foránea a `users.user.id`, no nulo)
  - `password_hash` (text, no nulo)
  - `created_at` (timestamp, valor por defecto: `now()`)

- **sessions**: sesiones activas con columnas:
  - `id` (uuid, clave primaria, valor por defecto: `gen_random_uuid()`)
  - `user_id` (integer, clave foránea a `users.user.id`, no nulo)
  - `session_start` (timestamp, no nulo, valor por defecto: `now()`)
  - `last_activity` (timestamp, no nulo, valor por defecto: `now()`)
  - `is_active` (boolean, no nulo, valor por defecto: `true`)
  - `ip_address` (inet, nullable)

---

### Schema: `currencies`

- **currency**: tabla que almacena monedas fiat con columnas:
  - `id` (integer, clave primaria, autoincremental)
  - `symbol` (varchar(10), único, no nulo) — Ej: USD, EUR, JPY
  - `name` (varchar(100), no nulo) — Ej: United States Dollar, Euro, Japanese Yen
  - `country` (varchar(100), nullable)
  - `created_at` (timestamp, valor por defecto: `now()`)

- **crypto_coin**: tabla que almacena criptomonedas con columnas:
  - `id` (integer, clave primaria, autoincremental)
  - `symbol` (varchar(10), único, no nulo) — Ej: BTC, ETH, USDT
  - `name` (varchar(100), no nulo) — Ej: Bitcoin, Ethereum, Tether
  - `description` (text, nullable)
  - `created_at` (timestamp, valor por defecto: `now()`)

- **crypto_coin_currencies**: tabla intermedia que relaciona criptomonedas con monedas fiat con columnas:
  - `crypto_coin_id` (integer, clave foránea a `currencies.crypto_coin.id`, no nulo)
  - `currency_id` (integer, clave foránea a `currencies.currency.id`, no nulo)
  - Clave primaria compuesta: (`crypto_coin_id`, `currency_id`)

---

## 📚 Documentación adicional

- Para más detalles, revisa la carpeta `/database` donde se encuentran backups y diagramas ER.
- Puedes usar Postman importando el archivo `/postman/CryptoTrade.postman_collection.json` para probar la API fácilmente.

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor abre un issue o pull request para sugerir mejoras o reportar errores.

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT.

---
*¡Gracias por usar CryptoTrade API!*
