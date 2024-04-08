package com.sonnt.blog.exception;

import com.sonnt.blog.payload.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //---------------------------------------------------------------------------------------

    //handle specific exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(BlogAPIException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, exception.getStatus());
    }

    //---------------------------------------------------------------------------------------

    // global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(Exception exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //---------------------------------------------------------------------------------------

    // NOT VALID exception
    //override it
    //First Approach
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        //get errors from MethodArgumentNotValidException
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        //
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //Second Approach
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleResourceNotFoundException(MethodArgumentNotValidException exception, WebRequest webRequest){
//        Map<String,String> errors= new HashMap<>();
//        //get errors from MethodArgumentNotValidException
//        exception.getBindingResult().getAllErrors().forEach((error)->{
//            String fieldName= ((FieldError)error).getField();
//            String message= error.getDefaultMessage();
//            errors.put(fieldName,message);
//        });
//        return  new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
    //---------------------------------------------------------------------------------------
}
