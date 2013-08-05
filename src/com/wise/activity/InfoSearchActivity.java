package com.wise.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.wise.config.UrlConfig;
import com.wise.service.WebService;

public class InfoSearchActivity extends MapActivity {
	static final int GetRoadStations = 1;//根据路线获取站点
	static final int GetStationsRoad = 2;//根据站点获取路线
	static final int ShowButton = 3;//显示button按钮
	
	ViewFlipper flipper;
	Button bt_Route_Info,bt_Station_Info,bt_Closest_Info,bt_Navigation,bt_station_search;
	EditText et_station_name;
	ListView lv_station_road;
	Spinner s_road;
	MapView mMapView;
	MapController mMapController;
	List<Overlay> myOverlay;

	List<String> roadList = new ArrayList<String>();//公交信息
	List<RoadStationsData> roadStationsDatas;//站点信息
	List<RoadData> roadDatas; //根据站点查询的线路信息
	
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
				showRoadStationMap();
				break;
			case GetStationsRoad:
				GetStationsRoadDatas(msg.obj.toString());
				break;
				
			case ShowButton:
				bt_Route_Info.setVisibility(View.VISIBLE);
				bt_Station_Info.setVisibility(View.VISIBLE);
				bt_Closest_Info.setVisibility(View.VISIBLE);
				bt_Navigation.setVisibility(View.VISIBLE);
				System.out.println("Show Button");
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
			case R.id.bt_station_search:
				SearchRoad();
				break;
			}
		}
	};
	/**
	 * 查找线路
	 */
	private void SearchRoad(){
		String stationName = et_station_name.getText().toString().trim();
		if(!stationName.equals("")){
			new Thread(new GetStationsRoadThread(stationName)).start();
		}		
	}
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
	/**
	 * 解析站点信息
	 * @param result
	 */
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
	/**
	 * 解析站点信息
	 * @param result
	 */
	private void GetStationsRoadDatas(String result){
		roadDatas = new ArrayList<RoadData>();
		String[] str1 = result.split("Road=anyType");
		for (int i = 1; i < str1.length; i++) {
			String[] str2 = str1[i].split("; ");
			RoadData roadData = new RoadData();
			roadData.setRoadName(str2[1].substring(9));
			roadData.setStationName(str2[6].substring(12));
			roadDatas.add(roadData);
		}
		RoadAdapter roadAdapter = new RoadAdapter(getApplicationContext(), roadDatas);
		lv_station_road.setAdapter(roadAdapter);
	}
	/**
	 * 在地图上显示站点
	 */
	private void showRoadStationMap(){
		myOverlay.clear();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.station);
		for(int j = 0; j < roadStationsDatas.size()-1;j++ ){
			GeoPoint geoPoint1 = new GeoPoint((int)(roadStationsDatas.get(j).getLat()*1E6), (int)(roadStationsDatas.get(j).getLon()*1E6));
			GeoPoint geoPoint2 = new GeoPoint((int)(roadStationsDatas.get(j+1).getLat()*1E6), (int)(roadStationsDatas.get(j+1).getLon()*1E6));
			LineOverlay lineOverlay = new LineOverlay(geoPoint1, geoPoint2);
			myOverlay.add(lineOverlay);
		}
		for(int i = 0; i < roadStationsDatas.size(); i++) {
			roadStationsDatas.get(i).getLat();
			GeoPoint geoPoint = new GeoPoint((int)(roadStationsDatas.get(i).getLat()*1E6), (int)(roadStationsDatas.get(i).getLon()*1E6));
			StationOverlay stationOverlay = new StationOverlay(geoPoint, bitmap, roadStationsDatas.get(i).getStationName());
			System.out.println(roadStationsDatas.get(i).getStationName());
			myOverlay.add(stationOverlay);
		}
		if(roadStationsDatas!=null){
			GeoPoint geoPoint = new GeoPoint((int)(roadStationsDatas.get(0).getLat()*1E6), (int)(roadStationsDatas.get(0).getLon()*1E6));
			mMapController.animateTo(geoPoint);
		}
	}
	
	class StationOverlay extends Overlay{
		GeoPoint geoPoint;
		Bitmap bitmap;
		String stationName;
		/**
		 * 画站点信息
		 * @param geoPoint
		 * @param bitmap
		 * @param stationName
		 */
		public StationOverlay(GeoPoint geoPoint,Bitmap bitmap,String stationName){
			this.geoPoint = geoPoint;
			this.bitmap = bitmap;
			this.stationName = stationName;
		}
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,long when) {
			
			Paint paint = new Paint();
            Point myScreenCoords = new Point();
            // 将经纬度转换成实际屏幕坐标
            mapView.getProjection().toPixels(geoPoint, myScreenCoords);
            paint.setStrokeWidth(1);
            paint.setARGB(255, 255, 0, 0);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawBitmap(bitmap, myScreenCoords.x - bitmap.getWidth()/2, myScreenCoords.y - bitmap.getHeight()/2, paint);
            canvas.drawText(stationName, myScreenCoords.x, myScreenCoords.y - bitmap.getHeight()/2, paint);
            return true;			
		}		
	}
	class LineOverlay extends Overlay{
		GeoPoint geopoint1;
		GeoPoint geopoint2;
		/**
		 * 2点划线
		 * @param geoPoint1
		 * @param geoPoint2
		 */
		public LineOverlay(GeoPoint geoPoint1, GeoPoint geoPoint2) {
			this.geopoint1 = geoPoint1;
			this.geopoint2 = geoPoint2;
		}
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setDither(true);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(5);
			Projection projection = mapView.getProjection();
			Point p1 = new Point();
			Point p2 = new Point();
			projection.toPixels(geopoint1, p1);
			projection.toPixels(geopoint2, p2);
			canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
		}
	}
	
	
	
	class GetRoadStationsThread extends Thread{
		String RoadName;
		/**
		 * 读取站点信息
		 * @param RoadName 线路名称
		 */
		public GetRoadStationsThread(String RoadName){
			this.RoadName = RoadName;
		}
		@Override
		public void run() {
			super.run();
			try {
				String result = WebService.GetRoadStations(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetRoadStations,RoadName,UrlConfig.timeout);
				Message message = new Message();
				message.what = GetRoadStations;
				message.obj = result;
				
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class GetStationsRoadThread extends Thread{
		String Station;
		/**
		 * 根据站点读取路线信息
		 * @param RoadName 站点名称
		 */
		public GetStationsRoadThread(String Station){
			this.Station = Station;
		}
		@Override
		public void run() {
			super.run();
			try {
				String result = WebService.GetStationsRoad(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetRoadByStation,Station,UrlConfig.timeout);
				Message message = new Message();
				message.what = GetStationsRoad;
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
		bt_Station_Info = (Button)findViewById(R.id.bt_Station_Info);
		bt_Closest_Info = (Button)findViewById(R.id.bt_Closest_Info);
		bt_Navigation = (Button)findViewById(R.id.bt_Navigation);
		
		//绑定监听
		bt_Station_Info.setOnClickListener(onClickListener);
		bt_Closest_Info.setOnClickListener(onClickListener);
		bt_Route_Info.setOnClickListener(onClickListener);
		bt_Navigation.setOnClickListener(onClickListener);
		
		//设置按钮不可见
//		buttonUnDisplay();
		
		flipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
        LayoutInflater mLayoutInflater = LayoutInflater.from(InfoSearchActivity.this);        
        //Route Info
        View  routeView = mLayoutInflater.inflate(R.layout.route_info, null);
		flipper.addView(routeView);
		s_road = (Spinner)routeView.findViewById(R.id.s_road);
		mMapView = (MapView) routeView.findViewById(R.id.MapView);
		mMapController = mMapView.getController();
		mMapController.setZoom(15);
		mMapView.setEnabled(true);
		mMapView.setClickable(true);
		myOverlay = mMapView.getOverlays();
		
		
		routeView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(InfoSearchActivity.this, "OnClick", 0).show();
			}
		});
		
		
		//station info
        View stationView = mLayoutInflater.inflate(R.layout.station_info, null);
		flipper.addView(stationView);
		et_station_name = (EditText)findViewById(R.id.et_station_name);
		bt_station_search = (Button)findViewById(R.id.bt_station_search);
		bt_station_search.setOnClickListener(onClickListener);
		lv_station_road = (ListView)findViewById(R.id.lv_station_road);
		//closest info
        View closestView = mLayoutInflater.inflate(R.layout.closest_info, null);
		flipper.addView(closestView);
		//navigation info
        View navigationView = mLayoutInflater.inflate(R.layout.navigation, null);
		flipper.addView(navigationView);
		
		BindSpinner();
	}
	/**
	 * 站点信息
	 * @author 唐飞
	 */
	
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
	private class RoadData{
		String roadName;
		String stationName;
		public String getRoadName() {
			return roadName;
		}
		public void setRoadName(String roadName) {
			this.roadName = roadName;
		}
		public String getStationName() {
			return stationName;
		}
		public void setStationName(String stationName) {
			this.stationName = stationName;
		}
		@Override
		public String toString() {
			return "RoadData [roadName=" + roadName + ", stationName="
					+ stationName + "]";
		}		
	}
	
	//自定义Adapter
	class RoadAdapter extends BaseAdapter{
		LayoutInflater mInflater;
		Context context;
		List<RoadData> roadDatas;
		public RoadAdapter(Context context,List<RoadData> roadDatas){
			this.context = context;
			this.roadDatas = roadDatas;
			mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return roadDatas.size();
		}
		@Override
		public Object getItem(int arg0) {
			return roadDatas.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.station_info_row, null);
				holder = new ViewHolder();
				holder.tv_road_row_roadname = (TextView) convertView.findViewById(R.id.tv_road_row_roadname);
				holder.tv_road_row_stationname = (TextView)convertView.findViewById(R.id.tv_road_row_stationname);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_road_row_roadname.setText(roadDatas.get(position).getRoadName());
			holder.tv_road_row_stationname.setText(roadDatas.get(position).getStationName());
			return convertView;
		}
		private class ViewHolder {
			TextView tv_road_row_roadname,tv_road_row_stationname;
		}
	}
	
	
	//设置按钮不可见
	public void buttonUnDisplay(){
		bt_Route_Info.setVisibility(View.GONE);
		bt_Station_Info.setVisibility(View.GONE);
		bt_Closest_Info.setVisibility(View.GONE);
		bt_Navigation.setVisibility(View.GONE);
	}
	
	
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}	
}