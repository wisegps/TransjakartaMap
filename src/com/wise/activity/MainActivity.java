package com.wise.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends TabActivity {
	private TabHost tabHost = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		tabHost = getTabHost();
	  	// ��ӱ�ǩ
		addArrival();
		addAnnouncement();
		addInfoSearch();
		tabHost.setCurrentTab(0);
	}


	// ���Arrival��ǩҳ ��0ҳ
	public void addArrival() {
		Intent ArrivalIntent = getIntent();
		ArrivalIntent.setClass(MainActivity.this, RoadActivity.class);
		TabHost.TabSpec ArrivalSpec = getTabHost().newTabSpec("Est Arrival");
		ArrivalSpec.setIndicator("Est Arrival",getResources().getDrawable(R.drawable.ic_launcher));
		ArrivalSpec.setContent(ArrivalIntent);
		getTabHost().addTab(ArrivalSpec);
	}
	// ���Announcement��ǩҳ ��1ҳ
	public void addAnnouncement() {
		Intent AnnouncementIntent = new Intent();
		AnnouncementIntent.setClass(MainActivity.this, Announcement.class);
		TabHost.TabSpec AnnouncementSpec = getTabHost().newTabSpec("Announcement");
		AnnouncementSpec.setIndicator("Announcement", getResources().getDrawable(R.drawable.ic_launcher));
		AnnouncementSpec.setContent(AnnouncementIntent);
		getTabHost().addTab(AnnouncementSpec);
	}
	// ���InfoSearch��ǩҳ ��2ҳ
	public void addInfoSearch() {
		Intent InfoSearchIntent = new Intent();
		InfoSearchIntent.setClass(MainActivity.this, InfoSearchActivity.class);
		TabHost.TabSpec InfoSearchSpec = getTabHost().newTabSpec("Info Search");
		InfoSearchSpec.setIndicator("Info Search",getResources().getDrawable(R.drawable.ic_launcher));
		InfoSearchSpec.setContent(InfoSearchIntent);
		getTabHost().addTab(InfoSearchSpec);
	}

}