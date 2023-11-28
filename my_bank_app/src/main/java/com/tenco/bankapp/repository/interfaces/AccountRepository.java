package com.tenco.bankapp.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bankapp.repository.entity.Account;

@Mapper
public interface AccountRepository {

	public int insert(Account account);

	public int updateById(Account account);

	public int deleteById(Integer id);

	// 계좌 1개 조회
	public Account findById(Integer id);

	// 계좌 전체 조회
	public List<Account> findAll();

	// User 정보로 계좌 리스트를 출력
	public List<Account> findByUserId(Integer principalId);

	// 계좌번호로 계좌 조회
	public Account findByNumber(String number);

}
