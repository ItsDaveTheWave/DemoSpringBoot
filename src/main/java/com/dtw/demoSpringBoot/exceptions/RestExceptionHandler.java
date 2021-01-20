package com.dtw.demoSpringBoot.exceptions;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ApiError> handleException(Exception ex) {
		ex.printStackTrace();
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error", ex);
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ApiError> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed on this path", ex);
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	
	// Produced by @Valid
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage("Validation error");
		for(FieldError error : ex.getFieldErrors()) {
			apiError.addSubError(new ApiValidationError(ex.getTarget().getClass(), error));
		}
		return buildResponseEntity(apiError);
	}
	
	// Produces when failing to parse a Json token to a specific type, e.g. converting this({"a": null}) to string
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "JSON parse error", ex);
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		String message = "Type conversion error.";
		if(ex.getValue() != null && ex.getRequiredType() != null) {
			message = message + " The value {" + ex.getValue() + 
					"} can not be converted to the type {" + ex.getRequiredType().getSimpleName() + "}.";
		}
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, ex);
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(InvalidFormatException.class)
	protected ResponseEntity<ApiError> handleInvalidFormatException(InvalidFormatException ex) {
		String message = "Invalid format error.";
		if(ex.getValue() != null && ex.getTargetType() != null) {
			message = message + " The value {" + ex.getValue() + 
					"} can not be converted to the type {" + ex.getTargetType().getSimpleName() + "}.";
		}
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, ex);
		return buildResponseEntity(apiError);
	}		
	
	private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}