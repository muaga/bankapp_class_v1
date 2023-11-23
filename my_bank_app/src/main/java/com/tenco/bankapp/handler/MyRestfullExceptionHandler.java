package com.tenco.bankapp.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tenco.bankapp.handler.exception.CustomRestfullException;

/*
 * 예외 발생시 데이터를 내려줄 수 있다.
 * */

// 데이터 반환 시 예외처리 핸들러 만들기
@RestControllerAdvice // IOC 대상 + AOP 기반 관점 지향형

public class MyRestfullExceptionHandler {
	
	// Exception.class를 타면 핸들러가 동작
	@ExceptionHandler(Exception.class)
	public void exception(Exception e) {
		System.out.println("-----------------");
		System.out.println(e.getClass().getName());
		System.out.println(e.getMessage());
		System.out.println("-----------------");

	}
	
	// 사용자 정의 예외 클래스 활용
	// try-catch 구문에서 new를 이용해 CustomRestfullException.class를 타게하면,
	// 위의 exception 메소드가 실행된다.
	@ExceptionHandler(CustomRestfullException.class)
	public String basicException(CustomRestfullException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		sb.append("alert(" + e.getMessage() + ");");
		sb.append("</script>");
		
		return sb.toString();
	}
}
