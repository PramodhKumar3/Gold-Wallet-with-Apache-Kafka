package com.cg.gold.utility;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cg.gold.exception.AddressException;
import com.cg.gold.exception.PaymentException;
import com.cg.gold.exception.PhysicalGoldTransactionException;
import com.cg.gold.exception.TransactionHistoryException;
import com.cg.gold.exception.UserException;
import com.cg.gold.exception.VendorBranchException;
import com.cg.gold.exception.VendorException;
import com.cg.gold.exception.VirtualGoldHoldingException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private Environment environment;

	@ExceptionHandler(AddressException.class)
	public ResponseEntity<ErrorInfo> handleAddressException(AddressException ex) {
		ErrorInfo errorInfo = new ErrorInfo(environment.getProperty(ex.getMessage()), HttpStatus.NOT_FOUND.value(),
				LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ErrorInfo> handlePaymentException(PaymentException ex) {
		ErrorInfo errorInfo = new ErrorInfo(environment.getProperty(ex.getMessage()), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PhysicalGoldTransactionException.class)
	public ResponseEntity<ErrorInfo> handleTransactionException(PhysicalGoldTransactionException ex) {
		ErrorInfo errorInfo = new ErrorInfo(environment.getProperty(ex.getMessage()), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(TransactionHistoryException.class)
	public ResponseEntity<ErrorInfo> handleTransactionHistoryException(TransactionHistoryException ex) {
		ErrorInfo errorInfo = new ErrorInfo(environment.getProperty(ex.getMessage()), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorInfo> handleUserException(UserException ex) {
		ErrorInfo errorInfo = new ErrorInfo(environment.getProperty(ex.getMessage()), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(VendorBranchException.class)
	public ResponseEntity<ErrorInfo> handleVendorBranchException(VendorBranchException ex) {
		ErrorInfo errorInfo = new ErrorInfo(environment.getProperty(ex.getMessage()), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(VendorException.class)
	public ResponseEntity<ErrorInfo> handleVendorException(VendorException ex) {
		ErrorInfo errorInfo = new ErrorInfo(environment.getProperty(ex.getMessage()), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(VirtualGoldHoldingException.class)
	public ResponseEntity<ErrorInfo> handleVirtualGoldHoldingException(VirtualGoldHoldingException ex) {
		ErrorInfo errorInfo = new ErrorInfo(environment.getProperty(ex.getMessage()), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class, ConstraintViolationException.class })
	public ResponseEntity<ErrorInfo> exceptionHandler(Exception exception) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());

		String errorMsg = "";
		if (exception instanceof MethodArgumentNotValidException ex) {
			errorMsg = ex.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.joining(", "));
		} else if (exception instanceof ConstraintViolationException ex) {
			errorMsg = ex.getConstraintViolations().stream().map(violation -> violation.getMessage())
					.collect(Collectors.joining(", "));
		}
		errorInfo.setErrorMessage(errorMsg);
		errorInfo.setTimeStamp(LocalDateTime.now());

		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> handleGenericException(Exception ex) {
		ErrorInfo errorInfo = new ErrorInfo(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
