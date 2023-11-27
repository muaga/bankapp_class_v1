package com.tenco.bankapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bankapp.dto.DepositFormDto;
import com.tenco.bankapp.dto.SaveFormDto;
import com.tenco.bankapp.dto.TransferFormDto;
import com.tenco.bankapp.dto.WithdrawFormDto;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.Account;
import com.tenco.bankapp.repository.entity.History;
import com.tenco.bankapp.repository.interfaces.AccountRepository;
import com.tenco.bankapp.repository.interfaces.HistoryRepository;

@Service // IoC 대상 + 싱글톤 관리
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	// DIP 원칙, 추상클래스, 나중에 변경할 때 구현클래스만 변경하면 되므로 추상 클래스는 추상화한다.

	@Autowired
	private HistoryRepository historyRepository;

	/*
	 * 계좌 생성 기능
	 * 
	 * @param dto
	 * 
	 * @param principalId
	 */

	@Transactional
	public void createAccount(SaveFormDto dto, Integer principalId) {

		// 계좌 중복 여부 확인

		Account account = Account.builder().number(dto.getNumber()).password(dto.getPassword())
				.balance(dto.getBalance()).userId(principalId).build();

		int resultRowCount = accountRepository.insert(account);
		if (resultRowCount != 1) {
			throw new CustomRestfullException("계좌 생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 계좌 목록 보기 기능
	public List<Account> readAccountList(Integer userId) {
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
	@Transactional
	public void updateAccountWithdraw(WithdrawFormDto dto, Integer principalId) {

		// 1. 계좌 존재 여부 조회 -> select
		Account accountEntity = accountRepository.findByNumber(dto.getWAccountNumber());

		if (accountEntity == null) {
			throw new CustomRestfullException("해당 계좌가 없습니다", HttpStatus.BAD_REQUEST);
		}

		// 2. 본인 계좌 여부 확인 -> select
		if (accountEntity.getUserId() != principalId) {
			throw new CustomRestfullException("본인 소유 계좌가 아닙니다", HttpStatus.UNAUTHORIZED);
		}

		// 3. 본인 계좌 비밀번호
		if (accountEntity.getPassword().equals(dto.getPassword()) == false) {
			throw new CustomRestfullException("출금 계좌 비밀번호가 틀렸습니다", HttpStatus.BAD_REQUEST);
		}

		// 4. 본인 계좌 잔액 확인
		if (accountEntity.getBalance() < dto.getAmount()) {
			throw new CustomRestfullException("계좌 잔액이 부족합니다", HttpStatus.BAD_REQUEST);
		}

		// 5. 출금 처리 -> update
		// 객체 모델 상태값 변경 처리
		accountEntity.withdraw(dto.getAmount());
		accountRepository.updateById(accountEntity);

		// 6. 거래 내역 등록(history) -> insert
		History history = new History();
		history.setAmount(dto.getAmount());
		// 출금 거래 내역에서는 사용자가 출금 후에 잔액을 입력한다.
		// 그렇다면 setWBalance에는 출금한 상태인 account의 변경된 상태를 넣어준다.(accountEntity.withdraw)
		history.setWBalance(accountEntity.getBalance());
		// 출금 거래 내역이므로 입금은 존재하지 않으니 null이다.
		history.setDBalance(null);
		history.setWAccountId(accountEntity.getId());
		history.setDAccountId(null);
		// ===========> 출금 내역
		int resultRowCount = historyRepository.insert(history);
		if (resultRowCount != 1) {
			throw new CustomRestfullException("정상 처리가 되지 않았습니다", HttpStatus.BAD_REQUEST);
		}
	}

	// 입금 기능 로직 고민해보기
	// 1. 계좌 존재 여부 조회 -> select
	// 2. 본인 계좌 여부 확인 -> select
	// 3. 입금 처리 -> update
	// 4. 거래 내역 등록(history) -> insert
	// 5. 트랜잭션 처리
	@Transactional
	public void updateAccountDespoit(DepositFormDto dto, Integer principalId) {

		// 1. 계좌 존재 여부 조회 -> select
		Account accountEntity = accountRepository.findByNumber(dto.getDAccountNumber());
		if(accountEntity == null) {
			throw new CustomRestfullException("해당 계좌가 없습니다", HttpStatus.BAD_REQUEST);
		}
		
		// 2. 본인 계좌 여부 확인 -> select
		if(accountEntity.getUserId() != principalId) {
			throw new CustomRestfullException("본인 소유 계좌가 아닙니다", HttpStatus.UNAUTHORIZED);
		}
		
		// 3. 입금 처리 -> update
		accountEntity.deposit(dto.getAmount());
		accountRepository.updateById(accountEntity);
		
		// 4. 거래 내역 등록(history) -> insert
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWBalance(null);
		history.setDBalance(accountEntity.getBalance());
		history.setWAccountId(null);
		history.setDAccountId(accountEntity.getId());
		// ==========> 입금 내역
		int resultRowCount = historyRepository.insert(history);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("정상 처리가 되지 않았습니다", HttpStatus.BAD_REQUEST);
		}
	}
	
	// 이체 기능 로직 고민해보기
	// 1. 계좌 존재 여부 조회 -> 출금계좌, 입금계좌 select
	// 2. 본인 계좌 여부 확인 -> select
	// 3. 본인 계좌 비밀번호
	// 4. 본인 계좌 잔액 확인
	// 5. 이체 처리 -> 출금계좌, 입금계좌
	// 6. 거래 내역 등록(history) -> insert
	// 7. 트랜잭션 처리
	@Transactional
	public void updateAccountTransfer(TransferFormDto dto, Integer principalId) {
		
		// 1. 계좌 존재 여부 조회 -> 출금계좌, 이체계좌 select
		Account wAccountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
		if(wAccountEntity == null) {
			throw new CustomRestfullException("출금 계좌가 없습니다", HttpStatus.BAD_REQUEST);
		}
		Account dAccountEntity = accountRepository.findByNumber(dto.getDAccountNumber());
		if(dAccountEntity == null) {
			throw new CustomRestfullException("입금 계좌가 없습니다", HttpStatus.BAD_REQUEST);
		}
		
		// 2. 본인 계좌 여부 확인 -> select
		wAccountEntity.checkOwner(principalId);
//		if(wAccountEntity.getId() != principalId) {
//			throw new CustomRestfullException("본인 소유 계좌가 아닙니다", HttpStatus.UNAUTHORIZED);
//		}
//		
		// 3. 본인 계좌 비밀번호
		wAccountEntity.checkPassword(dto.getPassword());
//		if(wAccountEntity.getPassword().equals(dto.getPassword()) == false) {
//			throw new CustomRestfullException("출금 계좌 비밀번호가 틀렸습니다", HttpStatus.BAD_REQUEST);
//		}
		
		// 4. 본인 계좌 잔액 확인
		wAccountEntity.checkBalance(dto.getAmount());
//		if(wAccountEntity.getBalance() < dto.getAmount()) {
//			throw new CustomRestfullException("계좌 잔액이 부족합니다", HttpStatus.BAD_REQUEST);
//		}
		
		// 5. 이체 처리 -> 출금계좌, 이체계좌
		wAccountEntity.withdraw(dto.getAmount());
		accountRepository.updateById(wAccountEntity);
		dAccountEntity.deposit(dto.getAmount());
		accountRepository.updateById(dAccountEntity);
		
		// 6. 거래 내역 등록(history) -> insert
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWAccountId(wAccountEntity.getId());
		history.setDAccountId(dAccountEntity.getId());
		history.setWBalance(wAccountEntity.getBalance());
		history.setDBalance(dAccountEntity.getBalance());
		int resultRowCount = historyRepository.insert(history);
		// ==========> 이체 내역
		if(resultRowCount != 1) {
			throw new CustomRestfullException("정상 처리가 되지 않았습니다", HttpStatus.BAD_REQUEST);
		}
	}
}
