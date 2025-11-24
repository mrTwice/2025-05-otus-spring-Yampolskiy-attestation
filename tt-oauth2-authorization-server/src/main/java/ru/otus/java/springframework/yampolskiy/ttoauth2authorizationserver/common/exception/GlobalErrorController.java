package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.common.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GlobalErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorController.class);

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object statusCodeAttr = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object messageAttr = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exceptionAttr = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        int statusCode = (statusCodeAttr instanceof Integer code) ? code : 500;
        HttpStatus httpStatus = HttpStatus.resolve(statusCode);
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String message = (messageAttr instanceof String m) ? m : "Unexpected error";

        LOGGER.error("❌ Обработка ошибки: status={}, path={}, message={}, exception={}",
                statusCode, path, message, exceptionAttr);

        Map<String, Object> errorResponse = buildBody(statusCode, httpStatus, message, path);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private static Map<String, Object> buildBody(int statusCode, HttpStatus httpStatus, String message, String path) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", Instant.now());
        errorResponse.put("status", statusCode);
        errorResponse.put("error", httpStatus.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("path", path);
        return errorResponse;
    }
}