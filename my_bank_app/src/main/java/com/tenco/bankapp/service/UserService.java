package com.tenco.bankapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bankapp.dto.SignUpFormDto;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.repository.interfaces.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	// 의존주입(생성자, 메소드)
	
	@Transactional
	public int signUp(SignUpFormDto dto) {

		// username 중복 여부 확인
		
		User user = User.builder()
				.username(dto.getUsername())
				.password(dto.getPassword())
				.fullname(dto.getFullname())
				.build();
		
		int resultRowCount = userRepository.insert(user);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return resultRowCount;
	}
	
}
