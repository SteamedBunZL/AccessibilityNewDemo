package com.zl.accessibilitydemo04;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener, ServiceConnection {

	
	private Button mButtonClear;

	private Button mButtonState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		bindClearService();
		mButtonClear = (Button) findViewById(R.id.button_clear);
		mButtonState = (Button) findViewById(R.id.button_clear_state);
		mButtonClear.setOnClickListener(this);
		mButtonState.setOnClickListener(this);
		//判断service是否运行
		if (isServiceWorked()) {
			bindClearService();
			mButtonState.setText("服务已经开启，可以清理");
			mButtonState.setEnabled(false);
		}else {
			mButtonState.setText("开启强力清理模式");
			mButtonState.setEnabled(true);
		}
	}
	
	@Override
	protected void onRestart() {
		//判断service是否运行
		if (isServiceWorked()) {
			bindClearService();
			mButtonState.setText("服务已经开启，可以清理");
			mButtonState.setEnabled(false);
		}else {
			mButtonState.setText("开启强力清理模式");
			mButtonState.setEnabled(true);
		}
		super.onRestart();
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_clear:
			Intent intent_install = new Intent("/");
			ComponentName cm = new ComponentName("com.android.settings",
					"com.android.settings.applications.InstalledAppDetailsTop");
			intent_install.setComponent(cm);
			intent_install.setData(Uri.parse("com.netease.newsreader.activity"));
			intent_install.setAction("android.intent.action.VIEW");
			startActivity(intent_install);
			break;
		case R.id.button_clear_state:
			Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
			startActivity(intent);
			break;
		}
	}
	
	private void bindClearService(){
		Intent intent = new Intent(this,ClearService.class);
		bindService(intent, this, BIND_AUTO_CREATE);
	}
	
	private void unbindClearService(){
		unbindService(this);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.d(ClearService.TAG, "MainActivity onServiceConnected.");
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.d(ClearService.TAG, "MainActivity onServiceDisconnected.");
	}

	public boolean isServiceWorked() {
		ActivityManager myManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals("com.zl.accessibilitydemo04.ClearService")) {
				return true;
			}
		}
		return false;
	}
}
