package com.tenco.bankapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tenco.bankapp.handler.AuthIntercepter;


// @Component IoC 등록이 되지만, 2개 이상의 IoC 등록 처리를 할 때 @Component 보다는 @Configuration을 사용한다.

@Configuration // IoC 등록 + 스프링 부트 설정 클래스라는 의미
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private AuthIntercepter authIntercepter;
	
	// 기존 작동되는 Interceptor 외에 사용하고 싶은 Interceptor가 있는 경우
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(authIntercepter)
				.addPathPatterns("/account/**")
				.addPathPatterns("/auth/**");
				// addPathPatterns : 인터셉터가 실행될 엔드포인트 지정
	}
}
