package com.gege.ideas.messenger.exception;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

   private static final Logger log = LoggerFactory.getLogger(
      GlobalExceptionHandler.class
   );

   @ExceptionHandler(UnauthorizedException.class)
   public ResponseEntity<Map<String, String>> handleUnauthorized(
      UnauthorizedException ex
   ) {
      return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body(Map.of("error", ex.getMessage()));
   }

   @ExceptionHandler(ResourceAlreadyExistsException.class)
   public ResponseEntity<Map<String, String>> handleConflict(
      ResourceAlreadyExistsException ex
   ) {
      return ResponseEntity
         .status(HttpStatus.CONFLICT)
         .body(Map.of("error", ex.getMessage()));
   }

   @ExceptionHandler(BadRequestException.class)
   public ResponseEntity<Map<String, String>> handleBadRequest(
      BadRequestException ex
   ) {
      return ResponseEntity
         .status(HttpStatus.BAD_REQUEST)
         .body(Map.of("error", ex.getMessage()));
   }

   @ExceptionHandler(NoResourceFoundException.class)
   public ResponseEntity<Map<String, String>> handleNoResource(
      NoResourceFoundException ex
   ) {
      // Routine 404 — client hit a path that doesn't exist; not worth an ERROR log.
      log.debug("No resource found: {}", ex.getMessage());
      return ResponseEntity
         .status(HttpStatus.NOT_FOUND)
         .body(Map.of("error", "Not found"));
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
      log.error("Unhandled exception", ex);
      return ResponseEntity
         .status(HttpStatus.INTERNAL_SERVER_ERROR)
         .body(Map.of("error", "An unexpected error occurred"));
   }
}
