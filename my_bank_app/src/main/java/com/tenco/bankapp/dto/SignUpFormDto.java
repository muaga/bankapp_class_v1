package com.tenco.bankapp.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpFormDto {
	
	private String username;
	private String password;
	private String fullname;
	// 바이너리 파일 데이터 받기
	private MultipartFile file; // name 속성과 일치해야 함
	// 파일이 등록될 때 중복되는 이름이 있다면, 덮어쓰기 해야 한다.
	// 그래서 기존의 파일명과 새로 등록되는 파일명을 관리해야 한다.
	 private String originFileName;
	 private String uploadFileName;
}
