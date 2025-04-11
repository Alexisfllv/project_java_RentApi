package edu.com.rentapi.Exception;


/**
 * Excepción personalizada para reglas de negocio inválidas.
 * Ejemplo: datos inconsistentes, duplicados, lógicos inválidos, etc.
 */
public class ExInvalidDataException extends RuntimeException {

    public ExInvalidDataException(String message) {
        super(message);
    }
}