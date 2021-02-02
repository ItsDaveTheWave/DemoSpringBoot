package com.dtw.demoSpringBoot.exceptions;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

	// Generic error
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ApiError> handleException(Exception ex) {
		ex.printStackTrace();
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error", ex);
		return buildResponseEntity(apiError);
	}

	// Invalid path/method error
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ApiError> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed on this path", ex);
		return buildResponseEntity(apiError);
	}

	// Entity not found error
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	// Validation error by @Valid
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage("Validation error");
		for (FieldError error : ex.getFieldErrors()) {
			apiError.addSubError(new ApiValidationError(ex.getTarget().getClass(), error));
		}
		return buildResponseEntity(apiError);
	}
	
	// Can be thrown when commiting changes to DB, can contain ConstraintViolationException(if error is because of validation)
	@ExceptionHandler(TransactionSystemException.class)
	protected ResponseEntity<ApiError> handleTransactionSystem(TransactionSystemException ex) {
		if(ex.getRootCause() instanceof ConstraintViolationException) {
			return handleConstraintViolation((ConstraintViolationException) ex.getRootCause());
		}
		else {
			return handleException(ex);
		}
	}
	
	// Validation error by Validator	
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage("Validation error");
		for(ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			apiError.addSubError(new ApiValidationError(violation));
		}
		return buildResponseEntity(apiError);
	}

	// Json parse error
	@ExceptionHandler({ HttpMessageNotReadableException.class, JsonProcessingException.class })
	protected ResponseEntity<ApiError> handleHttpMessageNotReadable_JsonProcessing(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "JSON processing/parse error", ex);
		return buildResponseEntity(apiError);
	}

	// Type conversion error
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
		String message = "Type conversion error.";
		Object value = ex.getValue();
		Class<?> requiredType = ex.getRequiredType();
		
		if (value != null && requiredType != null) {
			message = message + " The value {" + value + "} can not be converted to the type {"
					+ requiredType.getSimpleName() + "}.";
		}
		
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, ex);
		return buildResponseEntity(apiError);
	}

	// JsonPatch error
	@ExceptionHandler(JsonPatchException.class)
	protected ResponseEntity<ApiError> handleJsonPatch(JsonPatchException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "JSONPatch error.", ex);
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}