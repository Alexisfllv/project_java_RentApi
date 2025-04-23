# 🚗 RentApi - Backend Java con Spring Boot

![Java](https://img.shields.io/badge/Java-21-orange?logo=java&logoWidth=14)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen?logo=spring&logoWidth=14)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue?logo=mysql&logoWidth=14)
![MapStruct](https://img.shields.io/badge/MapStruct-1.6.3-yellow?logo=mapstruct&logoWidth=14)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red?logo=lombok&logoWidth=14)
![Swagger](https://img.shields.io/badge/API%20Docs-Swagger-informational?logo=swagger&logoWidth=14)

## 📚 Descripción

Este es un proyecto backend desarrollado con **Spring Boot 3.4.4** para gestionar el sistema de alquiler de habitaciones. Usa una arquitectura sencilla y moderna, con integración de base de datos MySQL, documentación de API con Swagger y mapeo de DTOs usando MapStruct.

---
## 🚀 Tecnologías utilizadas

- ☕ **Java 21**
- 🌱 **Spring Boot Web & Data JPA**
- 🛢️ **MySQL**
- 🔁 **MapStruct** – para mapeo entre entidades y DTOs
- 🔒 **Validaciones con Jakarta Validation**
- 🧪 **Spring Boot Test**
- 📚 **Swagger (Springdoc OpenAPI)** – documentación automática
---
# 🧱 Estructura proyecto

``` python
com.ejemplo.rentapi
├── controller
│	├── HabitacionController.java
│   └── ReservaController.java
├── Dto
│   ├── HabitacionRequestDTO.java
│   ├── HabitacionResponseDTO.java
│   ├── HabitacionUpdateDTO.java
│   ├── PlanoReservaResponseDTO.java
│   ├── ReservaRequestDTO.java
│   └── ReservaResponseDTO.java
├── Entity
│   ├── EstadoHabitacion.java
│   ├── EstadoReserva.java
│   ├── Habitacion.java
│   └── Reserva.java
├── Exception
│   ├── ErrorResponse.java
│   ├── ExInvalidDataException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── Mapper
│   ├── HabitacionMapper.java
│   └── ReservaMapper.java
├── Pagination
│   └── PageResponseDTO.java
├── Repository
│   ├── HabitacionRepository.java
│   └── ReservaRepository.java
├── Response
│   ├── ResponseDTO.java
│   └── ResponseMessage.java
├── Service
│   ├── HabitacionService.java
│   ├── ReservaService.java
│   └── Service/Impl
│		├── HabitacionServiceImpl.java
│		└── ReservaServiceimpl.java
└── RentApiApplication.java
```

```python
test/com.ejemplo.rentapi
├── Service
│	├── HabitacionServiceImpl.java
│   └── ReservaServiceImpl.java
```
---

# Entity
### Habitacion.java

``` java
@Data  
@Entity  
@NoArgsConstructor  
@AllArgsConstructor  
@Table(name = "habitaciones")  
public class Habitacion {  
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  
  
    @Column(nullable = false)  
    private Integer numero;  
  
    @Column(nullable = false)  
    private Integer piso;  
  
    @Column(nullable = false)  
    private String tipo; // Ej: "Suite", "Simple", "Medium"  
  
    @Enumerated(EnumType.STRING)  
    @Column(nullable = false)  
    private EstadoHabitacion estado = EstadoHabitacion.DISPONIBLE; // Ej: DISPONIBLE, RESERVADA, MANTENIMIENTO  
}
```

``` java
public enum EstadoHabitacion {  
    DISPONIBLE, RESERVADA, MANTENIMIENTO  
}
```

### Tabla: habitaciones

| Columna | Tipo de Dato | Clave | Nulo | Descripción                                   |
| ------- | ------------ | ----- | ---- | --------------------------------------------- |
| id      | BIGINT       | PK    | NO   | Identificador único de la habitación          |
| numero  | INT          |       | NO   | Número de la habitación                       |
| piso    | INT          |       | NO   | Piso en el que se encuentra                   |
| tipo    | VARCHAR      |       | NO   | Tipo: "Suite", "Simple", "Medium"             |
| estado  | ENUM(STRING) |       | NO   | Estado: DISPONIBLE, RESERVADA, MANTENIMIENTO. |

---


### Reserva.java
```java
@Data  
@NoArgsConstructor  
@AllArgsConstructor  
@Entity  
@Table(name = "reservas")  
public class Reserva {  
  
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  
  
    // Relación con la habitación (muchas reservas pueden ser de una habitación)  
    @ManyToOne  
    @JoinColumn(name = "habitacion_id", nullable = false)  
    private Habitacion habitacion;  
  
    // Datos del cliente como texto (no entidad, porque no es socio/usuario registrado)  
    @Column(name = "cliente_nombre", nullable = false)  
    private String clienteNombre;  
  
    @Column(name = "cliente_dni", nullable = false)  
    private String clienteDni;  
  
    @Column(name = "fecha_inicio", nullable = false)  
    private LocalDate fechaInicio;  
  
    @Column(name = "fecha_fin", nullable = false)  
    private LocalDate fechaFin;  
  
    @Column(name = "comentarios")  
    private String comentarios;  
  
    @Column(name = "fecha_creacion")  
    private LocalDateTime fechaCreacion;  
  
    @Column(name = "fecha_culminada")  
    private LocalDateTime fechaCulminada;  
  
    @Enumerated(EnumType.STRING)  
    @Column(nullable = true)  
    private EstadoReserva estado = EstadoReserva.REALIZADA;   
}
```

``` java
public enum EstadoReserva {  
    REALIZADA,CULMINADA  
}
```

### Tabla: reservas

| Columna         | Tipo de Dato    | Clave | Nulo | Descripción                                    |
| --------------- | --------------- | ----- | ---- | ---------------------------------------------- |
| id              | BIGINT          | PK    | NO   | Identificador de la reserva                    |
| habitacion_id   | BIGINT (FK)     | FK    | NO   | Relación con `habitaciones.id`                 |
| cliente_nombre  | VARCHAR         |       | NO   | Nombre del cliente                             |
| cliente_dni     | VARCHAR         |       | NO   | DNI del cliente                                |
| fecha_inicio    | DATE            |       | NO   | Fecha de inicio de la reserva                  |
| fecha_fin       | DATE            |       | NO   | Fecha de fin de la reserva                     |
| comentarios     | TEXT (opcional) |       | SÍ   | Comentarios adicionales                        |
| fecha_creacion  | DATETIME        |       | SÍ   | Fecha en que se creó la reserva                |
| fecha_culminada | DATETIME        |       | SÍ   | Fecha en que terminó (check-out)               |
| estado          | ENUM(STRING)    |       | SÍ   | Estado: REALIZADA, CANCELADA, FINALIZADA, etc. |

---
# DTO (record)
### HabitacionRequestDTO.java
```java
public record HabitacionRequestDTO(  
  
        @NotNull(message = "El piso es obligatorio")  
        @Min(value = 1, message = "El piso debe ser mayor o igual a 1")  
        @Schema(description = "Número de piso", example = "2", minimum = "1", required = true)  
        Integer piso,  
  
        @NotNull(message = "El número de habitación es obligatorio")  
        @Min(value = 1, message = "El número de habitación debe ser mayor o igual a 1")  
        @Schema(description = "Número de la habitación", example = "205", minimum = "1", required = true)  
        Integer numero,  
  
        @NotBlank(message = "El tipo de habitación es obligatorio")  
        @Size(max = 50, message = "El tipo de habitación no debe exceder los 50 caracteres")  
        @Schema(description = "Tipo de habitación (ej: simple, doble, suite)", example = "doble", maxLength = 50, required = true)  
        String tipo  
) {}
```

```json
{
  "piso": 2,
  "numero": 105,
  "tipo": "Matrimonial" 
  // DISPONIBLE
}
```

---
### HabitacionResponseDTO.java

```java
public record HabitacionResponseDTO(  
  
        @Schema(description = "ID único de la habitación AutoIncrement")  
        Long id,  
  
        @Schema(description = "Número de piso", example = "2")  
        Integer piso,  
  
        @Schema(description = "Número de habitación", example = "205")  
        Integer numero,  
  
        @Schema(description = "Tipo de habitación", example = "doble")  
        String tipo,  
  
        @Schema(description = "Estado actual de la habitación", example = "DISPONIBLE/RESERVADA/MANTENIMIENTO")  
        EstadoHabitacion estado  
) {}
```

```json
{
  "piso": 3,
  "numero": 210,
  "tipo": "Doble",
  "estado": "DISPONIBLE" // DISPONIBLE, RESERVADA, MANTENIMIENTO
}

```

---
### HabitacionUpdateDTO.java

```java
public record HabitacionUpdateDTO(  
        @NotNull(message = "El piso es obligatorio")  
        @Min(value = 1, message = "El piso debe ser mayor o igual a 1")  
        @Schema(description = "Número de piso", example = "2", minimum = "1", required = true)  
        Integer piso,  
  
        @NotNull(message = "El número de habitación es obligatorio")  
        @Min(value = 1, message = "El número de habitación debe ser mayor o igual a 1")  
        @Schema(description = "Número de la habitación", example = "205", minimum = "1", required = true)  
        Integer numero,  
  
        @NotBlank(message = "El tipo de habitación es obligatorio")  
        @Size(max = 50, message = "El tipo de habitación no debe exceder los 50 caracteres")  
        @Schema(description = "Tipo de habitación (ej: simple, doble, suite)", example = "doble", maxLength = 50, required = true)  
        String tipo,  
  
        @NotBlank(message = "El estado habitacion es obligatorio")  
        @Enumerated(EnumType.STRING)  
        @Schema(description = "Estados comunes" , example = "DISPONIBLE,RESERVADA,MANTENIMIENTO")  
        EstadoHabitacion estado  
) {}
```

```json
{
  "piso": 2,
  "numero": 203,
  "tipo": "Matrimonial",
  "estado": "MANTENIMIENTO" // DISPONIBLE, RESERVADA,   MANTENIMIENTO
}
```
---
### ReservaRequestDTO.java

``` java
public record ReservaRequestDTO(  
  
        @NotNull(message = "El ID de la habitación es obligatorio")  
        @Schema(description = "Campo id habitacion autoincrementado")  
        Long habitacionId,  
  
        @NotBlank(message = "El nombre del cliente es obligatorio")  
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")  
        @Schema(description = "Nombre de cliente",example = "Juanito")  
        String clienteNombre,  
  
        @NotBlank(message = "El DNI del cliente es obligatorio")  
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")  
        @Schema(description = "numero de dni",example = "12345678",minimum = "8",maxLength = 8,required = true)  
        String clienteDni,  
  
        @NotNull(message = "La fecha de inicio es obligatoria")  
        @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")  
        @Schema(description = "Fecha de Inicio reservada",example = "2025-12-31")  
        LocalDate fechaInicio,  
  
        @NotNull(message = "La fecha de fin es obligatoria")  
        @Future(message = "La fecha de fin debe ser en el futuro")  
        @Schema(description = "Fecha de Fin reservada mayor a fecha inicio reserva",example = "2025-12-31")  
        LocalDate fechaFin,  
  
        @Size(max = 255, message = "Los comentarios no deben superar los 255 caracteres")  
        @Schema(description = "Comentarios sobre la reserva adicionales")  
        String comentarios  
) {}
```

``` json
{
  "clienteNombre": "Juan Pérez",
  "clienteDni": "87654321",
  "fechaInicio": "2025-04-14",
  "fechaFin": "2025-04-16",
  "comentarios": "Solicita habitación con vista al mar"
}
```
---
### PlanoReservaResponseDTO.java

``` java
public record PlanoReservaResponseDTO(  
        @Schema(description = "La reserva ID autogenerada")  
        Long reservaId,  
        @Schema(description = "El campo de habitacion de numero",example = "101")  
        Integer habitacionNumero,  
        @Schema(description = "El campo muestra el tipo de habitacion")  
        String habitacionTipo,  
        @Schema(description = "El campo muestra el nombre del cliente registrado")  
        String clienteNombre,  
        @Schema(description = "Fecha de inicio para la reserva")  
        LocalDate fechaInicio,  
        @Schema(description = "Fecha de fin para la reserva")  
        LocalDate fechaFin,  
        @Schema(description = "Campo para datos adicionales de la reserva")  
        String comentarios,  
        @Schema(description = "El estado del la reserva",example = "REALIZADA,CULMINADA")  
        EstadoReserva estadoReserva,  
        @Schema(description = "fecha creada como actual desde que se realizo")  
        LocalDateTime fechaCreacion,  
        @Schema(description = "fecha que se modifica para cuando termine la reserva",example = "CULMINADA -> REALIZADA")  
        LocalDateTime fechaCulminada  
) { }
```

``` json
{
  "reservaId": 12345,
  "habitacionNumero": 101,
  "habitacionTipo": "Matrimonial",
  "clienteNombre": "Ana García",
  "fechaInicio": "2025-04-15",
  "fechaFin": "2025-04-20",
  "comentarios": "Reserva con preferencia por cama adicional",
  "estadoReserva": "REALIZADA", //REALIZADA,CULMINADA
  "fechaCreacion": "2025-04-01T10:30:00",
  "fechaCulminada": "2025-04-20T12:00:00"
}
```
---
# Mapper
###  HabitacionMapper.java

``` java
@Mapper(componentModel = "spring")  
public interface HabitacionMapper {  
  
    // response -> modelo  
    HabitacionResponseDTO toHabitacionResponseDTO(Habitacion habitacion);  

    // entity -> request  
    Habitacion toHabitacion(HabitacionRequestDTO habitacionRequestDTO);  
}
```
***
### ReservaMapper.java
```java
@Mapper(componentModel = "spring")  
public interface ReservaMapper {  
    @Mapping(target = "habitacion.id", source = "habitacionId")  
    Reserva toReserva(ReservaRequestDTO reservaRequestDTO);  
    // plano  
    @Mapping(target = "habitacionNumero", source = "habitacion.numero")  
    @Mapping(target = "habitacionTipo", source = "habitacion.tipo")  
    @Mapping(target = "estadoReserva", source = "estado")  
    @Mapping(target = "reservaId", source = "id")  
    @Mapping(target = "fechaCreacion", source = "fechaCreacion")  
    @Mapping(target = "fechaCulminada", source = "fechaCulminada")  
    PlanoReservaResponseDTO toPlanoReservaResponseDto(Reserva reserva);  
}
```

---
# Exception
### ErrorResponse.java

```java
public record ErrorResponse(  
    int status,  
    String message,  
    LocalDateTime timestamp,  
    Map<String, String> errors  
) {}
```

### ExInvalidDataException.java

```java
public class ExInvalidDataException extends RuntimeException { 
    public ExInvalidDataException(String message) {  
        super(message);  
    }  
}
```

### ResourceNotFoundException.java

```java
public class ResourceNotFoundException extends RuntimeException {  
    public ResourceNotFoundException(String message) {  
        super(message);  
    }  
}
```

### GlobalExceptionHandler.java

``` java
@RestControllerAdvice  
public class GlobalExceptionHandler {  
  
    /**  
     * Errores de validación de campos en el cuerpo JSON (RequestBody).     * Ejemplo: campo faltante o inválido con @Valid.     */    @ExceptionHandler(MethodArgumentNotValidException.class)  
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {  
        Map<String, String> errors = new HashMap<>();  
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));  
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed (GlobalExceptionHandler)", errors);  
    }  
  
    /**  
     * Errores de validación de parámetros individuales (@RequestParam, @PathVariable, etc.).     * Requiere @Validated en el controlador.     */    @ExceptionHandler(ConstraintViolationException.class)  
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {  
        Map<String, String> errors = new HashMap<>();  
        ex.getConstraintViolations().forEach(v -> {  
            String field = v.getPropertyPath().toString();  
            errors.put(field, v.getMessage());  
        });  
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request parameters (GlobalExceptionHandler)", errors);  
    }  
  
    /**  
     * JSON mal formado o tipo de dato inválido (por ejemplo, fecha incorrecta).     */    @ExceptionHandler(HttpMessageNotReadableException.class)  
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {  
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request (GlobalExceptionHandler)", null);  
    }  
  
    /**  
     * Parámetro obligatorio ausente en la URL (ej. @RequestParam faltante).     */    @ExceptionHandler(MissingServletRequestParameterException.class)  
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {  
        Map<String, String> errors = Map.of(ex.getParameterName(), "Parameter is missing");  
        return buildResponse(HttpStatus.BAD_REQUEST, "Missing required parameter (GlobalExceptionHandler)", errors);  
    }  
  
    /**  
     * Error de tipo en parámetros (ej. id esperado como Long y se envía un String).     */    @ExceptionHandler(MethodArgumentTypeMismatchException.class)  
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {  
        Map<String, String> errors = Map.of(ex.getName(), "Invalid type: expected " + ex.getRequiredType());  
        return buildResponse(HttpStatus.BAD_REQUEST, "Type mismatch (GlobalExceptionHandler)", errors);  
    }  
  
    /**  
     * Metodo HTTP no permitido (ej. POST cuando solo se permite GET).     */    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)  
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {  
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not supported (GlobalExceptionHandler)", null);  
    }  
  
    /**  
     * Excepción personalizada para datos inválidos (reglas de negocio).     */    @ExceptionHandler(ExInvalidDataException.class)  
    public ResponseEntity<ErrorResponse> handleCustom(ExInvalidDataException ex) {  
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage() + " (GlobalExceptionHandler)", null);  
    }  
  
    /**  
     * Recurso no encontrado (ej. entidad por ID no existe).     */    @ExceptionHandler(ResourceNotFoundException.class)  
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {  
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage() + " (GlobalExceptionHandler)", null);  
    }  
  
    /**  
     * Cualquier otra excepción no controlada.     */    @ExceptionHandler(Exception.class)  
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {  
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred (GlobalExceptionHandler)", null);  
    }  
  
    // Crea el objeto de respuesta de error estandarizado  
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, Map<String, String> errors) {  
        ErrorResponse response = new ErrorResponse(  
                status.value(),  
                message,  
                LocalDateTime.now(),  
                errors  
        );  
        return new ResponseEntity<>(response, status);  
    }  
}
```

---
# Pagination
### PageResponseDTO.java

```java
public record PageResponseDTO<T>(  
        List<T> content,  
        int page,  
        int size,  
        long totalElements,  
        int totalPages,  
        boolean isLast,  
        boolean isFirst,  
        int numberOfElements  
) {public PageResponseDTO(Page<T> page) {  
    this(page.getContent(),  
            page.getNumber(),  
            page.getSize(),  
            page.getTotalElements(),  
            page.getTotalPages(),  
            page.isLast(),  
            page.isFirst(),  
            page.getNumberOfElements());  
}  
}
```
---
# Response

### ResponseDTO.java

```java
public record ResponseDTO(  
        String mensaje,  
        Object data) {  
}
```

###  ResponseMessage.java

```java
@Getter  
public enum ResponseMessage {  
    SUCCESSFUL_ADDITION("Added successfully"),  
    SUCCESSFUL_MODIFICATION("Modification completed successfully"),  
    SUCCESSFUL_DELETION("Deletion completed successfully");  
  
    private final String message;  
    // Private constructor  
    ResponseMessage(String message) {  
        this.message = message;  
    }  
}
```

---
# Service
### HabitacionService.java

```java
public interface HabitacionService {  
    // listado de habitaciones disponibles  
    List<HabitacionResponseDTO> listarHabitacionesConEstadoDisponible();  
  
    // listar todas las habitaciones  
    PageResponseDTO<HabitacionResponseDTO> listarHabitaciones(Pageable pageable);  
  
    // busqueda  
    HabitacionResponseDTO buscarHabitacionPorId(Long id);  
  
    // insercion  
    ResponseDTO registrarHabitacion(HabitacionRequestDTO habitacionRequestDTO);  
  
    // update  
    ResponseDTO updateHabitacion(Long id, HabitacionUpdateDTO habitacionUpdateDTO);  
  
    // delete  
    ResponseDTO eliminarHabitacion(Long id);  
}
```

### ReservaService.java

```java
public interface ReservaService {  
    // datos en formato plano  
    ResponseDTO crearReservaPlana(ReservaRequestDTO reservaRequestDTO);  
  
    // listado de reservas  
    PageResponseDTO<PlanoReservaResponseDTO> listadoReservas(Pageable pageable);  
  
    // busqueda de reservas  
    PlanoReservaResponseDTO buscarReserva(Long id);  
  
    // culminar reserva  
    ResponseDTO culminarReserva(Long id);  
  
    // filtrado de fechas  
    PageResponseDTO<PlanoReservaResponseDTO> filtradoFechaInicio(Pageable pageable, LocalDateTime desde, LocalDateTime hasta);  
}
```

---
# Service/Impl

### HabitacionServiceImpl.java

``` java
@Service  
@RequiredArgsConstructor  
public class HabitacionServiceImpl implements HabitacionService {  
  
    // ioc  
    private final HabitacionRepository habitacionRepository;  
    private final HabitacionMapper habitacionMapper;  
  
  
    @Override  
    public List<HabitacionResponseDTO> listarHabitacionesConEstadoDisponible() {  
        List<Habitacion> habitaciones = habitacionRepository.findByEstado(EstadoHabitacion.DISPONIBLE);  
  
        return habitaciones  
                .stream()  
                .map(habitacion -> habitacionMapper.toHabitacionResponseDTO(habitacion))  
                .toList();  
  
    }  
  
    @Override  
    public PageResponseDTO<HabitacionResponseDTO> listarHabitaciones(Pageable pageable) {  
  
        Page<HabitacionResponseDTO> paged =  habitacionRepository.findAll(pageable)  
                .map(habitacion -> habitacionMapper.toHabitacionResponseDTO(habitacion));  
  
        return new PageResponseDTO<>(paged);  
    }  
  
    @Override  
    public HabitacionResponseDTO buscarHabitacionPorId(Long id) {  
        Habitacion habitacion = habitacionRepository.findById(id)  
                .orElseThrow(()-> new ResourceNotFoundException("Habitacion en id " + id + " no encontrado"));  
        return habitacionMapper.toHabitacionResponseDTO(habitacion);  
    }  
  
    @Override  
    public ResponseDTO registrarHabitacion(HabitacionRequestDTO habitacionRequestDTO) {  
        Habitacion habitacion = habitacionMapper.toHabitacion(habitacionRequestDTO);  
  
        habitacion.setPiso(habitacionRequestDTO.piso());  
        habitacion.setNumero(habitacionRequestDTO.numero());  
        habitacion.setTipo(habitacionRequestDTO.tipo());  
  
        habitacionRepository.save(habitacion);  
        HabitacionResponseDTO dto = habitacionMapper.toHabitacionResponseDTO(habitacion);  
  
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_ADDITION.getMessage(), dto);  
    }  
  
    @Override  
    public ResponseDTO updateHabitacion(Long id, HabitacionUpdateDTO habitacionUpdateDTO) {  
        Habitacion habitacion = habitacionRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion en id " + id + " no encontrado"));  
  
        habitacion.setPiso(habitacionUpdateDTO.piso());  
        habitacion.setNumero(habitacionUpdateDTO.numero());  
        habitacion.setTipo(habitacionUpdateDTO.tipo());  
        habitacion.setEstado(habitacionUpdateDTO.estado());  
        habitacionRepository.save(habitacion);  
  
        HabitacionResponseDTO dto = habitacionMapper.toHabitacionResponseDTO(habitacion);  
  
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_MODIFICATION.getMessage(), dto);  
    }  
  
    @Override  
    public ResponseDTO eliminarHabitacion(Long id) {  
        habitacionRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion en id " + id + " no encontrado"));  
        habitacionRepository.deleteById(id);  
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_DELETION.getMessage(), "Id eliminado : "+ id);  
    }  
}
```

### ReservaServiceImpl.java

```java
@Slf4j  
@Service  
@RequiredArgsConstructor  
public class ReservaServiceImpl implements ReservaService {  
  
  
    private final ReservaRepository reservaRepository;  
    private final ReservaMapper reservaMapper;  
  
    private final HabitacionRepository habitacionRepository;  
  
  
    @Override  
    @Transactional    public ResponseDTO crearReservaPlana(ReservaRequestDTO reservaRequestDTO) {  
        Reserva reserva =  reservaMapper.toReserva(reservaRequestDTO);  
  
        reserva.setClienteNombre(reservaRequestDTO.clienteNombre());  
        reserva.setClienteDni(reservaRequestDTO.clienteDni());  
        reserva.setFechaInicio(reservaRequestDTO.fechaInicio());  
        reserva.setFechaFin(reservaRequestDTO.fechaFin());  
        reserva.setComentarios(reservaRequestDTO.comentarios());  
  
        reserva.setFechaCreacion(LocalDateTime.now());  
  
        // habitacion  
        Habitacion habitacion = habitacionRepository.findById(reservaRequestDTO.habitacionId())  
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada :"+reservaRequestDTO.habitacionId()));  
  
        if (!habitacion.getEstado().equals(EstadoHabitacion.DISPONIBLE)) {  
            log.warn("Habitacion estado :"+habitacion.getEstado());  
            throw new ResourceNotFoundException(" la habitacion no esta disponible para reservar :"+ habitacion.getEstado());  
  
        }  
  
        log.info("estado :"+ reserva.getEstado());  
        habitacion.setEstado(EstadoHabitacion.RESERVADA);  
        habitacionRepository.save(habitacion);  
  
        reserva.setHabitacion(habitacion);  
  
        reservaRepository.save(reserva);  
  
        PlanoReservaResponseDTO dto = reservaMapper.toPlanoReservaResponseDto(reserva);  
  
        // response  
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_ADDITION.getMessage(), dto);  
  
    }  
  
    @Override  
    public PageResponseDTO<PlanoReservaResponseDTO> listadoReservas(Pageable pageable) {  
  
        Page<PlanoReservaResponseDTO> paged = reservaRepository.findAll(pageable)  
                .map(reserva -> reservaMapper.toPlanoReservaResponseDto(reserva));  
  
        return new PageResponseDTO<>(paged);  
    }  
  
    @Override  
    public PlanoReservaResponseDTO buscarReserva(Long id) {  
        Reserva reserva  =reservaRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada :"+id));  
        return reservaMapper.toPlanoReservaResponseDto(reserva);  
    }  
  
    @Override  
    @Transactional    public ResponseDTO culminarReserva(Long id) {  
        Reserva reservaExistente = reservaRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada :"+id));  
  
        reservaExistente.setFechaCulminada(LocalDateTime.now());  
        reservaExistente.setEstado(EstadoReserva.CULMINADA);  
  
        // habitacion  
        reservaExistente.getHabitacion().setEstado(EstadoHabitacion.DISPONIBLE);  
  
        reservaRepository.save(reservaExistente);  
        PlanoReservaResponseDTO dto = reservaMapper.toPlanoReservaResponseDto(reservaExistente);  
  
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_MODIFICATION.getMessage(), dto);  
    }  
  
    @Override  
    public PageResponseDTO<PlanoReservaResponseDTO> filtradoFechaInicio(Pageable pageable, LocalDateTime desde, LocalDateTime hasta) {  
  
        Page<Reserva> reservaFiltro = reservaRepository.findByFechaCreacionBetween(pageable,desde,hasta);  
  
        Page<PlanoReservaResponseDTO> paged = reservaFiltro  
                .map(reserva -> reservaMapper.toPlanoReservaResponseDto(reserva));  
  
        return new PageResponseDTO<>(paged);  
    }  
}
```

---
# Configuration
### OpenAPIConfig.java

```java
@Configuration  
public class OpenAPIConfig {  
    @Bean  
    public OpenAPI customOpenAPI() {  
        return new OpenAPI()  
                .info(new Info()  
                        .title("API de Renta Habitacion")  
                        .version("1.0")  
                        .description("Documentacion Rent-Api"));  
    }  
}
```
---
# Controller
### HabitacionController

```java
@Tag(name = "Habitaciones", description = "API para gestion de Habitaciones")  
@RestController  
@RequestMapping("/api/habitaciones")  
@RequiredArgsConstructor  
@CrossOrigin(origins = "*")  
public class HabitacionController {  
  
    private final HabitacionService habitacionService;  
  
    //  
    @Operation(  
            summary = "Creacion de habitacion",  
            description = "Retorna todas las habitaciones disponibles con la condicion de *DISPONIBLES* de habitacion",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "listado HabitacionResponseDTO disponibles"),  
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")  
            }    )  
    @GetMapping("/disponibles")  
    public ResponseEntity<List<HabitacionResponseDTO>> disponibles() {  
        return ResponseEntity.status(200).body(habitacionService.listarHabitacionesConEstadoDisponible());  
    }  
  
    // listar  
  
    @Operation(  
            summary = "Listado de habitaciones paginadas",  
            description = "Retorna todas las habitaciones disponibles paginadas ",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "listado HabitacionResponseDTO paginadas"),  
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")  
            }    )  
    @GetMapping("/listar")  
    public ResponseEntity<PageResponseDTO<HabitacionResponseDTO>> paginadoListadoHabitacion(  
            @RequestParam(defaultValue = "0") Integer page,  
            @RequestParam(defaultValue = "3") Integer size  
    ){  
        Pageable pageable = PageRequest.of(page, size);  
        return ResponseEntity.status(200).body(habitacionService.listarHabitaciones(pageable));  
    }  
  
    @Operation(  
            summary = "Busqueda de Habitaciones por id",  
            description = "Retorna habitacion buscada por el ID ",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "HabitacionResponseDTO"),  
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")  
            }    )  
    @GetMapping("/buscar/{id}")  
    public ResponseEntity<HabitacionResponseDTO> buscarHabitacion(@PathVariable Long id) {  
        return ResponseEntity.ok(habitacionService.buscarHabitacionPorId(id));  
    }  
  
    @Operation(  
            summary = "Creacion de Habitacion",  
            description = "Creacion de habitacion con modelo de HabitacionRequestDTO y retonar un HabitacionResponseDTO",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "ResponseDTO con mensaje exitoso de creacion y HabitacionResponseDTO"),  
                    @ApiResponse(responseCode = "400", description = "ResponseDTO con mensaje fallido por error de Validacion"),  
                    @ApiResponse(responseCode = "500", description = "ResponsedTO con mensaje fallido de URL"),  
                    @ApiResponse(responseCode = "400", description = "ResponsedTO con mensaje fallido de Mal formato de JSON")  
            }    )  
    // registrar  
    @PostMapping("/crear")  
    public ResponseEntity<ResponseDTO> crearHabitacion(@Valid @RequestBody HabitacionRequestDTO habitacionRequestDTO) {  
        return ResponseEntity.status(201).body(habitacionService.registrarHabitacion(habitacionRequestDTO));  
    }  
  
    @Operation(  
            summary = "Modificar habitacion por Id",  
            description = "Modifica la habitacion con HabitacionRequestDTO por id y retorna HabitacionResponseDTO ",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "ResponseDTO con mensaje exitoso de modificacion y HabitacionResponseDTO"),  
                    @ApiResponse(responseCode = "400", description = "ResponseDTO con mensaje fallido por error de Validacion"),  
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL"),  
                    @ApiResponse(responseCode = "400", description = "ResponseDTO con mensaje fallido de Mal formato de JSON")  
            }    )  
    @PutMapping("/modificar/{id}")  
    public ResponseEntity<ResponseDTO> modificarHabitacion(@PathVariable Long id, @RequestBody HabitacionUpdateDTO habitacionUpdateDTO) {  
        return ResponseEntity.ok(habitacionService.updateHabitacion(id, habitacionUpdateDTO));  
    }  
  
    @Operation(  
            summary = "Eliminar habitacion por Id",  
            description = "Elimina la habitacion por el id de @PathVariable Ingresado",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "ResponseDTO con mensaje exitoso de eliminacion"),  
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")  
            }    )  
    @DeleteMapping("/eliminar/{id}")  
    public ResponseEntity<ResponseDTO> eliminarHabitacion(@PathVariable Long id) {  
        return ResponseEntity.ok(habitacionService.eliminarHabitacion(id));  
    }  
}
```

### ReservaController.java

```java
@Tag(name = "Habitaciones", description = "API para gestion de Reservas")  
@RestController  
@RequestMapping("/api/reservas")  
@RequiredArgsConstructor  
public class ReservaController {  
    private final ReservaService reservaService;  
      
    @Operation(  
            summary = "Creacion de reserva",  
            description = "Retorna un PlanoReservaResponseDTO despues de la creacion de ReservaRequestDTO",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "ResponseDTO con mensaje exitoso de creacion y PlanoReservaResponseDTO"),  
                    @ApiResponse(responseCode = "400", description = "ResponseDTO con mensaje fallido por error de Validacion"),  
                    @ApiResponse(responseCode = "500", description = "ResponsedTO con mensaje fallido de URL"),  
                    @ApiResponse(responseCode = "400", description = "ResponsedTO con mensaje fallido de Mal formato de JSON")  
            }    )  
    @PostMapping("/crear/plano")  
    public ResponseEntity<ResponseDTO> crearReservaPlano(@Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {  
        return ResponseEntity.ok(reservaService.crearReservaPlana(reservaRequestDTO));  
    }  
  
    // listado  
    @Operation(  
            summary = "Listado de reservas paginadas",  
            description = "Retorna todas las PlanoReservaResponseDTO  paginadas ",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "listado PlanoReservaResponseDTO paginadas"),  
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")  
            }    )  
    @GetMapping("/listado")  
    public ResponseEntity<PageResponseDTO<PlanoReservaResponseDTO>> listarReservas(  
  
            @RequestParam(defaultValue = "0") int page,  
            @RequestParam(defaultValue = "3") int size  
    ) {  
        Pageable pageable = PageRequest.of(page, size);  
        return ResponseEntity.status(200).body(reservaService.listadoReservas(pageable));  
    }  
  
  
    // busqueda  
    @Operation(  
            summary = "Busqueda de Reservas",  
            description = "Retorna la PlanoReservaResponseDTO buscada por id",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = " PlanoReservaResponseDTO "),  
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")  
            }    )  
    @GetMapping("buscar/{id}")  
    public ResponseEntity<PlanoReservaResponseDTO> buscarReserva(@PathVariable Long id) {  
        return ResponseEntity.ok(reservaService.buscarReserva(id));  
    }  
  
    @Operation(  
            summary = "Culminar el estado de la reserva",  
            description = "Enviar el id para culminar esa reserva cambia el estado del campo estadoReserva a CULMINADA",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = " PlanoReservaResponseDTO "),  
                    @ApiResponse(responseCode = "404", description = "ResponseDTO con mensaje fallido de id no encontrado o no existe"),  
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")  
            }    )  
    @PostMapping("culminar/{id}")  
    public ResponseEntity<ResponseDTO> culminarReserva(@PathVariable Long id) {  
        return ResponseEntity.ok(reservaService.culminarReserva(id));  
    }  
  
    @Operation(  
            summary = "Filtrar por fechas paginadas",  
            description = "Busqueda entre fachas de creacion las reservas y paginadas",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Listado PlanoReservaResponseDTO "),  
                    @ApiResponse(responseCode = "400", description = "ResponseDTO con mensaje fallido de error de parametros"),  
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")  
            }    )  
  
    @GetMapping("buscar/fecha")  
    public ResponseEntity<PageResponseDTO<PlanoReservaResponseDTO>> filtrarReservaFechasCreacion  
            (  
                    @RequestParam (defaultValue = "0") Integer page,  
                    @RequestParam (defaultValue = "3") Integer size,  
                    @RequestParam LocalDateTime desde,  
                    @RequestParam LocalDateTime hasta  
            ){  
        Pageable pageable = PageRequest.of(page, size);  
        return ResponseEntity.ok(reservaService.filtradoFechaInicio(pageable,desde,hasta));  
    }  
}
```
---

```python
com.ejemplo.hotel
├── controller
│	├── HabitacionController.java
│   └── ReservaController.java
├── Service
│   ├── HabitacionServiceImplTest.java
│   ├── .java
│   └── Service/Impl
└── RentApiApplicationTests.java
```

# Test
### HabitacionServiceImplTest.java

```java
@ExtendWith(MockitoExtension.class)  
public class HabitacionServiceImplTest {  
  
    // creacion de mocks de instancias a probar  
    @Mock  
    private HabitacionRepository habitacionRepository;  
    @Mock  
    private HabitacionMapper habitacionMapper;  
  
    @InjectMocks // instancia real de mocks injectados  
    private HabitacionServiceImpl habitacionServiceImpl;  
  
  
    // listar disponibles  
    @Test  
    void shouldListAllHabitacionesDisponiblesTest() {  
        // Arrange  
        Habitacion habitacion1 = new Habitacion(1L,1,101,"Simple",EstadoHabitacion.DISPONIBLE);  
        Habitacion habitacion2 = new Habitacion(2L,2,201,"Basica",EstadoHabitacion.DISPONIBLE);  
        Habitacion habitacion3 = new Habitacion(3L,3,301,"Vip",EstadoHabitacion.RESERVADA);  
  
        List<Habitacion> habitaciones = List.of(habitacion1,habitacion2,habitacion3);  
  
        HabitacionResponseDTO dto1 =  new HabitacionResponseDTO(1L,1,101,"Simple",EstadoHabitacion.DISPONIBLE);  
        HabitacionResponseDTO dto2 =  new HabitacionResponseDTO(2L,2,301,"Basica",EstadoHabitacion.DISPONIBLE);  
  
  
        // WHEN  
        when(habitacionRepository.findByEstado(EstadoHabitacion.DISPONIBLE)).thenReturn(List.of(habitacion1,habitacion2));  
        when(habitacionMapper.toHabitacionResponseDTO(habitacion1)).thenReturn(dto1);  
        when(habitacionMapper.toHabitacionResponseDTO(habitacion2)).thenReturn(dto2);  
  
        List<HabitacionResponseDTO> resultado = habitacionServiceImpl.listarHabitacionesConEstadoDisponible();  
  
        // THEN  
  
        // ASSERT        assertThat(resultado).isNotNull();  
        assertThat(resultado).hasSize(2);  
        assertThat(resultado).containsExactly(dto1,dto2);  
  
        // Verify  
        verify(habitacionRepository).findByEstado(EstadoHabitacion.DISPONIBLE);  
        verify(habitacionMapper).toHabitacionResponseDTO(habitacion1);  
        verify(habitacionMapper).toHabitacionResponseDTO(habitacion2);  
  
    }  
  
  
  
    @Test  
    void shouldListAllHabitacionesPaginated() {  
  
        // Arrange  
        Pageable pageable = PageRequest.of(0, 3);  
        Habitacion habitacion1 = new Habitacion(1L,1,101,"Simple",EstadoHabitacion.DISPONIBLE);  
        Habitacion habitacion2 = new Habitacion(2L,2,202,"Simple",EstadoHabitacion.MANTENIMIENTO);  
  
        List<Habitacion> listaHabitaciones = List.of(habitacion1,habitacion2);  
        Page<Habitacion> pageHabitaciones = new PageImpl<>(listaHabitaciones,pageable,listaHabitaciones.size());  
  
        HabitacionResponseDTO dto1 = new HabitacionResponseDTO(1L,1,101,"Simple",EstadoHabitacion.DISPONIBLE);  
        HabitacionResponseDTO dto2 = new HabitacionResponseDTO(2L,2,202,"Simple",EstadoHabitacion.MANTENIMIENTO);  
  
        when(habitacionRepository.findAll(pageable)).thenReturn(pageHabitaciones);  
        when(habitacionMapper.toHabitacionResponseDTO(habitacion1)).thenReturn(dto1);  
        when(habitacionMapper.toHabitacionResponseDTO(habitacion2)).thenReturn(dto2);  
  
        // Act  
  
        PageResponseDTO<HabitacionResponseDTO> resultado = habitacionServiceImpl.listarHabitaciones(pageable);  
  
        // Assert  
        assertThat(resultado).isNotNull();  
        assertThat(resultado.content()).hasSize(2);  
        assertThat(resultado.content()).containsExactly(dto1,dto2);  
        assertThat(resultado.totalElements()).isEqualTo(2);  
        assertThat(resultado.page()).isEqualTo(0);  
        assertThat(resultado.size()).isEqualTo(3);  
  
        // Verificacion  
        verify(habitacionRepository).findAll(pageable);  
        verify(habitacionMapper).toHabitacionResponseDTO(habitacion1);  
        verify(habitacionMapper).toHabitacionResponseDTO(habitacion2);  
  
    }  
  
  
  
  
    @Test  
    void shouldReturnHabitacionWhenIdExists(){  
        // Given  
        Long id = 1L;  
        Habitacion habitacion =  new Habitacion(id,101,1,"Basico", EstadoHabitacion.DISPONIBLE);  
        HabitacionResponseDTO habitacionResponseDTO = new HabitacionResponseDTO(id,1,101,"Basico", EstadoHabitacion.DISPONIBLE);  
  
        when(habitacionRepository.findById(id)).thenReturn(Optional.of(habitacion));  
        when(habitacionMapper.toHabitacionResponseDTO(habitacion)).thenReturn(habitacionResponseDTO);  
  
        // When  
        HabitacionResponseDTO resultado = habitacionServiceImpl.buscarHabitacionPorId(id);  
  
        // Then  
        assertThat(resultado).isNotNull();  
        assertThat(resultado.id()).isEqualTo(id);  
        assertThat(resultado.numero()).isEqualTo(101);  
        assertThat(resultado.tipo()).isEqualTo("Basico");  
        assertThat(resultado.estado()).isEqualTo(EstadoHabitacion.DISPONIBLE);  
        verify(habitacionRepository,times(1)).findById(id);  
    }  
  
    @Test  
    void shouldThrowExceptionWhenHabitacionNotFound(){  
  
        // Given  
        Long id = 999L;  
        when(habitacionRepository.findById(id)).thenReturn(Optional.empty());  
  
        // When /Then  
        assertThatThrownBy(()-> habitacionServiceImpl.buscarHabitacionPorId(id))  
                .isInstanceOf(ResourceNotFoundException.class)  
                .hasMessage("Habitacion en id " + id + " no encontrado");  
  
        verify(habitacionRepository,times(1)).findById(id);  
    }  
  
    // metodo de registrar habitacion  
  
    @Test  
    void shouldRegisterHabitacionSuccessfully() {  
        // Given (preparacion)  
        HabitacionRequestDTO habitacionRequestDTO = new HabitacionRequestDTO(1,101,"Basico");  
        Habitacion habitacion = new Habitacion(1L,101,1,"Basico", EstadoHabitacion.DISPONIBLE);  
        HabitacionResponseDTO habitacionResponseDTO = new HabitacionResponseDTO(1L,1,101,"Basico", EstadoHabitacion.DISPONIBLE);  
  
        // Mocks de flujo interno  
        when(habitacionMapper.toHabitacion(habitacionRequestDTO)).thenReturn(habitacion);  
        when(habitacionRepository.save(habitacion)).thenReturn(habitacion);  
        when(habitacionMapper.toHabitacionResponseDTO(habitacion)).thenReturn(habitacionResponseDTO);  
  
  
        // When (ejecutar)  
        ResponseDTO response =  habitacionServiceImpl.registrarHabitacion(habitacionRequestDTO);  
  
        // Then (verificacion)  
        assertThat(response.mensaje()).isEqualTo("Added successfully");  
        assertThat(response.data()).isInstanceOf(HabitacionResponseDTO.class);  
        assertThat(((HabitacionResponseDTO) response.data()).id()).isEqualTo(1L);  
  
  
        // verificar que se haya llamado lo esperado  
        verify(habitacionRepository,times(1)).save(habitacion);  
        verify(habitacionMapper).toHabitacionResponseDTO(habitacion);  
        verify(habitacionMapper).toHabitacion(habitacionRequestDTO);  
  
    }  
  
  
    @Test  
    void shouldUpdateHabitacionSuccessfully(){  
  
        // Given  
        Long id = 1L;  
        Habitacion habitacionExistente =  new Habitacion(id,101,1,"Basico", EstadoHabitacion.DISPONIBLE);  
        HabitacionUpdateDTO habitacionUpdateDTO =  new HabitacionUpdateDTO(2,202,"Doble", EstadoHabitacion.MANTENIMIENTO);  
        Habitacion HabitacionActualizada =  new Habitacion(id,202,2,"Doble", EstadoHabitacion.MANTENIMIENTO);  
        HabitacionResponseDTO habitacionResponseDTO =  new HabitacionResponseDTO(id,2,202,"Doble", EstadoHabitacion.MANTENIMIENTO);  
  
        when(habitacionRepository.findById(id)).thenReturn(Optional.of(habitacionExistente));  
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(HabitacionActualizada);  
        when(habitacionMapper.toHabitacionResponseDTO(HabitacionActualizada)).thenReturn(habitacionResponseDTO);  
  
        // ACTION  
        ResponseDTO responseDTO = habitacionServiceImpl.updateHabitacion(id,habitacionUpdateDTO);  
  
        // Assert Verificacion  
  
        assertThat(responseDTO).isNotNull(); // datos poblados correctamente  
        assertThat(responseDTO.mensaje()).isEqualTo("Modification completed successfully");  
        assertThat(responseDTO.data()).isInstanceOf(HabitacionResponseDTO.class);  
  
        HabitacionResponseDTO result = (HabitacionResponseDTO) responseDTO.data();  
  
        assertThat(result.id()).isEqualTo(id);  
        assertThat(result.numero()).isEqualTo(202);  
        assertThat(result.piso()).isEqualTo(2);  
        assertThat(result.tipo()).isEqualTo("Doble");  
        assertThat(result.estado()).isEqualTo(EstadoHabitacion.MANTENIMIENTO);  
  
        // Verificar llamadas  
        verify(habitacionRepository).findById(id);  
        verify(habitacionRepository).save(habitacionExistente);  
        verify(habitacionMapper).toHabitacionResponseDTO(HabitacionActualizada);  
  
    }  
  
  
    @Test  
    void shouldUpdateHabitacionNotFound(){  
        // Given  
        Long id = 999L;  
        when(habitacionRepository.findById(id)).thenReturn(Optional.empty());  
  
        HabitacionUpdateDTO habitacionUpdateDTO = new HabitacionUpdateDTO(1,101,"Basico", EstadoHabitacion.DISPONIBLE);  
  
        // When / Then  
        assertThatThrownBy(()-> habitacionServiceImpl.updateHabitacion(id, habitacionUpdateDTO))  
                .isInstanceOf(ResourceNotFoundException.class)  
                .hasMessage("Habitacion en id " + id + " no encontrado");  
  
        verify(habitacionRepository,times(1)).findById(id);  
    }  
  
  
    @Test  
    void shouldDeleteHabitacionSuccessfully() {  
        // Arrange  
        Long id = 1L;  
        Habitacion habitacion =  new Habitacion(id,101,1,"Basico", EstadoHabitacion.DISPONIBLE);  
  
        // habitacion existe  
        when(habitacionRepository.findById(id)).thenReturn(Optional.of(habitacion));  
  
        // Act  
        ResponseDTO responseDTO = habitacionServiceImpl.eliminarHabitacion(id);  
  
        // Assert  
        assertThat(responseDTO).isNotNull();  
        assertThat(responseDTO.mensaje()).isEqualTo("Deletion completed successfully");  
        assertThat(responseDTO.data()).isEqualTo("Id eliminado : "+ id);  
  
        // Verify interacciones  
        verify(habitacionRepository).findById(id);  
        verify(habitacionRepository).deleteById(id);  
    }  
  
  
    @Test  
    void shouldDeleteHabitacionNotFound(){  
        // Given  
        Long id = 999L;  
  
        when(habitacionRepository.findById(id)).thenReturn(Optional.empty());  
  
        //Wheb / Then  
  
        assertThatThrownBy(() -> habitacionServiceImpl.eliminarHabitacion(id))  
                .isInstanceOf(ResourceNotFoundException.class)  
                .hasMessage("Habitacion en id " + id + " no encontrado");  
  
        verify(habitacionRepository,times(1)).findById(id);  
  
    }  
}
```
---
### ReservaServiceImplTest.java

```java
@ExtendWith(MockitoExtension.class)  
public class ReservaServiceImplTest {  
  
    //  
    @Mock  
    private ReservaRepository reservaRepository;  
  
    @Mock  
    private HabitacionRepository habitacionRepository;  
  
    @Mock  
    private ReservaMapper reservaMapper;  
  
    @InjectMocks  
    private ReservaServiceImpl reservaServiceImpl;  
  
    @Test  
    void crearReservaPlana() {  
        // Arrange  
        Long idHabitacion = 1L;  
        Long idReserva = 1L;  
  
        Habitacion habitacion1 = new Habitacion();  
        habitacion1.setId(idHabitacion);  
        habitacion1.setNumero(101);  
        habitacion1.setPiso(1);  
        habitacion1.setTipo("Simple");  
        habitacion1.setEstado(EstadoHabitacion.DISPONIBLE);  
  
        Reserva reserva1 = new Reserva();  
        reserva1.setId(idReserva);  
        reserva1.setHabitacion(habitacion1);  
        reserva1.setClienteNombre("Juan Pérez");  
        reserva1.setClienteDni("12345678");  
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 1));  
        reserva1.setFechaFin(LocalDate.of(2025, 5, 5));  
        reserva1.setComentarios("Vista al mar");  
        reserva1.setEstado(EstadoReserva.REALIZADA);  
  
        ReservaRequestDTO reservaRequestDTO = new ReservaRequestDTO(  
                idHabitacion,  
                "Juan Pérez",  
                "12345678",  
                LocalDate.of(2025, 5, 1),  
                LocalDate.of(2025, 5, 5),  
                "Vista al mar"  
        );  
  
        PlanoReservaResponseDTO planoReservaResponseDTO = new PlanoReservaResponseDTO(  
                idReserva,  
                101,  
                "Simple",  
                "Juan Pérez",  
                LocalDate.of(2025, 5, 1),  
                LocalDate.of(2025, 5, 5),  
                "Vista al mar",  
                EstadoReserva.REALIZADA,  
                LocalDateTime.of(2025, 4, 1, 10, 30),  
                null  
        );  
  
        // Mocks  
        when(habitacionRepository.findById(idHabitacion)).thenReturn(Optional.of(habitacion1));  
        when(reservaMapper.toReserva(reservaRequestDTO)).thenReturn(reserva1);  
        when(reservaRepository.save(reserva1)).thenReturn(reserva1);  
        when(habitacionRepository.save(habitacion1)).thenReturn(habitacion1);  
        when(reservaMapper.toPlanoReservaResponseDto(reserva1)).thenReturn(planoReservaResponseDTO);  
  
        // Act  
        ResponseDTO responseDTO = reservaServiceImpl.crearReservaPlana(reservaRequestDTO);  
  
        // Assert  
        assertThat(habitacion1.getEstado()).isEqualTo(EstadoHabitacion.RESERVADA);  
        assertThat(responseDTO.mensaje()).isEqualTo("Added successfully");  
        assertThat(responseDTO.data()).isEqualTo(planoReservaResponseDTO);  
  
  
        verify(habitacionRepository).findById(idHabitacion);  
        verify(reservaMapper).toReserva(reservaRequestDTO);  
        verify(habitacionRepository).save(habitacion1);  
        verify(reservaRepository).save(reserva1);  
        verify(reservaMapper).toPlanoReservaResponseDto(reserva1);  
    }  
  
  
    @Test  
    void shouldThrowExceptionWhenHabitacionNotFound() {  
        // Given  
        Long habitacionIdInexistente = 999L;  
  
        ReservaRequestDTO reservaRequestDTO = new ReservaRequestDTO(  
                habitacionIdInexistente,  
                "Carlos De",  
                "12345678",  
                LocalDate.of(2025, 5, 1),  
                LocalDate.of(2025, 5, 5),  
                "Reservar para evento"  
        );  
  
        // El mapper genera una reserva vacía (necesario para evitar null)  
        Reserva reserva = new Reserva();  
        when(reservaMapper.toReserva(reservaRequestDTO)).thenReturn(reserva);  
  
        // Simulamos que la habitación no existe  
        when(habitacionRepository.findById(habitacionIdInexistente))  
                .thenReturn(Optional.empty());  
  
        // When / Then  
        assertThatThrownBy(() -> reservaServiceImpl.crearReservaPlana(reservaRequestDTO))  
                .isInstanceOf(ResourceNotFoundException.class)  
                .hasMessage("Habitacion no encontrada :" + habitacionIdInexistente);  
  
        // Verifica que se llamó a los mocks necesarios  
        verify(habitacionRepository).findById(habitacionIdInexistente);  
        verify(reservaMapper).toReserva(reservaRequestDTO); // sí se usa  
        verifyNoInteractions(reservaRepository); // este sí se mantiene  
    }  
  
    @Test  
    void shouldThrowExceptionWhenHabitacionNotAvailable() {  
        // Given  
        Long habitacionId = 2L;  
  
        ReservaRequestDTO reservaRequestDTO = new ReservaRequestDTO(  
                habitacionId,  
                "Lucía Vargas",  
                "87654321",  
                LocalDate.now().plusDays(1),  
                LocalDate.now().plusDays(3),  
                "Cliente frecuente"  
        );  
  
        Reserva reserva = new Reserva();  
        when(reservaMapper.toReserva(reservaRequestDTO)).thenReturn(reserva);  
  
        // Creamos una habitación que NO está disponible  
        Habitacion habitacionNoDisponible = new Habitacion(  
                habitacionId,  
                105,  
                1,  
                "Suite",  
                EstadoHabitacion.MANTENIMIENTO  
        );  
  
        when(habitacionRepository.findById(habitacionId))  
                .thenReturn(Optional.of(habitacionNoDisponible));  
  
        // When / Then  
        assertThatThrownBy(() -> reservaServiceImpl.crearReservaPlana(reservaRequestDTO))  
                .isInstanceOf(ResourceNotFoundException.class)  
                .hasMessage(" la habitacion no esta disponible para reservar :"  
                        + habitacionNoDisponible.getEstado());  
  
        verify(habitacionRepository).findById(habitacionId);  
        verify(reservaMapper).toReserva(reservaRequestDTO);  
        verifyNoInteractions(reservaRepository); // no se debería guardar ninguna reserva  
    }  
  
  
    // page reserva response dto  
  
    @Test  
    void shouldListAllReservasPaginated() {  
        // Arrange  
        Pageable pageable = PageRequest.of(0, 3);  
  
        Habitacion habitacion1 = new Habitacion(1L, 101, 1, "Matrimonial", EstadoHabitacion.RESERVADA);  
        Habitacion habitacion2 = new Habitacion(2L, 201, 2, "Basica", EstadoHabitacion.RESERVADA);  
  
        Reserva reserva1 = new Reserva();  
        reserva1.setId(1L);  
        reserva1.setHabitacion(habitacion1);  
        reserva1.setClienteNombre("Ana");  
        reserva1.setClienteDni("11112222");  
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 1));  
        reserva1.setFechaFin(LocalDate.of(2025, 5, 5));  
        reserva1.setComentarios("Comentario 1");  
        reserva1.setEstado(EstadoReserva.REALIZADA);  
  
        Reserva reserva2 = new Reserva();  
        reserva1.setId(2L);  
        reserva1.setHabitacion(habitacion2);  
        reserva1.setClienteNombre("Luis");  
        reserva1.setClienteDni("33334444");  
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 2));  
        reserva1.setFechaFin(LocalDate.of(2025, 5, 6));  
        reserva1.setComentarios("Comentario 2");  
        reserva1.setEstado(EstadoReserva.REALIZADA);  
  
        List<Reserva> reservas = List.of(reserva1, reserva2);  
  
        Page<Reserva> pageReservas = new PageImpl<>(reservas,pageable,reservas.size());  
  
        PlanoReservaResponseDTO planoReservaResponseDTO1 = new PlanoReservaResponseDTO(  
                1L,  
                101,  
                "Matrimonial",  
                "Ana",  
                LocalDate.of(2025, 5, 1),  
                LocalDate.of(2025, 5, 5),  
                "Comentario 1",  
                EstadoReserva.REALIZADA,  
                LocalDateTime.of(2025, 4, 1, 10, 30),  
                null  
        );  
  
        PlanoReservaResponseDTO planoReservaResponseDTO2 = new PlanoReservaResponseDTO(  
                2L,  
                201,  
                "Basica",  
                "Luis",  
                LocalDate.of(2025, 5, 2),  
                LocalDate.of(2025, 5, 6),  
                "Comentario 2",  
                EstadoReserva.REALIZADA,  
                LocalDateTime.of(2025, 4, 1, 10, 30),  
                null  
        );  
  
        when(reservaRepository.findAll(pageable)).thenReturn(pageReservas);  
        when(reservaMapper.toPlanoReservaResponseDto(reserva1)).thenReturn(planoReservaResponseDTO1);  
        when(reservaMapper.toPlanoReservaResponseDto(reserva2)).thenReturn(planoReservaResponseDTO2);  
  
        // act  
        PageResponseDTO<PlanoReservaResponseDTO> resultado = reservaServiceImpl.listadoReservas(pageable);  
  
        // Assert  
  
        assertThat(resultado).isNotNull();  
        assertThat(resultado.content()).hasSize(2);  
        assertThat(resultado.content()).containsExactly(planoReservaResponseDTO1, planoReservaResponseDTO2);  
        assertThat(resultado.totalElements()).isEqualTo(2);  
        assertThat(resultado.page()).isEqualTo(0);  
        assertThat(resultado.size()).isEqualTo(3);  
  
        //verificar  
  
        verify(reservaRepository).findAll(pageable);  
        verify(reservaMapper).toPlanoReservaResponseDto(reserva1);  
        verify(reservaMapper).toPlanoReservaResponseDto(reserva2);  
  
    }  
  
    @Test  
    void shouldReturnReservaWhenIdExists(){  
  
        // Given  
        Long idReserva = 1L;  
        Habitacion habitacion1 = new Habitacion(1L, 101, 1, "Matrimonial", EstadoHabitacion.RESERVADA);  
  
        Reserva reserva1 = new Reserva();  
        reserva1.setId(1L);  
        reserva1.setHabitacion(habitacion1);  
        reserva1.setClienteNombre("Ana");  
        reserva1.setClienteDni("11112222");  
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 1));  
        reserva1.setFechaFin(LocalDate.of(2025, 5, 5));  
        reserva1.setComentarios("Comentario 1");  
        reserva1.setEstado(EstadoReserva.REALIZADA);  
  
        PlanoReservaResponseDTO planoReservaResponseDTO1 = new PlanoReservaResponseDTO(  
                1L,  
                101,  
                "Matrimonial",  
                "Ana",  
                LocalDate.of(2025, 5, 1),  
                LocalDate.of(2025, 5, 5),  
                "Comentario 1",  
                EstadoReserva.REALIZADA,  
                LocalDateTime.of(2025, 4, 1, 10, 30),  
                null  
        );  
  
        // When  
  
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva1));  
        when(reservaMapper.toPlanoReservaResponseDto(reserva1)).thenReturn(planoReservaResponseDTO1);  
  
        // ACT  
        PlanoReservaResponseDTO resultado = reservaServiceImpl.buscarReserva(idReserva);  
  
        // ASSERT  
        assertThat(resultado).isNotNull();  
        assertThat(resultado.reservaId()).isEqualTo(idReserva);  
        assertThat(resultado.habitacionNumero()).isEqualTo(101);  
        assertThat(resultado.habitacionTipo()).isEqualTo("Matrimonial");  
        assertThat(resultado.clienteNombre()).isEqualTo("Ana");  
        assertThat(resultado.fechaInicio()).isEqualTo(LocalDate.of(2025, 5, 1));  
        assertThat(resultado.fechaFin()).isEqualTo(LocalDate.of(2025, 5, 5));  
        assertThat(resultado.comentarios()).isEqualTo("Comentario 1");  
        assertThat(resultado.estadoReserva()).isEqualTo(EstadoReserva.REALIZADA);  
        assertThat(resultado.fechaCreacion()).isEqualTo(LocalDateTime.of(2025, 4, 1, 10, 30));  
        assertThat(resultado.fechaCulminada()).isNull();  
  
        verify(reservaRepository).findById(idReserva);  
        verify(reservaMapper).toPlanoReservaResponseDto(reserva1);  
  
    }  
  
  
    @Test  
    void shouldReturnReservaWhenIdDoesNotExist(){  
  
        // Given  
        Long idReserva = 999L;  
  
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());  
  
        // When  
        assertThatThrownBy(() -> reservaServiceImpl.buscarReserva(idReserva))  
                .isInstanceOf(ResourceNotFoundException.class)  
                .hasMessageContaining("Reserva no encontrada :"+idReserva);  
  
        // THen  
        verify(reservaRepository).findById(idReserva);  
  
    }  
  
    //  
    @Test  
    void shouldCulminarReservaWhenIdExists(){  
  
        // Given  
        Long idReserva = 1L;  
        Habitacion habitacion1 = new Habitacion(1L, 101, 1, "Matrimonial", EstadoHabitacion.RESERVADA);  
  
        Reserva reserva1 = new Reserva();  
        reserva1.setId(1L);  
        reserva1.setHabitacion(habitacion1);  
        reserva1.setClienteNombre("Ana");  
        reserva1.setClienteDni("11112222");  
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 1));  
        reserva1.setFechaFin(LocalDate.of(2025, 5, 5));  
        reserva1.setComentarios("Comentario 1");  
        reserva1.setEstado(EstadoReserva.REALIZADA);  
  
        PlanoReservaResponseDTO planoReservaResponseDTO1 = new PlanoReservaResponseDTO(  
                1L,  
                101,  
                "Matrimonial",  
                "Ana",  
                LocalDate.of(2025, 5, 1),  
                LocalDate.of(2025, 5, 5),  
                "Comentario 1",  
                EstadoReserva.CULMINADA,  
                LocalDateTime.of(2025, 4, 1, 10, 30),  
                LocalDateTime.of(2025, 4, 5, 10, 30)  
        );  
  
  
        // When  
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva1));  
        when(reservaMapper.toPlanoReservaResponseDto(reserva1)).thenReturn(planoReservaResponseDTO1);  
  
        ResponseDTO responseDTO = reservaServiceImpl.culminarReserva(idReserva);  
  
  
        // Assert  
        assertThat(responseDTO.mensaje()).isEqualTo("Modification completed successfully");  
        assertThat(responseDTO.data()).isEqualTo(planoReservaResponseDTO1);  
        assertThat(((PlanoReservaResponseDTO) responseDTO.data()).reservaId()).isEqualTo(idReserva);  
        assertThat(reserva1.getEstado()).isEqualTo(EstadoReserva.CULMINADA);  
        assertThat(reserva1.getFechaCulminada()).isNotNull();  
        assertThat(reserva1.getHabitacion().getEstado()).isEqualTo(EstadoHabitacion.DISPONIBLE);  
  
        // verify  
        verify(reservaRepository).findById(idReserva);  
        verify(reservaMapper).toPlanoReservaResponseDto(reserva1);  
        verify(reservaRepository).save(reserva1);  
  
    }  
  
    @Test  
    void shouldCulminarReservaWhenIdExistsNotFound(){  
        // Given  
        Long idReserva = 999L;  
  
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());  
  
        // When  
        assertThatThrownBy(() -> reservaServiceImpl.culminarReserva(idReserva))  
                .isInstanceOf(ResourceNotFoundException.class)  
                .hasMessageContaining("Reserva no encontrada :"+idReserva);  
  
        // THen  
        verify(reservaRepository).findById(idReserva);  
    }  
  
  
    @Test  
    void shouldCulminarReservaWhenIdDoesNotExist(){  
        // Arrange  
        Pageable pageable = PageRequest.of(0, 3);  
        LocalDateTime desde = LocalDateTime.of(2025, 4, 1, 0, 0);  
        LocalDateTime hasta = LocalDateTime.of(2025, 4, 30, 23, 59);  
  
        Habitacion habitacion1 = new Habitacion(1L, 101, 1, "Matrimonial", EstadoHabitacion.RESERVADA);  
  
        Reserva reserva1 = new Reserva();  
        reserva1.setId(1L);  
        reserva1.setHabitacion(habitacion1);  
        reserva1.setClienteNombre("Ana");  
        reserva1.setClienteDni("11112222");  
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 1));  
        reserva1.setFechaFin(LocalDate.of(2025, 5, 5));  
        reserva1.setComentarios("Comentario 1");  
        reserva1.setEstado(EstadoReserva.REALIZADA);  
        reserva1.setFechaCreacion(LocalDateTime.of(2025, 4, 10, 12, 0));  
  
        PlanoReservaResponseDTO dto = new PlanoReservaResponseDTO(  
                1L,  
                101,  
                "Simple",  
                "Carlos",  
                LocalDate.of(2025, 5, 1),  
                LocalDate.of(2025, 5, 3),  
                "Comentario",  
                EstadoReserva.REALIZADA,  
                reserva1.getFechaCreacion(),  
                null  
        );  
  
        Page<Reserva> reservas = new PageImpl<>(List.of(reserva1),pageable,1);  
  
        when(reservaRepository.findByFechaCreacionBetween(pageable,desde,hasta)).thenReturn(reservas);  
        when(reservaMapper.toPlanoReservaResponseDto(reserva1)).thenReturn(dto);  
  
        // Act  
        PageResponseDTO<PlanoReservaResponseDTO> resultado = reservaServiceImpl.filtradoFechaInicio(pageable,desde,hasta);  
  
        // Assert  
        assertThat(resultado).isNotNull();  
        assertThat(resultado.content()).hasSize(1);  
        assertThat(resultado.content().get(0)).isEqualTo(dto);  
        assertThat(resultado.totalElements()).isEqualTo(1);  
        assertThat(resultado.page()).isEqualTo(0);  
        assertThat(resultado.size()).isEqualTo(3);  
  
        // verificar  
        verify(reservaRepository).findByFechaCreacionBetween(pageable,desde,hasta);  
        verify(reservaMapper).toPlanoReservaResponseDto(reserva1);  
    }  
}
```

---
# POM.XML
```xml
<?xml version="1.0" encoding="UTF-8"?>  
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">  
    <modelVersion>4.0.0</modelVersion>  
    <parent>        <groupId>org.springframework.boot</groupId>  
        <artifactId>spring-boot-starter-parent</artifactId>  
        <version>3.4.4</version>  
        <relativePath/> <!-- lookup parent from repository -->  
    </parent>  
    <groupId>edu.com</groupId>  
    <artifactId>RentApi</artifactId>  
    <version>0.0.1-SNAPSHOT</version>  
    <name>RentApi</name>  
    <description>RentApi</description>  
    <url/>  
    <properties>        <java.version>21</java.version>  
        <org.mapstruct.version>1.6.3</org.mapstruct.version>  
  
    </properties>  
    <dependencies>        <dependency>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-data-jpa</artifactId>  
        </dependency>  
        <dependency>            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-web</artifactId>  
        </dependency>  
  
        <dependency>            <groupId>com.mysql</groupId>  
            <artifactId>mysql-connector-j</artifactId>  
            <scope>runtime</scope>  
        </dependency>  
        <dependency>            <groupId>org.projectlombok</groupId>  
            <artifactId>lombok</artifactId>  
            <optional>true</optional>  
        </dependency>  
        <dependency>            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-test</artifactId>  
            <scope>test</scope>  
        </dependency>  
  
        <!-- validaciones dtos , records -->  
        <dependency>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-validation</artifactId>  
        </dependency>  
  
        <!-- MapStruct -->  
        <dependency>  
            <groupId>org.mapstruct</groupId>  
            <artifactId>mapstruct</artifactId>  
            <version>${org.mapstruct.version}</version>  
        </dependency>  
  
        <dependency>            <groupId>org.mapstruct</groupId>  
            <artifactId>mapstruct-processor</artifactId>  
            <version>${org.mapstruct.version}</version>  
            <scope>provided</scope>  
        </dependency>  
  
        <!-- SWAGGER -->  
        <dependency>  
            <groupId>org.springdoc</groupId>  
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>  
            <version>2.3.0</version>  
        </dependency>  
  
  
        <!-- MOCKITO -->  
        <dependency>  
            <groupId>org.mockito</groupId>  
            <artifactId>mockito-core</artifactId>  
            <scope>test</scope>  
        </dependency>  
  
        <dependency>            <groupId>org.mockito</groupId>  
            <artifactId>mockito-core</artifactId>  
            <scope>test</scope>  
        </dependency>  
  
  
    </dependencies>  
  
    <build>        <plugins>  
  
            <!-- JaCoCo Plugin -->  
            <plugin>  
                <groupId>org.jacoco</groupId>  
                <artifactId>jacoco-maven-plugin</artifactId>  
                <version>0.8.12</version>  
                <executions>                    <execution>  
                        <goals>  
                            <goal>prepare-agent</goal>  
                        </goals>  
                    </execution>  
                    <execution>                        <id>report</id>  
                        <phase>verify</phase>  
                        <goals>                            <goal>report</goal>  
                        </goals>  
                    </execution>  
                    <execution>                        <id>jacoco-check</id>  
                        <goals>                            <goal>check</goal>  
                        </goals>  
                        <configuration>                            <rules>  
                                <rule>  
                                    <element>PACKAGE</element>  
                                    <limits>                                        <limit>  
                                            <counter>LINE</counter>  
                                            <value>COVEREDRATIO</value>  
                                            <minimum>0.85</minimum>  
                                        </limit>  
                                    </limits>  
                                </rule>  
                            </rules>  
                        </configuration>  
                    </execution>  
                </executions>  
            </plugin>  
            <plugin>                <groupId>org.apache.maven.plugins</groupId>  
                <artifactId>maven-compiler-plugin</artifactId>  
                <configuration>                    <annotationProcessorPaths>  
                        <path>  
                            <groupId>org.projectlombok</groupId>  
                            <artifactId>lombok</artifactId>  
                        </path>  
                        <path>                            <!-- MapStruct -->  
                            <groupId>org.mapstruct</groupId>  
                            <artifactId>mapstruct-processor</artifactId>  
                            <version>${org.mapstruct.version}</version>  
                        </path>  
                    </annotationProcessorPaths>  
                </configuration>  
            </plugin>  
            <plugin>                <groupId>org.springframework.boot</groupId>  
                <artifactId>spring-boot-maven-plugin</artifactId>  
                <configuration>                    <excludes>  
                        <exclude>  
                            <groupId>org.projectlombok</groupId>  
                            <artifactId>lombok</artifactId>  
                        </exclude>  
                    </excludes>  
                </configuration>  
            </plugin>  
  
  
  
        </plugins>  
    </build>  
  
</project>
```
---

# Properties
```bash
spring.application.name=RentApi  
spring.datasource.url=jdbc:mysql://localhost:3306/RentApi?createDatabaseIfNotExist=true  
spring.datasource.username=root  
spring.datasource.password=password  
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver  
  
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect  
spring.jpa.hibernate.ddl-auto=update  
spring.jpa.show-sql=true  
  
springdoc.api-docs.path=/v3/api-docs  
springdoc.swagger-ui.path=/swagger-ui.html
```
---
# 🚗 RentApi - Backend Java con Spring Boot

![Java](https://img.shields.io/badge/Java-21-orange?logo=java&logoWidth=14)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen?logo=spring&logoWidth=14)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue?logo=mysql&logoWidth=14)
![MapStruct](https://img.shields.io/badge/MapStruct-1.6.3-yellow?logo=mapstruct&logoWidth=14)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red?logo=lombok&logoWidth=14)
![Swagger](https://img.shields.io/badge/API%20Docs-Swagger-informational?logo=swagger&logoWidth=14)

## 📚 Descripción

Este es un proyecto backend desarrollado con **Spring Boot 3.4.4** para gestionar el sistema de alquiler de habitaciones. Usa una arquitectura sencilla y moderna, con integración de base de datos MySQL, documentación de API con Swagger y mapeo de DTOs usando MapStruct.

---
## 🚀 Tecnologías utilizadas

- ☕ **Java 21**
- 🌱 **Spring Boot Web & Data JPA**
- 🛢️ **MySQL**
- 🔁 **MapStruct** – para mapeo entre entidades y DTOs
- 🔒 **Validaciones con Jakarta Validation**
- 🧪 **Spring Boot Test**
- 📚 **Swagger (Springdoc OpenAPI)**
---
## ⚙️ ¿Cómo levantar el proyecto?

### 1. Clonar el repositorio

```bash
git clone https://github.com/Alexisfllv/project_java_RentApi.git
cd project_java_RentApi
```

### 2. Configurar la base de datos

Asegúrate de tener una base de datos MySQL creada. Agrega tu configuración en `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rentadb
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Construir el proyecto con Maven

```bash
./mvnw clean install
```
### 4. Levantar el proyecto

```bash
./mvnw spring-boot:run
```

El backend estará corriendo en:
📍 `http://localhost:8080`

---
## 📘 Documentación Swagger

Accede a la documentación automática de la API en:

🔗 [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

---
## 🧰 Buenas prácticas

- Usa **DTOs** para exponer solo los datos necesarios.
- Valida entradas con `@Valid` en tus controladores.
- Usa **MapStruct** para mantener limpio el código de conversión.
- Documenta todos tus endpoints con anotaciones de Swagger.
- Separa la lógica de negocio en **services**.
---
## 📤 Autor

Desarrollado por: [AlexisFl]
📧 Contacto: alexisxscfl@gmail.com

# Puntos a mejorar
* Agregar Spring Secutiry
* Usar perfiles : dev, prod
