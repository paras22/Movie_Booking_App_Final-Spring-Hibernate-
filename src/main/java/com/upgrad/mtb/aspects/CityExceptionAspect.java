package com.upgrad.mtb.aspects;

import com.upgrad.mtb.exceptions.CityDetailsNotFoundException;
import com.upgrad.mtb.responses.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CityExceptionAspect {
    @ExceptionHandler(CityDetailsNotFoundException.class)
    public ResponseEntity<CustomResponse> handleCityDetailsNotFoundException(Exception e){
        CustomResponse response = new CustomResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
