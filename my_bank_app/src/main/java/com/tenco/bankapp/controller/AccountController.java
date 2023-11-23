package com.tenco.bankapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bankapp.handler.exception.CustomPageException;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	// 임시 예외 발생 확인
	// http://localhost:80/account/list
	@GetMapping("/list")
	public void list() {
		throw new CustomPageException("페이지가 없어요", HttpStatus.NOT_FOUND);
	}
}
