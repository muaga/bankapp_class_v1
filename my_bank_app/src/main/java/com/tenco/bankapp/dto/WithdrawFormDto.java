package com.tenco.bankapp.dto;

import lombok.Data;

@Data
public class WithdrawFormDto {
	
	private Long amount;
	private String wAccountNumber;
	private String password;
}

// 기본 파싱 전략에서 key=value로 넘어오는 값이 있다면, 꼭 getter/setter가 있어야 한다. = @Data