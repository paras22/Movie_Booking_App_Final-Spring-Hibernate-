package com.upgrad.mtb.aspects;

import com.upgrad.mtb.exceptions.TheatreDetailsNotFoundException;
import com.upgrad.mtb.responses.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TheatreExceptionAspect {

    @ExceptionHandler(TheatreDetailsNotFoundException.class)
    public ResponseEntity<CustomResponse> handleTheatreDetailsNotFoundException(Exception e){
        CustomResponse response = new CustomResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
