package com.seaneoo.rankulations.error;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalErrorController.class);

    private String stackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    private <T extends Throwable> ResponseEntity<ExceptionResponse> handleException(T throwable, HttpStatus httpStatus,
                                                                                    HttpServletRequest request) {
        var response = ExceptionResponse.builder()
                .statusCode(httpStatus.value())
                .statusReason(httpStatus.getReasonPhrase())
                .path(request.getRequestURI())
                .message(throwable.getMessage())
                .build();
        return ResponseEntity.status(httpStatus)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(HttpServletRequest req, Exception e) {
        logger.warn(stackTraceAsString(e));
        return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR, req);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoResourceFoundException(HttpServletRequest req,
                                                                            NoResourceFoundException e) {
        return handleException(e, HttpStatus.NOT_FOUND, req);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> handleResponseStatusException(HttpServletRequest req,
                                                                           ResponseStatusException e) {
        return handleException(e, HttpStatus.valueOf(e.getStatusCode()
                .value()), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(HttpServletRequest req,
                                                                                   MethodArgumentNotValidException e) {
        var httpStatus = HttpStatus.BAD_REQUEST;

        var errors = new ArrayList<String>();
        var bindingResult = e.getBindingResult();
        for (var error : bindingResult.getAllErrors())
            errors.add(error.getDefaultMessage());

        var response = ExceptionResponse.builder()
                .statusCode(httpStatus.value())
                .statusReason(httpStatus.getReasonPhrase())
                .path(req.getRequestURI())
                .errors(errors)
                .build();
        return ResponseEntity.status(httpStatus)
                .body(response);
    }
}
