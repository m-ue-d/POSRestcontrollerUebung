package dev.fabianmild.posrestcontrolleruebung.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(RuntimeException e) {
        System.out.printf("An %s error occurred: %s", e.getClass().getSimpleName(), e.getMessage());
    }
}
