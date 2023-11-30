package com.tenco.bankapp.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

// {"access_token":"9ZpNj51nafbws5PgXLhrleALxMt2MHPGLIwKPXTbAAABjB4MaNVHueF-5ScOZw","token_type":"bearer","refresh_token":"tvblJtAEbEYf3v2SLi3xuIgrB--Yt77XaY8KPXTbAAABjB4MaNJHueF-5ScOZw","expires_in":21599,"scope":"profile_image profile_nickname","refresh_token_expires_in":5183999}
@Data
// JSON 형식에 코딩 컨벤션의 스네이크 케이스를 자바 카멜 노테이션으로 변환처리
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OAuthToken {
	private String accessToken; // access_token -> accessToken으로 가능하다
	private String tokenType;
	private String refreshToken;
	private String expiresIn;
	private String scope;
}
