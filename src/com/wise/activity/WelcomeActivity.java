package com.wise.activity;

import java.util.ArrayList;
import com.wise.bean.RoadInfo;
import com.wise.config.UrlConfig;
import com.wise.net.NetThread;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WelcomeActivity extends Activity {
	private MyHandler myHandler = null;
	private ArrayList<RoadInfo> roadInfos = new ArrayList<RoadInfo>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		myHandler = new MyHandler();
		new Thread(new NetThread.GetRodListThread(myHandler, UrlConfig.url,
				UrlConfig.nameSpace, UrlConfig.MethodGetRoadName,
				UrlConfig.timeout, 0)).start();
	}

	// 用于异步接收并且处理相应的结果
	class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String result = msg.obj.toString();
			String[] array1 = result.split("Road=anyType");
			for (int i = 1; i < array1.length; i++) {
				String[] array2 = array1[i].split("; ");
				RoadInfo roadInfo = new RoadInfo();
				roadInfo.setRoadName(array2[1].substring(9));
				roadInfos.add(roadInfo);
			}
			UrlConfig.roadInfos = roadInfos;
			// 跳转显示所有路线
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}