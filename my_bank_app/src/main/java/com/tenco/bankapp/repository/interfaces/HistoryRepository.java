package com.tenco.bankapp.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tenco.bankapp.repository.entity.History;

public interface HistoryRepository {

	// 거래 내역 등록
	public int insert(History history);
	
	// 거래 내역 조회
	public List<History> findByAccountNumber(String accountNumber);
	
	// 동적 쿼리 생성
//	public List<History> findByIdAndDynamicType(@Param("type") String type, @Param("id") Integer id);
	// history에는 입금 / 출금 / 전체 내역이 존재
	// 하나의 쿼리로 입금 / 출금 / 전체 내역을 각 조건에 맞게 출력하기 위해
	// key 값을 설정한다. = @Param(동적 매개변수)
	// 특히 두 개 이상의 파라미터 사용 시 @Param을 사용해야 한다. 그리고 key 값인 ""를 설정해야 한다.
}
