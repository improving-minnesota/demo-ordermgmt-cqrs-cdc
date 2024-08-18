package com.snacks.ordercommands.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrderAlreadyExistsException.class)
    ResponseEntity<Object> handleOrderAlreadyExistsException(OrderAlreadyExistsException e) {
        ApiError apiError = new ApiError(e.getMessage());

        e.printStackTrace();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderDoesNotExistException.class)
    ResponseEntity<Object> handleOrderDoesNotExistException(OrderDoesNotExistException e) {
        ApiError apiError = new ApiError(e.getMessage());

        e.printStackTrace();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderPaymentAmountException.class)
    ResponseEntity<Object> handleOrderPaymentAmountException(OrderPaymentAmountException e) {
        ApiError apiError = new ApiError(e.getMessage());

        e.printStackTrace();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderFullfillmentException.class)
    ResponseEntity<Object> handleOrderFulfillmentException(OrderFullfillmentException e) {
        ApiError apiError = new ApiError(e.getMessage());

        e.printStackTrace();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderShippingException.class)
    ResponseEntity<Object> handleOrderShippingException(OrderShippingException e) {
        ApiError apiError = new ApiError(e.getMessage());

        e.printStackTrace();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
