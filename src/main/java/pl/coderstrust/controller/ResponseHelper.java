package pl.coderstrust.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {

    public static ResponseEntity<?> createPdfOkResponse(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Passed byte array is null");
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(array, responseHeaders, HttpStatus.OK);
    }

    public static ResponseEntity<?> createJsonOkResponse(Object body) {
        if (body == null) {
            throw new IllegalArgumentException("Response body cannot be null");
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
    }

    public static ResponseEntity<?> createJsonCreatedResponse(Object body, String location) {
        if (body == null) {
            throw new IllegalArgumentException("Response body cannot be null");
        }
        if (location.contains(" ")) {
            throw new IllegalArgumentException("Passed location cannot contain whitespaces");
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        responseHeaders.setLocation(URI.create(location));
        return new ResponseEntity<>(body, responseHeaders, HttpStatus.CREATED);
    }

    public static boolean isPdfResponse(HttpHeaders httpHeaders) {
        Optional<MediaType> type = httpHeaders.getAccept().stream()
            .filter(x -> x.isCompatibleWith(MediaType.APPLICATION_PDF) || x.isCompatibleWith(MediaType.APPLICATION_JSON))
            .findFirst();
        if (type.isEmpty()) {
            return false;
        }
        return !type.get().isWildcardType() && type.get().isCompatibleWith(MediaType.APPLICATION_PDF);
    }
}
