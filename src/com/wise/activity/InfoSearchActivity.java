package com.wise.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ViewFlipper;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.wise.config.UrlConfig;
import com.wise.service.WebService;

public class InfoSearchActivity extends MapActivity {
	static final int GetRoadStations = 1;
	
	ViewFlipper flipper;
	Button bt_Route_Info,bt_Station_Info,bt_Closest_Info,bt_Navigation;
	Spinner s_road;
	MapView mMapView;
	MapController mMapController;
	List<Overlay> myOverlay;
	

	List<String> roadList = new ArrayList<String>();
	List<RoadStationsData> roadStationsDatas;//站点信息
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		init();
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GetRoadStations:
				GetRoadStationsDatas(msg.obj.toString());
				break;
			}
		}		
	};
	
	OnClickListener onClickListener = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_Route_Info:
				flipper.setDisplayedChild(0);
				break;
			case R.id.bt_Station_Info:		
				flipper.setDisplayedChild(1);
				System.out.println("1");
				break;
			case R.id.bt_Closest_Info:	
				flipper.setDisplayedChild(2);
				break;
			case R.id.bt_Navigation:
				flipper.setDisplayedChild(3);
				break;
			}
		}
	};
	private void BindSpinner(){
		for(int i = 0 ; i < UrlConfig.roadInfos.size();i++){
			roadList.add(UrlConfig.roadInfos.get(i).getRoadName());
		}
		ArrayAdapter<String> roadAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roadList);
		roadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 设置下拉列表的风格
		s_road.setAdapter(roadAdapter);
		s_road.setOnItemSelectedListener(onItemSelectedListener);
	}
	
	OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			new Thread(new GetRoadStationsThread(roadList.get(arg2))).start();
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	};	
	private void GetRoadStationsDatas(String result){
		roadStationsDatas = new ArrayList<RoadStationsData>();
		String[] str1 = result.split("RoadInfo=anyType");
		for (int i = 1; i < str1.length; i++) {
			String[] str2 = str1[i].split("; ");
			RoadStationsData roadStationsData = new RoadStationsData();
			roadStationsData.setStationName(str2[8].substring(10));
			roadStationsData.setLat(Double.parseDouble(str2[6].substring(4)));
			roadStationsData.setLon(Double.parseDouble(str2[5].substring(4)));
			roadStationsDatas.add(roadStationsData);
		}
	}
	
	class GetRoadStationsThread extends Thread{
		String RoadName;
		public GetRoadStationsThread(String RoadName){
			this.RoadName = RoadName;
		}
		@Override
		public void run() {
			super.run();
			try {
				String result = WebService.GetRoadStations(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetRoadStations,RoadName,2000);
				Message message = new Message();
				message.what = GetRoadStations;
				message.obj = result;
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void init(){
		setContentView(R.layout.info_search);
		bt_Route_Info = (Button)findViewById(R.id.bt_Route_Info);
		bt_Route_Info.setOnClickListener(onClickListener);
		bt_Station_Info = (Button)findViewById(R.id.bt_Station_Info);
		bt_Station_Info.setOnClickListener(onClickListener);
		bt_Closest_Info = (Button)findViewById(R.id.bt_Closest_Info);
		bt_Closest_Info.setOnClickListener(onClickListener);
		bt_Navigation = (Button)findViewById(R.id.bt_Navigation);
		bt_Navigation.setOnClickListener(onClickListener);
		
		flipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
        LayoutInflater mLayoutInflater = LayoutInflater.from(InfoSearchActivity.this);        
        //Route Info
        View routeView = mLayoutInflater.inflate(R.layout.route_info, null);
		flipper.addView(routeView);
		s_road = (Spinner)routeView.findViewById(R.id.s_road);
		mMapView = (MapView) routeView.findViewById(R.id.MapView);
		mMapController = mMapView.getController();
		mMapController.setZoom(15);
		mMapView.setEnabled(true);
		mMapView.setClickable(true);
		myOverlay = mMapView.getOverlays();
		//station info
        View stationView = mLayoutInflater.inflate(R.layout.station_info, null);
		flipper.addView(stationView);
		//closest info
        View closestView = mLayoutInflater.inflate(R.layout.closest_info, null);
		flipper.addView(closestView);
		//navigation info
        View navigationView = mLayoutInflater.inflate(R.layout.navigation, null);
		flipper.addView(navigationView);
		
		BindSpinner();
	}
	private class RoadStationsData{
		String stationName;
		double lon;
		double lat;
		public String getStationName() {
			return stationName;
		}
		public void setStationName(String stationName) {
			this.stationName = stationName;
		}
		public double getLon() {
			return lon;
		}
		public void setLon(double lon) {
			this.lon = lon;
		}
		public double getLat() {
			return lat;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}
		@Override
		public String toString() {
			return "RoadStationsData [stationName=" + stationName + ", lon="
					+ lon + ", lat=" + lat + "]";
		}		
	}
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}	
}
