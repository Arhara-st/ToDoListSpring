package org.spring.todolistspring.commons.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    // Обработчик ошибок десериализации JSON (например, неверный ENUM)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseException(HttpMessageNotReadableException e) {
        String errorMessage = "Invalid request: " + extractEnumErrorMessage(e);
        log.error(errorMessage, e);
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", errorMessage));
    }

    // Обработчик NotFoundException
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", exception.getMessage()));
    }

    // Обработчик ошибок при передаче Enum через PathVariable
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleEnumConversionException(MethodArgumentTypeMismatchException e) {
        if (e.getRequiredType() != null && e.getRequiredType().isEnum()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message",
                            "Invalid value on status: " + e.getValue() + ". Allowed values: "
                                    + Arrays.toString(e.getRequiredType().getEnumConstants())));
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Invalid request parameter"));
    }

    // Вспомогательный метод для извлечения ошибки ENUM из исключения
    private String extractEnumErrorMessage(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();
            if (targetType.isEnum()) {
                return "Invalid value on status: " + invalidFormatException.getValue() +
                        ". Allowed values: " + Arrays.toString(targetType.getEnumConstants());
            }
        }
        return "Invalid JSON format";
    }
}
