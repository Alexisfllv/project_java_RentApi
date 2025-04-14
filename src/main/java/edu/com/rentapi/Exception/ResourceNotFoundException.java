package edu.com.rentapi.Exception;

/**
 * Excepción personalizada para recurso no encontrado (HTTP 404).
 * Ejemplo: entidad no existe por ID u otra clave.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}