# 🧱 Estructura proyecto

``` python
com.ejemplo.hotel
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
└── HotelApplication.java
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
    private int numero;  
  
    @Column(nullable = false)  
    private int piso;  
  
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
    private EstadoReserva estado = EstadoReserva.REALIZADA; // Reutilizas el enum para saber si está RESERVADA, etc.  
  
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
  
        @Min(value = 1, message = "El piso debe ser mayor o igual a 1")  
        int piso,  
        @Min(value = 1, message = "El número de habitación debe ser mayor o igual a 1")  
        int numero,  
        @NotBlank(message = "El tipo de habitación es obligatorio")  
        @Size(max = 50, message = "El tipo de habitación no debe exceder los 50 caracteres")  
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
	Long id,
	int piso,
	int numero,
	String tipo,
	EstadoHabitacion estado 
	// DISPONIBLE, RESERVADA, MANTENIMIENTO

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

    @Min(value = 1, message = "El piso debe ser mayor o igual a 1")
    int piso,
    @Min(value = 1, message = "El número de habitación debe ser mayor o igual a 1")
    int numero,
    @NotBlank(message = "El tipo de habitación es obligatorio")
    @Size(max = 50, message = "El tipo de habitación no debe exceder los 50 caracteres")
    String tipo,
    @NotNull(message = "El estado de la habitación es obligatorio")
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
	Long habitacionId,
	@NotBlank(message = "El nombre del cliente es obligatorio")
	@Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
	String clienteNombre,
	@NotBlank(message = "El DNI del cliente es obligatorio")
	@Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
	String clienteDni,
	@NotNull(message = "La fecha de inicio es obligatoria")
	@FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
	LocalDate fechaInicio,
	@NotNull(message = "La fecha de fin es obligatoria")
	@Future(message = "La fecha de fin debe ser en el futuro")
	LocalDate fechaFin,
	@Size(max = 255, message = "Los comentarios no deben superar los 255 caracteres")
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
    Long reservaId,  
    int habitacionNumero,  
    String habitacionTipo,  
    String clienteNombre,  
    LocalDate fechaInicio,  
    LocalDate fechaFin,  
    String comentarios,  
    EstadoReserva estadoReserva,  
    LocalDateTime fechaCreacion,  
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

### ReservaServiceimpl.java

```java
@Slf4j  
@Service  
@RequiredArgsConstructor  
public class ReservaServiceimpl implements ReservaService {  
  
  
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
        if (reservaExistente.getHabitacion().getEstado().equals(EstadoHabitacion.DISPONIBLE)) {  
            log.warn("Habitacion estado :"+reservaExistente.getHabitacion().getEstado());  
            throw new ResourceNotFoundException(" la habitacion ya se encuentra disponible:"+ reservaExistente.getHabitacion().getEstado());  
  
        }  
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
# Properties
```bash
spring.application.name=RentApi  
spring.datasource.url=jdbc:mysql://localhost:3306/RentApi?createDatabaseIfNotExist=true  
spring.datasource.username=root  
spring.datasource.password=deadmau5  
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
- 📚 **Swagger (Springdoc OpenAPI)** – documentación automática
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
## Postman Collection

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
* Documentar DTO
* Mejorar respuestas http en los Controladores
* Agregar pruebas unitarias
* Agregar mas tipos de validaciones
* Agregar Spring Secutiry
* Usar perfiles : dev, prod
* 
