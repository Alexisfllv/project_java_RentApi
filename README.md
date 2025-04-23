# 🏨 RentApi - Sistema de Gestión de Reservas y Habitaciones

Una API REST desarrollada en Java con Spring Boot para gestionar habitaciones y reservas de un sistema de alquiler, con validaciones, documentación Swagger, pruebas unitarias y arquitectura en capas.

---

## ⚙️ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.4.4**
- **Spring Web / Spring Data JPA**
- **Hibernate**
- **MySQL**
- **Lombok**
- **MapStruct**
- **Bean Validation (JSR-380)**
- **Swagger / OpenAPI (springdoc-openapi)**
- **Mockito & JUnit 5** (para pruebas)
- **JaCoCo** (cobertura de código)

---

## 📁 Estructura del Proyecto

```
com.ejemplo.rentapi
├── controller         # Controladores REST
├── dto               # Clases de transferencia de datos
├── entity            # Entidades JPA
├── exception         # Manejo de errores global
├── mapper            # MapStruct: mapeo entity <-> dto
├── repository        # Repositorios JPA
├── response          # Mensajes y respuestas personalizadas
├── service           # Lógica de negocio
├── configuration     # Swagger config
└── RentApiApplication.java
```

---

## 🧩 Principales Funcionalidades

### 📌 Habitaciones
- CRUD completo (crear, listar, modificar, eliminar)
- Filtro de habitaciones por estado (disponible, reservada, mantenimiento)
- Paginación de resultados

### 📌 Reservas
- Crear reserva (verifica si habitación está disponible)
- Marcar reserva como culminada (y liberar habitación)
- Buscar reserva por ID
- Filtro de reservas por fecha de creación
- Resultados en formato plano (`PlanoReservaResponseDTO`)

---

## ✅ Validaciones Incluidas

- Validación de campos con anotaciones `@NotNull`, `@Min`, `@Size`, `@Pattern`
- Validación de fechas futuras
- Validación de enums como `EstadoReserva`, `EstadoHabitacion`
- Manejadores globales de errores HTTP y validación (`GlobalExceptionHandler`)

---

## 🧪 Pruebas Unitarias

El proyecto incluye pruebas unitarias con **Mockito** para:
- `HabitacionServiceImpl`
- `ReservaServiceImpl`

Cobertura de:
- Listado paginado
- Excepciones por entidad no encontrada
- Creación de habitaciones/reservas
- Actualizaciones y eliminaciones

---

## 📌 Beneficios del Proyecto

- 🔒 Robustez en validaciones y manejo de errores
- ⚡ Alta cohesión y bajo acoplamiento gracias al uso de interfaces, DTOs y mapeadores
- 📄 Documentación automática con Swagger
- 🔄 Arquitectura limpia en capas
- 🧪 Código testeable y mantenible con pruebas unitarias

---

## 📚 Documentación Swagger

Accede a la documentación de la API:
```
GET http://localhost:8080/swagger-ui/index.html
```

---

## ▶️ Ejecución

Asegúrate de tener configurada una base de datos MySQL y actualiza las credenciales en `application.properties`.

```bash
mvn spring-boot:run
```

---

## 🚀 Futuras Mejores

- Implementar autenticación con Spring Security
- Soporte para roles de usuario (admin, cliente)
- Persistencia de usuarios/clientes

---

## 🧑‍💻 Autor

Proyecto realizado con fines educativos.
