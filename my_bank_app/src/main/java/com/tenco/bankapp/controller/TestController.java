package com.tenco.bankapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // IOC에 대상
// 만약 엔드포인트에서 공통으로 들어가는 단어가 있을 때
@RequestMapping("/temp")
public class TestController {

	// 주소설계 - http://localhost:80/temp-test
	// Get
	@GetMapping("/temp-test")
	public String tempTest() {
		return "temp";
		// yml에서 prefix에서 경로를 지정해 놓았기 때문에 파일명만 붙이면 된다.
	}
	
	// 주소설계 - http://localhost:80/main-page
	// Get
	@GetMapping("/main-page")
	public String tempMainPage() {
		return "main";
	}
}
