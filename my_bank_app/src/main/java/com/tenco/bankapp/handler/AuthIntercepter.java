package com.tenco.bankapp.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bankapp.handler.exception.UnAuthrizedException;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.utils.Define;

// 만드는 방법
// 1. HandlerIntercpetor 구현

@Component // IoC 대상 - 싱글톤 관리
public class AuthIntercepter implements HandlerInterceptor {
	

	// Controller에 들어오기 전에 동작
	// Controller ---> true(진입) / false(X)
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 세션에 사용자 정보 확인
		HttpSession session = request.getSession();
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			// response.sendRedirect("/user/sign-in");
			throw new UnAuthrizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED); // 지정해 놓은 exception 쓰기
		}
		return true;
	}
	
	// view가 렌더링 되기 전에 호출되는 메소드 - JSP 파일 찾기 전
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	// 요청 처리가 완료된 후, 뷰 렌더링이 완료된 후에 호출되는 메소드
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
