package com.tenco.bankapp.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tenco.bankapp.dto.SignInFormDto;
import com.tenco.bankapp.dto.SignUpFormDto;
import com.tenco.bankapp.dto.response.KakaoProfile;
import com.tenco.bankapp.dto.response.OAuthToken;
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
	
	@Value("${tenco.key}")
	private String tencoKey;

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

	// http://localhost:80/user/kakao-callback?code=EASDFEADSFAFDE
	@GetMapping("/kakao-callback")
//	@ResponseBody // 데이터를 반환하고 싶을 때
	public String kakaoCallBack(@RequestParam String code) {
		
		// * 액세스 토큰 요청 ---> Server to Server
		RestTemplate rt1 = new RestTemplate();
		// 헤더 구성
		HttpHeaders headers1 = new HttpHeaders();
		headers1.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// 바디 구성
		MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
		params1.add("grant_type", "authorization_code");
		params1.add("client_id", "ca7abe11c882e821106b13207aa589ee");
		params1.add("redirect_uri", "http://localhost:80/user/kakao-callback");
		params1.add("code", code); // 인가 코드

		// 헤더 + 바디 결합 => HTTP MSG 완성
		HttpEntity<MultiValueMap<String, String>> requestMsg1
			= new HttpEntity<>(params1, headers1);
		
		// 요청 처리 - ResponseEntity로 응답이 된다.
		ResponseEntity<OAuthToken> response1  
		= rt1.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, requestMsg1,OAuthToken.class);
		
		System.out.println("===================================");
//		System.out.println(response1.getHeaders());
//		System.out.println(response1.getBody());
		System.out.println(response1.getBody().getAccessToken());
		System.out.println(response1.getBody().getRefreshToken());
		System.out.println("===================================");
		//---------------------------------------------- 여기까지 토큰을 받기 위함
		
		// * 토큰을 통해 사용자 정보 요청하기
		RestTemplate rt2 = new RestTemplate();
		
		// 헤더 구성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + response1.getBody().getAccessToken());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 바디 구성 - 생략(필수가 아님)
		
		// 헤더 + 바디 결합 => HTTP MSG 완성
		HttpEntity<MultiValueMap<String, String>> requestMsg2
			= new HttpEntity<>(headers2);
		
		// 요청 처리 - ResponseEntity로 응답이 된다.
		ResponseEntity<KakaoProfile> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, requestMsg2, KakaoProfile.class);
		
		System.out.println("===================================");
//		System.out.println(response1.getHeaders());
		System.out.println(response2.getBody().getProperties().getNickname());
		System.out.println("===================================");		
		//---------------------------------------------- 여기까지 카카오 서버에 존재하는 정보를 요청
		System.out.println("--------------카카오 서버 정보 받기 완료------------------");
		
		// 1. 회원 가입 여부 확인 
		// 최초 사용자라면 우리 사이트에 회원가입을 자동 완료
		// 추가 정보 입력 화면 -> 카카오 서버에서 제공하지 않는 추가 정보가 있다면 기능을 만들어야 한다. - DB insert 처리 필수
		
		KakaoProfile kakaoProfile = response2.getBody();
		// 소셜 회원 가입자는 전부 비밀번호가 동일하게 된다. 그래서 password는 절대 유출되어서는 안되는 값이다. 
		// => application.yml에 지정(해시 함수)
		SignUpFormDto signUpFormDto = SignUpFormDto.builder()
											.username("OAuth_" + kakaoProfile.getId() + "님")
											.fullname("kakao")
											.password(tencoKey)
											.build();
		User oldUser = userService.searchUsername(signUpFormDto.getUsername());
		// 소셜로 로그인한 유저
		
		// 회원가입 자동 처리
		if(oldUser == null) {
			// oldUser가 null이라면, 최초 회원 가입 처리를 해줘야 한다.
			userService.signUp(signUpFormDto); 

			// 다시 사용자 정보 조회 처리
			oldUser = userService.searchUsername(signUpFormDto.getUsername());
		}
		
		// 만약 소셜 로그인 사용자가 회원가입 처리 완료된 사용자라면 바로 세션 처리 및 로그인 처리하면 된다.
		// 로그인 처리
		oldUser.setPassword(null); 
		// 비밀번호 유출 위험 방지 - 불필요한 정보를 클라이언트한테 반환하는 것은 보안상의 위험도가 높아지기 때문에 하지 말아야 한다.
		session.setAttribute(Define.PRINCIPAL, oldUser); // 세션 등록
		
		return "redirect:/account/list";
	}
}
