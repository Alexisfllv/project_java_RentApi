package edu.com.rentapi.Exception;


import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Errores de validación de campos en el cuerpo JSON (RequestBody).
     * Ejemplo: campo faltante o inválido con @Valid.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed (GlobalExceptionHandler)", errors);
    }

    /**
     * Errores de validación de parámetros individuales (@RequestParam, @PathVariable, etc.).
     * Requiere @Validated en el controlador.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(v -> {
            String field = v.getPropertyPath().toString();
            errors.put(field, v.getMessage());
        });
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request parameters (GlobalExceptionHandler)", errors);
    }

    /**
     * JSON mal formado o tipo de dato inválido (por ejemplo, fecha incorrecta).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request (GlobalExceptionHandler)", null);
    }

    /**
     * Parámetro obligatorio ausente en la URL (ej. @RequestParam faltante).
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        Map<String, String> errors = Map.of(ex.getParameterName(), "Parameter is missing");
        return buildResponse(HttpStatus.BAD_REQUEST, "Missing required parameter (GlobalExceptionHandler)", errors);
    }

    /**
     * Error de tipo en parámetros (ej. id esperado como Long y se envía un String).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = Map.of(ex.getName(), "Invalid type: expected " + ex.getRequiredType());
        return buildResponse(HttpStatus.BAD_REQUEST, "Type mismatch (GlobalExceptionHandler)", errors);
    }

    /**
     * Metodo HTTP no permitido (ej. POST cuando solo se permite GET).
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not supported (GlobalExceptionHandler)", null);
    }

    /**
     * Excepción personalizada para datos inválidos (reglas de negocio).
     */
    @ExceptionHandler(ExInvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleCustom(ExInvalidDataException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage() + " (GlobalExceptionHandler)", null);
    }

    /**
     * Recurso no encontrado (ej. entidad por ID no existe).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage() + " (GlobalExceptionHandler)", null);
    }

    /**
     * Cualquier otra excepción no controlada.
     */
    @ExceptionHandler(Exception.class)
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