package com.wise.net;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetSystem {
	/**
	 * ʱ���ת�ַ���
	 * @param time
	 * @return
	 */
	public static String getStrTime(String time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		re_StrTime = sdf.format(new Date(Long.valueOf(time)));
		return re_StrTime;
	}
}
