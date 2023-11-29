package com.tenco.bankapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tenco.bankapp.handler.AuthIntercepter;


// @Component IoC 등록이 되지만, 2개 이상의 IoC 등록 처리를 할 때 @Component 보다는 @Configuration을 사용한다.

// @Configuration IoC 등록 + 스프링 부트 설정 클래스라는 의미
@Component
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
	
	// 리소스 등록 처리
	// 서버 컴퓨터에 위치한 Resource를 활용하는 방법(프로젝트 외부 폴더 접근)
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/uploads/**")
		.addResourceLocations("file:///C:\\spring_upload\\bank\\upload/");
	};
	
	@Bean // Ioc 등록
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	// PasswordEncoder는 인터페이스로, 구현클래스는 위의 코드이다.
}
