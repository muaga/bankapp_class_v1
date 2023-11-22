package com.tenco.bankapp.repository.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class History {
	
	private Integer id;
	private Long amount;
	// Long amount = 10(left value);
	// left value를 리터럴이라고 한다.
	// 10이라고 하면 int로 인식해서 10L이라고 해야 한다.
	private Long wBalance;
	private Long dBalance;
	private Integer wAccountId;
	private Integer dAccountId;
	private Timestamp createdAt;
}
