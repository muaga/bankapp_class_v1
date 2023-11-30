package com.tenco.bankapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bankapp.dto.SignInFormDto;
import com.tenco.bankapp.dto.SignUpFormDto;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.repository.interfaces.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	// 의존주입(생성자, 메소드)
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Transactional
	public int signUp(SignUpFormDto dto) {
		
		// username 중복 여부 확인 v1
//		String rawPwd = dto.getPassword();
//		// 사용자가 넣은 패스워드를 암호화 처리
//		String hashPwd = passwordEncoder.encode(rawPwd);
//		System.out.println("hashPwd : " + hashPwd);
//		
//		User user = User.builder()
//				.username(dto.getUsername())
//				.password(hashPwd) // 코드 수정 -> 해시코드
//				.fullname(dto.getFullname())
//				.build();
		
		// username 중복 여부 확인 v2
		User user = User.builder()
				.username(dto.getUsername())
				.password(passwordEncoder.encode(dto.getPassword())) // 코드 수정 -> 해시코드
				.fullname(dto.getFullname())
				.originFileName(dto.getOriginFileName())
				.uploadFileName(dto.getUploadFileName())
				.build();
		
		int resultRowCount = userRepository.insert(user);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return resultRowCount;
	}
	
	
	public User signIn(SignInFormDto dto) {
		// findByUsernameAndPassword 쿼리를 사용하면, 사용자가 입력하는 비밀번호는 '1234'이므로
		// 사용자의 이름만으로 먼저 userEntity를 찾아온다.
		// User userEntity = userRepository.findByUsernameAndPassword(dto);
		
		
		// 1. username 아이디 존재 여부 확인
		User userEntity = userRepository.findByUsername(dto.getUsername());
		if(userEntity == null) {
			throw new CustomRestfullException("존재하지 않는 계정입니다", HttpStatus.BAD_REQUEST);
		}
		
		// 2. 사용자가 입력한 비밀번호와 Entity의 비밀번호 비교 
		boolean isPwdMatched = passwordEncoder.matches(dto.getPassword(), userEntity.getPassword());
		if(isPwdMatched == false) {
			throw new CustomRestfullException("비밀번호가 잘못 되었습니다", HttpStatus.BAD_REQUEST);
		}
		
		return userEntity;
	}


	public User searchUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
}
