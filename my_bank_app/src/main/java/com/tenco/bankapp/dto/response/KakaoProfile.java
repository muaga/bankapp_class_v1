package com.tenco.bankapp.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoProfile {

	private Long id;
	private String connectedAt;
	private Properties properties;

	@Data
	public class Properties{
		private String nickname;
		private String profileImage;
		private String thumbnailImage;
	}
}
