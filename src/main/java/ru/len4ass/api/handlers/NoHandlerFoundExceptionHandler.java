package ru.len4ass.api.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.len4ass.api.models.error.ErrorResponse;

@RestControllerAdvice
public class NoHandlerFoundExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse requestHandlingNoHandlerFound() {
        return new ErrorResponse("No such page found.");
    }
}
