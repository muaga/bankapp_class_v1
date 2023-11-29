package com.tenco.bankapp.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.tenco.bankapp.dto.SignInFormDto;
import com.tenco.bankapp.dto.SignUpFormDto;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.service.UserService;
import com.tenco.bankapp.utils.Define;

@Controller
@RequestMapping("/user")
public class UserController {

	// 어노테이션으로 DI 처리
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession session;

	// 생성자주입으로 DI 처리
//	public UserController(UserService userService) {
//		this.userService = userService;
//	}

	// 회원가입 페이지 요청
	// http://localhost:80/user/sign-up
	@GetMapping("/sign-up")
	public String signUp() {
		return "user/signUp";
	}

	// 로그인 페이지 요청
	// http://localhost:80/user/sign-in
	@GetMapping("/sign-in")
	public String signIn() {
		return "user/signIn";
	}

	/**
	 * 회원 가입 처리
	 * 
	 * @param dto
	 * @return 리다이렉트 로그인 페이지
	 */
	// DTO - Object Mapper가 동작할 수 있도록
	@PostMapping("/sign-up")
	// get요청과 다른 요청이기 때문에 엔드포인트가 같아도 된다.
	public String signUpProc(SignUpFormDto dto) {

		// 1. 유효성 검사
		if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfullException("username을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("password을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		if (dto.getFullname() == null || dto.getFullname().isEmpty()) {
			throw new CustomRestfullException("fullname을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
			
		// 2. 핵심 로직
		
		// 2-1 프로필 이미지 등록
		// - 사용자 프로필 이미지 등록 처리
		MultipartFile file = dto.getFile();
		if(file.isEmpty() == false) {
			// 파일 사이즈 체크
			if(file.getSize() > Define.MAX_FILE_SIZE) {
				throw new CustomRestfullException("파일 크기는 20MB 이상 클 수 없습니다", HttpStatus.BAD_REQUEST);
			}
		}
		
		// - 저장 경로 세팅
		try {
			// 폴더가 있는 경우 - 저장 경로 세팅
			String saveDirectory = Define.UPLOAD_DIRECTORY;
			// 폴더가 없는 경우 - 오류가 발생
			File dir = new File(saveDirectory);
			if(dir.exists() == false) {
				dir.mkdir(); // 폴더가 없다면 생성
			}
			
			
			// - 파일 이름(중복 처리 예방)
			UUID uuid = UUID.randomUUID();
			// 새로운 파일 이름 생성
			String fileName = uuid + "_" + file.getOriginalFilename(); // 사용자가 입력한 파일이름과 조합
			
			// - 전체 경로 지정 생성
			String uploadPath = Define.UPLOAD_DIRECTORY + File.separator + fileName;
			System.out.println("uploadPath = " + uploadPath);
			File destination = new File(uploadPath);
			
			// - 반드시 사용
			file.transferTo(destination);
			
			// - 객체 상태 변경
			dto.setOriginFileName(file.getOriginalFilename()); // 사용자가 기존에 입력한 파일명
			dto.setUploadFileName(fileName); // 새로 등록된 파일명
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("파일 명 = "  + dto.getOriginFileName());

		// 2-2 회원가입
		int resultRowCount = userService.signUp(dto);
		if (resultRowCount != 1) {
			// 다른 처리
		}
	
		return "redirect:/user/sign-in";
	}
	
	@PostMapping("/sign-in")
	public String signInProc(SignInFormDto dto) {
		
		// 1. 유효성 검사
		if(dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfullException("username을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("username을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		
		// 2. 핵심 로직
		User principal = userService.signIn(dto);

		// 3. session 등록
		// was에 session 영역(쿠키 + 세션)이 있다. 여기에 user에 대한 정보를 넣는다.
		session.setAttribute(Define.PRINCIPAL, principal);
		
		System.out.println("principal" + principal.toString());
		
		return "redirect:/account/list";
	}
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/user/sign-in";
	}

}
