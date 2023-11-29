package com.tenco.bankapp.controller;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tenco.bankapp.dto.BoardDto;

@RestController
public class RestHomeController {

	// 웹 브라우저 -> 우리 서버로 진입
	// http://localhost:80/todos/1
//	@GetMapping("/todos/{id}")
//	public String restTemplateTest1(@PathVariable Integer id){
//		
//		// MIME TYPE : text/html 타입으로 내려짐
//		return "안녕 반가워";
//	}
	
	// 웹 브라우저 -> 우리 서버로 진입
	// http://localhost:80/todos/1
	@GetMapping("/todos/{id}")
	public ResponseEntity<?> restTemplateTest1(@PathVariable Integer id){
		
		// 다른 서버에 자원 요청
		// 1. 접근 URI 서버 생성
		// https://jsonplaceholder.typicode.com/todos/1
		URI uri = UriComponentsBuilder
				.fromUriString("https://jsonplaceholder.typicode.com") // String을 넣으면 URI 객체를 만들어 준다, baseURI만 세팅
				.path("/todos")
				.path("/" + id)
				.encode()
				.build()
				.toUri(); // 객체가 다르므로 toUri로 해야한다.
		
		// 2. 객체 생성
		RestTemplate restTemplate = new RestTemplate();
		// restTemplate.getForEntity(uri, return 타입) : get 방식으로 URI를 요청
		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());
		System.out.println(response.getHeaders());
		
		// MIME TYPE : text/html 타입으로 내려짐
		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	} 
	
	// POST 방식과 exchange 메소드 사용
	// http://localhost:80/exchange-test
	@GetMapping("/exchange-test")
	public ResponseEntity<?> restTemplateTest2(){
	
		// 다른 서버에 자원 등록 요청 -> POST 방식 사용법
		// 1. 접근 URI 서버 생성
		URI uri = UriComponentsBuilder
				.fromUriString("https://jsonplaceholder.typicode.com")
				.path("/posts")
				.encode()
				.build()
				.toUri();
		// 2. 객체 생성
		RestTemplate restTemplate = new RestTemplate();
		
		// * exchange 사용 방법
		// 1. HttpHeaders 객체를 만들고 Header 메세지 구성
		// 2. body 데이터를 key=value 구조로 만들기
		// 3. HttpEntity 객체를 생성해서 Header와 결합 후 요청
		
		// Header 구성
		// 1. HttpHeaders 객체를 만들고 Header 메세지 구성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json; charset=UTF-8");
		
		// 2. body 데이터를 key=value 구조로 만들기
		// Body 구성
//	    title: 'foo',
//	    body: 'bar',
//	    userId: 1
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		// JSON으로 구성하는 key=value를 추가할 수 있다
		params.add("title", "블로그 포스트 1");
		params.add("body", "후미진 어느 언덕에서 도시락 소풍");
		params.add("userId", "1");
		
		// 3. HttpEntity 객체를 생성해서 Header와 결합 후 요청
		// 헤더와 body의 결합
		HttpEntity<MultiValueMap<String, String>> requestMessage 
		= new HttpEntity<>(params, headers);
		
		// HTTP 요청 처리
//		ResponseEntity<String> response =
//		restTemplate.exchange(uri, HttpMethod.POST, requestMessage, String.class);
		// 해당 uri로, post등록, 요청 메세지, 응답 타입 클래스
		
		// HTTP 요청 처리
		// 다른 서버에서 넘겨받은 데이터를 DB에 저장하고자 한다면
		// 파싱 처리 해야한다.
		ResponseEntity<BoardDto> response =
		restTemplate.exchange(uri, HttpMethod.POST, requestMessage, BoardDto.class);
		BoardDto boardDto = response.getBody();
		
		System.out.println("headers + " + response.getHeaders());
		
		return ResponseEntity.status(HttpStatus.OK).body(boardDto.getTitle());
	}
}
