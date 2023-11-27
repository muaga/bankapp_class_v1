package com.tenco.bankapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bankapp.dto.DepositFormDto;
import com.tenco.bankapp.dto.SaveFormDto;
import com.tenco.bankapp.dto.TransferFormDto;
import com.tenco.bankapp.dto.WithdrawFormDto;
import com.tenco.bankapp.handler.exception.CustomPageException;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.handler.exception.UnAuthrizedException;
import com.tenco.bankapp.repository.entity.Account;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.service.AccountService;
import com.tenco.bankapp.utils.Define;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private AccountService accountService;
	
	// 계좌 목록 페이지
	// http://localhost:80/account/list
	@GetMapping({"/list", "/"})
	public String list(Model model) {
	
		// 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthrizedException("인증된 사용자가 아닙니다", HttpStatus.UNAUTHORIZED);
		}
		
		List<Account> accountList = accountService.readAccountList(principal.getId());
		if(accountList.isEmpty()) {			
			model.addAttribute("accountList", null);
		}else {			
			model.addAttribute("accountList", accountList);
		}
		
		return "account/list";
	}
	
	// 계좌 생성 페이지
	// http://localhost:80/account/save
	@GetMapping("/save")
	public String save() {
		
		// 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthrizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		
		
		return "account/save";
	}
	
	
	@PostMapping("/save")
	public String saveProc(SaveFormDto dto) {
		
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthrizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		
		// 2. 유효성 검사
		if(dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("계좌 비밀번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		if(dto.getBalance() == null || dto.getBalance() <= 0) {
			throw new CustomRestfullException("잘못된 입력 입니다", HttpStatus.BAD_REQUEST);
		}
		
		// 3. 핵심 로직
		accountService.createAccount(dto, principal.getId());
		
		return "redirect:/account/list";
	}
	
	
	// 출금 페이지
	// http://localhost:80/account/withdraw
	@GetMapping("/withdraw")
	public String withdraw() {
		
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthrizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		
		return "account/withdraw";
	}
	
	@PostMapping("/withdraw")
	public String withdrawProc(WithdrawFormDto dto) {
		
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthrizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		
		// 2. 유효성 검사
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getAmount().longValue() <= 0) {
			throw new CustomRestfullException("출금 금액이 0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getWAccountNumber() == null || dto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 비밀 번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		
		// 3. 핵심 로직
		accountService.updateAccountWithdraw(dto, principal.getId());
		
		return "redirect:/account/list";
	}
	
	// 입금 페이지
	// http://localhost:80/account/deposit
	@GetMapping("/deposit")
	public String deposit() {
		
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthrizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		
		return "account/deposit";
	}
	
	@PostMapping("/deposit")
	public String depositProc(DepositFormDto dto) {
		
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthrizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		
		// 2. 유효성 검사
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getAmount().longValue() <= 0) {
			throw new CustomRestfullException("입금 금액이 0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("입금 계좌 번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		
		// 3. 핵심 로직
		accountService.updateAccountDespoit(dto, principal.getId());
		
		return "redirect:/account/list";
	}
	
	// 이체 페이지
	// http://localhost:80/account/transfer
	@GetMapping("/transfer")
	public String transfer() {
		
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthrizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		
		return "account/transfer";
	}
	
	@PostMapping("/transfer")
	public String transferProc(TransferFormDto dto) {
		
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthrizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		
		// 2. 유효성 검사
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getAmount().longValue() <= 0) {
			throw new CustomRestfullException("이체 금액이 0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getWAccountNumber() == null || dto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 비밀 번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("입금 계좌 번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		
		// 3. 핵심 로직
		accountService.updateAccountTransfer(dto, principal.getId());
		
		return "redirect:/account/list";
	}
}
