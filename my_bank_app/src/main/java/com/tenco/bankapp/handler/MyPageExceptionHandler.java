package com.tenco.bankapp.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bankapp.handler.exception.CustomPageException;

/*
 * View 렌더링을 위해 ModelView 객체를 반환하도록 설정되어 있음
 * 예외 처리 Page return 할 때 사용
 * */

@ControllerAdvice
public class MyPageExceptionHandler {

	@ExceptionHandler(CustomPageException.class)
	public ModelAndView handleRuntimeException(CustomPageException e) {
		//ModelAndView 활용방법
		ModelAndView modelAndView = new ModelAndView("errorPage");
		modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value());
		modelAndView.addObject("message", e.getMessage());
		return modelAndView;
	}
}
