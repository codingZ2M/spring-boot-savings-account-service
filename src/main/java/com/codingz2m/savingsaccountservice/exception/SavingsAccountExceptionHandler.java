package com.codingz2m.savingsaccountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SavingsAccountExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(SavingsAccountNotFoundException.class)
	public ResponseEntity<ErrorMessage> savingsAccountNotFound(SavingsAccountNotFoundException exception,
			WebRequest request) {
		
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,
										exception.getMessage() ); 
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
	

	
	
}
