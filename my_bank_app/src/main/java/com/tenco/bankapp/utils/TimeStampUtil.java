package com.tenco.bankapp.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeStampUtil {

	// 상태값을 가지는 변수를 사용하면 안된다.
	public static String timestampToString(Timestamp time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		return sdf.format(time);
	}
}
