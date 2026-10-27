package com.smartinventory.controller;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
import java.time.Instant; import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("timestamp",Instant.now(),"status",400,"error","Validation failed","message",e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).toList()));}
 @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<?> bad(IllegalArgumentException e){return ResponseEntity.badRequest().body(Map.of("timestamp",Instant.now(),"status",400,"error","Bad Request","message",e.getMessage()==null?"Invalid request":e.getMessage()));}
 @ExceptionHandler(ResponseStatusException.class) ResponseEntity<?> status(ResponseStatusException e){return ResponseEntity.status(e.getStatusCode()).body(Map.of("timestamp",Instant.now(),"status",e.getStatusCode().value(),"error",e.getReason()==null?"Request failed":e.getReason()));}
 @ExceptionHandler(Exception.class) ResponseEntity<?> other(Exception e){return ResponseEntity.status(500).body(Map.of("timestamp",Instant.now(),"status",500,"error","Internal Server Error","message","An unexpected error occurred"));}
}
