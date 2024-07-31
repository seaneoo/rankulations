package com.seaneoo.regulation_apple.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static jakarta.servlet.RequestDispatcher.*;

@RestController
public class GlobalErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ExceptionResponse> handleErrorMapping(HttpServletRequest req, HttpServletResponse res) {
        var statusCode = Integer.parseInt(req.getAttribute(ERROR_STATUS_CODE)
                .toString());
        var errorMessage = "";

        var throwable = (Throwable) req.getAttribute(ERROR_EXCEPTION);
        if (throwable != null) {
            if (throwable instanceof ResponseStatusException) {
                errorMessage = ((ResponseStatusException) throwable).getReason();
                statusCode = ((ResponseStatusException) throwable).getStatusCode()
                        .value();
            } else {
                errorMessage = throwable.getMessage();
            }
        }

        var httpStatus = HttpStatus.valueOf(statusCode);
        var requestUri = req.getAttribute(ERROR_REQUEST_URI);

        var response = ExceptionResponse.builder()
                .statusCode(httpStatus.value())
                .statusReason(httpStatus.getReasonPhrase())
                .path(requestUri.toString())
                .message(errorMessage)
                .build();
        return ResponseEntity.status(httpStatus)
                .body(response);
    }
}
