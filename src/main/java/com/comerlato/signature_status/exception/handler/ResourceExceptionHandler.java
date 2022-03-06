package com.comerlato.signature_status.exception.handler;

import com.comerlato.signature_status.helper.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import static com.comerlato.signature_status.exception.ErrorCodeEnum.ERROR_DUPLICATED_FIELD;
import static com.comerlato.signature_status.exception.ErrorCodeEnum.ERROR_GENERIC_EXCEPTION;
import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ResourceExceptionHandler {

    private final MessageHelper messageHelper;
    @Value("${api.standard-error.trace}")
    private boolean isTrace;

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    void exceptionHandler(ValidationException e) {
        throw new ResponseStatusException(BAD_REQUEST, e.getMessage(), e);
    }

    @ResponseBody
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDTO> handlerResponseStatus(ResponseStatusException e, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorDTO.builder()
                .message(e.getReason())
                .error(e.getStatus().getReasonPhrase())
                .path(request.getRequestURI())
                .status(e.getStatus().value())
                .build(), e.getStatus());
    }

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDataIntegrityException(DataIntegrityViolationException e, HttpServletRequest request) {
        PSQLException ex = (PSQLException) e.getCause().getCause();
        String errorMessage;
        if (ex.getServerErrorMessage().getConstraint().equalsIgnoreCase("add")) {
            String field = ex.getMessage().substring(ex.getMessage().indexOf("(") + 1, ex.getMessage().indexOf(")"));
            errorMessage = messageHelper.get(ERROR_DUPLICATED_FIELD, field);
        } else {
            errorMessage = ex.getMessage();
        }
        return new ResponseEntity<>(ErrorDTO.builder()
                .error(e.getMessage())
                .message(errorMessage)
                .path(request.getRequestURI())
                .status(BAD_REQUEST.value())
                .build(), BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<com.comerlato.signature_status.exception.handler.StandardError> handleConstraintViolationExceptions(ConstraintViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error(e.getMessage());
        return ResponseEntity
                .status(status)
                .body(com.comerlato.signature_status.exception.handler.StandardError
                        .builder()
                        .status(status.value())
                        .error("Method argument not valid")
                        .message(e.getConstraintViolations()
                                .stream()
                                .map(objectError -> format("{0}: {1}", ((FieldError) objectError).getField(), (objectError).getMessage()))
                                .sorted()
                                .collect(Collectors.toList()))
                        .path(request.getRequestURI())
                        .trace(isTrace ? ExceptionUtils.getStackTrace(e) : null)
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<com.comerlato.signature_status.exception.handler.StandardError> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error(e.getMessage());
        return ResponseEntity
                .status(status)
                .body(com.comerlato.signature_status.exception.handler.StandardError
                        .builder()
                        .status(status.value())
                        .error("Method argument not valid")
                        .message(e.getBindingResult()
                                .getAllErrors()
                                .stream()
                                .map(objectError -> format("{0}: {1}", ((FieldError) objectError).getField(), (objectError).getDefaultMessage()))
                                .sorted()
                                .collect(Collectors.toList()))
                        .path(request.getRequestURI())
                        .trace(isTrace ? ExceptionUtils.getStackTrace(e) : null)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<com.comerlato.signature_status.exception.handler.StandardError> handlerGenericException(Exception e, HttpServletRequest request) {
        HttpStatus status = BAD_REQUEST;
        log.error(request.getRequestURI(), e);
        return ResponseEntity
                .status(status)
                .body(com.comerlato.signature_status.exception.handler.StandardError.builder()
                        .status(status.value())
                        .error("Server Error")
                        .message(List.of(messageHelper.get(ERROR_GENERIC_EXCEPTION, e)))
                        .path(request.getRequestURI())
                        .trace(isTrace ? ExceptionUtils.getStackTrace(e) : null)
                        .build());
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<com.comerlato.signature_status.exception.handler.StandardError> handlerParseException(ParseException e, HttpServletRequest request) {
        HttpStatus status = BAD_REQUEST;
        log.error(request.getRequestURI(), e);
        return ResponseEntity
                .status(status)
                .body(com.comerlato.signature_status.exception.handler.StandardError.builder()
                        .status(status.value())
                        .error("Server Error")
                        .message(List.of(e.getMessage()))
                        .path(request.getRequestURI())
                        .trace(isTrace ? ExceptionUtils.getStackTrace(e) : null)
                        .build());
    }
}
