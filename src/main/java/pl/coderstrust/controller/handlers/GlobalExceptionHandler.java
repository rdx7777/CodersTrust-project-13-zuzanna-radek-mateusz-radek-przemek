package pl.coderstrust.controller.handlers;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.coderstrust.controller.PdfResponseHelper;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleUnexpectedException(Exception e, WebRequest request) {
        log.error("Handling {} due to {}", e.getClass().getSimpleName(), e.getMessage());
        if (PdfResponseHelper.isPdfResponse(extractHttpHeaders(request))) {
            return createPdfResponse(e);
        }
        return createJsonResponse(e, request);
    }

    private HttpHeaders extractHttpHeaders(WebRequest request) {
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
        headersMap.add("Accept", String.join(" ", request.getHeaderValues("accept")));
        return new HttpHeaders(headersMap);
    }

    private ResponseEntity<Object> createPdfResponse(Exception e) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        if (e instanceof ResponseStatusException) {
            return new ResponseEntity<>(headers, ((ResponseStatusException) e).getStatus());
        }
        return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> createJsonResponse(Exception e, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (e instanceof ResponseStatusException) {
            return new ResponseEntity<>(createExceptionBody(((ResponseStatusException) e).getStatus(), ((ResponseStatusException) e).getReason(), request.getDescription(false)), headers, ((ResponseStatusException) e).getStatus());
        }
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(createExceptionBody(status, "An unexpected error occurred", request.getDescription(false)), headers, status);
    }

    private Map<String, Object> createExceptionBody(HttpStatus status, String message, String path) {
        Map<String, Object> exceptionBody = new LinkedHashMap<>();
        exceptionBody.put("timestamp", LocalDateTime.now());
        exceptionBody.put("status", status.value());
        exceptionBody.put("error", status.getReasonPhrase());
        exceptionBody.put("message", message);
        exceptionBody.put("path", path);
        return exceptionBody;
    }
}
