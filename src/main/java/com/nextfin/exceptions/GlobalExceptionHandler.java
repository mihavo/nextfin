package com.nextfin.exceptions;

import com.nextfin.exceptions.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIErrorResponse> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(APIErrorResponse.builder()
                                                    .status(HttpStatus.BAD_REQUEST)
                                                    .message(e.getMessage())
                                                    .timestamp(ZonedDateTime.now())
                                                    .build(),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(APIErrorResponse.builder()
                                                    .status(HttpStatus.NOT_FOUND)
                                                    .message(e.getMessage())
                                                    .timestamp(ZonedDateTime.now())
                                                    .build(),
                                    HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionTimeoutException.class)
    public ResponseEntity<APIErrorResponse> handleTransactionTimeoutException(TransactionTimeoutException e) {
        return new ResponseEntity<>(APIErrorResponse.builder()
                                                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                                                    .message(e.getMessage())
                                                    .timestamp(ZonedDateTime.now())
                                                    .build(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(APIErrorResponse.builder()
                                                    .status(HttpStatus.BAD_REQUEST)
                                                    .message(e.getMessage())
                                                    .timestamp(ZonedDateTime.now())
                                                    .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<APIErrorResponse> handleForbiddenException(ForbiddenException e) {
        return new ResponseEntity<>(APIErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(e.getMessage())
                .timestamp(ZonedDateTime.now())
                .build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Disabled2FAException.class)
    public ResponseEntity<APIErrorResponse> handleDisabled2FAException(Disabled2FAException e) {
        log.error("Attempted access of 2FA services");
        return new ResponseEntity<>(
                APIErrorResponse.builder().
                                status(HttpStatus.BAD_REQUEST)
                                .message(e.getMessage())
                                .timestamp(ZonedDateTime.now())
                                .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HolderNotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleHolderNotFoundException(HolderNotFoundException e) {
        return new ResponseEntity<>(APIErrorResponse.builder().status(HttpStatus.BAD_REQUEST).message(e.getMessage())
                                                    .timestamp(ZonedDateTime.now()).build(), HttpStatus.BAD_REQUEST);
    }

    protected String printStackTrace(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
