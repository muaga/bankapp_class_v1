package com.tenco.bankapp.handler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomRestfullException extends RuntimeException{
	
	private HttpStatus httpStatus;
	
	public CustomRestfullException(String message, HttpStatus status) {
		super(message); // 부모 생성자 호출, 넘겨받은 message를 넘기기
		this.httpStatus = status;
	}
}
