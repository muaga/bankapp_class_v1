package com.tenco.bankapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bankapp.dto.SaveFormDto;
import com.tenco.bankapp.dto.WithdrawFormDto;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.Account;
import com.tenco.bankapp.repository.interfaces.AccountRepository;

@Service // IoC 대상 + 싱글톤 관리
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	/*
	 * 계좌 생성 기능
	 * @param dto
	 * @param principalId
	 * */
	
	@Transactional
	public void createAccount(SaveFormDto dto, Integer principalId) {
		
		// 계좌 중복 여부 확인
		
		Account account = Account.builder()
				.number(dto.getNumber())
				.password(dto.getPassword())
				.balance(dto.getBalance())
				.userId(principalId)
				.build();
		
		int resultRowCount =  accountRepository.insert(account);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("계좌 생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 계좌 목록 보기 기능
	public List<Account> readAccountList(Integer userId){
		List<Account> list = accountRepository.findByUserId(userId);
		return list;
	}

	// 출금 기능 로직 고민해보기
	// 1. 계좌 존재 여부 조회 -> select
	// 2. 본인 계좌 여부 확인 -> select
	// 3. 본인 계좌 비밀번호
	// 4. 본인 계좌 잔액 확인
	// 5. 출금 처리 -> update
	// 6. 거래 내역 등록(history) -> insert
	// 7. 트랜잭션 처리
	public void updateAccountWithdraw(WithdrawFormDto dto, Integer id) {
	}
}
