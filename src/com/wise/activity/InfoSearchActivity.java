package com.wise.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewFlipper;
import com.google.android.maps.MapActivity;

public class InfoSearchActivity extends MapActivity {
	ViewFlipper flipper;
	Button bt_Route_Info,bt_Station_Info,bt_Closest_Info,bt_Navigation;
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		init();
	}
	
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
		//station info
        View stationView = mLayoutInflater.inflate(R.layout.station_info, null);
		flipper.addView(stationView);
		//closest info
        View closestView = mLayoutInflater.inflate(R.layout.closest_info, null);
		flipper.addView(closestView);
		//navigation info
        View navigationView = mLayoutInflater.inflate(R.layout.navigation, null);
		flipper.addView(navigationView);
	}
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}	
}
