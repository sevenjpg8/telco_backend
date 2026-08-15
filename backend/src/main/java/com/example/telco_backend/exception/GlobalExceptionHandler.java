package com.example.telco_backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(RecursoNoEncontradoException.class)
        public ResponseEntity<Map<String, Object>> manejarNoEncontrado(
                        RecursoNoEncontradoException ex, WebRequest request) {

                return crearRespuesta(
                                HttpStatus.NOT_FOUND,
                                "Recurso no encontrado",
                                ex.getMessage(),
                                request);
        }

        @ExceptionHandler(ReglaNegocioException.class)
        public ResponseEntity<Map<String, Object>> manejarReglaNegocio(
                        ReglaNegocioException ex, WebRequest request) {

                return crearRespuesta(
                                HttpStatus.BAD_REQUEST,
                                "Regla de negocio",
                                ex.getMessage(),
                                request);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidationException(
                        MethodArgumentNotValidException ex,
                        WebRequest request) {

                Map<String, String> errores = new HashMap<>();

                ex.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> {
                                        errores.put(
                                                        error.getField(),
                                                        error.getDefaultMessage());
                                });

                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("path", request.getDescription(false).replace("uri=", ""));
                response.put("error", "Bad Request");
                response.put("message", "Error de validación");
                response.put("errors", errores);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
                        DataIntegrityViolationException ex, WebRequest request) {

                return crearRespuesta(
                                HttpStatus.CONFLICT,
                                "Conflict",
                                "El código de llamada ya existe",
                                request);
        }

        private ResponseEntity<Map<String, Object>> crearRespuesta(
                        HttpStatusCode status,
                        String error,
                        String message,
                        WebRequest request) {

                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("path", request.getDescription(false).replace("uri=", ""));
                response.put("error", error);
                response.put("message", message);

                return ResponseEntity
                                .status(status.value())
                                .body(response);
        }

}
