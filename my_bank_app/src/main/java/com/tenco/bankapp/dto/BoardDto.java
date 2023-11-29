package com.tenco.bankapp.dto;

import lombok.Data;

//{
//	  "title": [
//	    "블로그 포스트 1"
//	  ],
//	  "body": [
//	    "후미진 어느 언덕에서 도시락 소풍"
//	  ],
//	  "userId": [
//	    "1"
//	  ],
//	  "id": 101
//	}

@Data
public class BoardDto {
	private String title;
	private String body;
	private String userId;
}
