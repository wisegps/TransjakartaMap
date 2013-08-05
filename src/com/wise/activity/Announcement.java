package com.wise.activity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wise.config.UrlConfig;
import com.wise.net.GetSystem;
import com.wise.net.NetThread;
import com.wise.service.WebService;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author 唐飞 
 */
public class Announcement extends Activity {
	private static final int GetData = 1;
	ListView lv_Announcement;
	List<AnnouncementData> announcementDatas = new ArrayList<AnnouncementData>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement);
		init();
	}
	Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case GetData:
				jsonData(msg.obj.toString());
				//绑定到list
				NewAdapter newAdapter = new NewAdapter(Announcement.this, announcementDatas);
				lv_Announcement.setAdapter(newAdapter);
				break;
			}
		}		
	};
	private void init(){
		lv_Announcement = (ListView)findViewById(R.id.lv_Announcement);
		new Thread(new GetAnnouThread()).start();
	}
	class GetAnnouThread extends Thread{
		@Override
		public void run() {
			super.run();
			try {
				String result = WebService.GetAnnouncementInfo(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetAnnouncementInfo, 2000);
				Message message = new Message();
				message.what = GetData;
				message.obj = result;
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 解析数据
	 * @param result
	 */
	private void jsonData(String result){
		try {
			String[] str1 = result.split("Announcement=anyType");
			for(int i = 1 ; i < str1.length ; i++){
				String[] str2 = str1[i].split("; ");
				AnnouncementData announcementData = new AnnouncementData();
				announcementData.setTime(str2[0].substring(6).replace("T", " ").replace("-", "/"));
				announcementData.setMessage(str2[2].substring(8));
				announcementData.setKoridor(str2[1].substring(8));
				announcementDatas.add(announcementData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class AnnouncementData{
		String Time;
		String Koridor;
		String Message;
		public String getTime() {
			return Time;
		}
		public void setTime(String time) {
			
			this.Time = time;
		}
		public String getKoridor() {
			return Koridor;
		}
		public void setKoridor(String koridor) {
			Koridor = koridor;
		}
		public String getMessage() {
			return Message;
		}
		public void setMessage(String message) {
			Message = message;
		}		
	}
	
	public class NewAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		Context mContext;
		List<AnnouncementData> Datas;
		public NewAdapter(Context context,List<AnnouncementData> smsDatas){
			mInflater = LayoutInflater.from(context);
			mContext = context;
			this.Datas = smsDatas;
		}
		public int getCount() {
			return Datas.size();
		}
		public Object getItem(int position) {
			return Datas.get(position);
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.announcement_row, null);
				holder = new ViewHolder();
				holder.tv_announcement_time = (TextView) convertView.findViewById(R.id.tv_announcement_time);
//				holder.tv_announcement_Koridor = (TextView)convertView.findViewById(R.id.tv_announcement_Koridor);
				holder.tv_announcement_Message = (TextView)convertView.findViewById(R.id.tv_announcement_Message);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_announcement_time.setText(Datas.get(position).getTime());
//			holder.tv_announcement_Koridor.setText(Datas.get(position).getKoridor());
			holder.tv_announcement_Message.setText(Datas.get(position).getMessage());
			return convertView;
		}
		private class ViewHolder {
			TextView tv_announcement_time,tv_announcement_Koridor,tv_announcement_Message;
		}
	}
}