package com.wise.activity;

import java.util.ArrayList;
import com.wise.bean.RoadInfo;
import com.wise.config.UrlConfig;
import com.wise.net.NetThread;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class WelcomeActivity extends Activity {
	private MyHandler myHandler = null;
	private ArrayList<RoadInfo> roadInfos = new ArrayList<RoadInfo>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		myHandler = new MyHandler();
		try {
		    Class.forName("com.google.android.maps.MapActivity");
		} catch (Exception e) {
			e.printStackTrace();
			ExitDialog();
		    return;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		 //检查网络
        if(!checkNetWorkStatus(WelcomeActivity.this)){
        	//跳转到手机网络设置页面
        	Toast.makeText(WelcomeActivity.this,getString(R.string.net_unalive), Toast.LENGTH_LONG).show();
			AlertDialog.Builder setNet = new Builder(WelcomeActivity.this);
			setNet.setTitle(getString(R.string.prompt));
			setNet.setMessage(getString(R.string.set_net));
			setNet.setPositiveButton(getString(R.string.sure_set), new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
				}
			});
			setNet.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					WelcomeActivity.this.finish();
				}
			});
			setNet.show();
        }else{
        	new Thread(new NetThread.GetRodListThread(myHandler, UrlConfig.url,
    				UrlConfig.nameSpace, UrlConfig.MethodGetRoadName,
    				UrlConfig.timeout, 0)).start();
        }
	}
	// 用于异步接收并且处理相应的结果
	class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String result = msg.obj.toString();
			if(result.equals("Exception")){
				Toast.makeText(getApplicationContext(), getString(R.string.read_error), Toast.LENGTH_LONG).show();
			}else{
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
			}			
			finish();
		}
	}
	
	
	//检查网络
	public boolean checkNetWorkStatus(Context context) {
		boolean result;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	
	//提示没有谷歌服务
	private void ExitDialog(){
		AlertDialog.Builder bulder = new AlertDialog.Builder(WelcomeActivity.this);
		bulder.setTitle(R.string.prompt);// 设置标题
		bulder.setMessage(R.string.google_unalive);
		bulder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				WelcomeActivity.this.finish();
			}
		});
		bulder.show();
	}
}