package com.tenco.bankapp.handler.exception;

import org.springframework.http.HttpStatus;

// 인증 Exception 예외처리 - 사용자 처리 예외 클래스
public class UnAuthrizedException extends RuntimeException {

	private HttpStatus status;

	public UnAuthrizedException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
}
