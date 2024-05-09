package com.michaelvol.bankingapp.exceptions;

import com.michaelvol.bankingapp.exceptions.exception.APIErrorResponse;
import com.michaelvol.bankingapp.exceptions.exception.BadRequestException;
import com.michaelvol.bankingapp.exceptions.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIErrorResponse> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(APIErrorResponse.builder()
                                                    .status(HttpStatus.BAD_REQUEST)
                                                    .message(e.getMessage())
                                                    .timestamp(ZonedDateTime.now())
                                                    .trace(printStackTrace(e))
                                                    .build(),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(APIErrorResponse.builder()
                                                    .status(HttpStatus.NOT_FOUND)
                                                    .message(e.getMessage())
                                                    .timestamp(ZonedDateTime.now())
                                                    .trace(printStackTrace(e))
                                                    .build(),
                                    HttpStatus.NOT_FOUND);
    }

    protected String printStackTrace(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
