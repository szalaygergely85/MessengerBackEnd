package com.gege.ideas.messenger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

   public ResourceAlreadyExistsException() {
      super("Resource already exists");
   }

   public ResourceAlreadyExistsException(String message) {
      super(message);
   }

   public ResourceAlreadyExistsException(String message, Throwable cause) {
      super(message, cause);
   }
}
