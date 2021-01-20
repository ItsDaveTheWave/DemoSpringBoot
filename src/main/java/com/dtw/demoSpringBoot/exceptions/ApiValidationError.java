package com.dtw.demoSpringBoot.exceptions;
import org.springframework.validation.FieldError;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
	
   private String object;
   private String field;
   private Object rejectedValue;
   private String message;

   public ApiValidationError(String object, String message) {
       this.object = object;
       this.message = message;
   }
   
   public ApiValidationError(Class<?> clazz, FieldError fieldError) {
	   String objectType = clazz.getSimpleName();
	   if(objectType.toLowerCase().endsWith("dto")) {
		   objectType = objectType.substring(0, objectType.length() - 3);
	   }
	   
	   object = objectType;
	   field = fieldError.getField();
	   rejectedValue = fieldError.getRejectedValue();
	   message = fieldError.getDefaultMessage();
   }
}