package com.zl.accessibilitydemo04;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import junit.framework.Test;

public class ClearService extends AccessibilityService{

	public static final String TAG = "ClearService";
	
	private boolean flag1 = false;
	private boolean flag2 = false;
	
	private Map<String, Boolean> map = new HashMap<>();
	
	private String netease = "com.android.settings.applications.InstalledAppDetailsTop";
	
	private String drawq = "";
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		int eventType = event.getEventType();
		switch (eventType) {
		case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
			String className = event.getClassName().toString();
			Log.d(TAG, className);
			if (netease.equals(className)) {
				performClick(netease);
			}
			else if ("android.app.AlertDialog".equals(className)) {
//				performClick2();
			}
			break;
		case AccessibilityEvent.TYPE_VIEW_CLICKED:
			Log.d(TAG, "AccessibilityEvent.TYPE_VIEW_CLICKED");
			List<CharSequence> text = event.getText();
			for(CharSequence cs:text){
				if ("强行停止".equals(cs.toString())) {
					performClick2();
				}else if ("确定".equals(cs.toString())) {
					
				}
			}
			break;
		case AccessibilityEvent.TYPE_VIEW_FOCUSED:
			Log.d(TAG, "AccessibilityEvent.TYPE_VIEW_FOCUSED");
			break;
		default:
			if (eventType!=AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
				Log.d(TAG, "event : 0x" + Integer.toHexString(eventType));
			}
			break;
		}
		
	}


	@SuppressLint("NewApi")
	private void performClick2() {
		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if (nodeInfo != null) {
			List<AccessibilityNodeInfo> list = nodeInfo
					.findAccessibilityNodeInfosByText("确定");
			for (AccessibilityNodeInfo n : list) {
				n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			}
		}
	}

	@SuppressLint("NewApi")
	private void performClick(String packagename) {
		if (map.get(packagename)!=null&&map.get(packagename)==true) {
			return;
		}
		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if (nodeInfo != null) {
			List<AccessibilityNodeInfo> list = nodeInfo
					.findAccessibilityNodeInfosByText("强行停止");
			for (AccessibilityNodeInfo n : list) {
				n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				map.put(packagename, true);
			}
		}
	}
	
	

	@Override
	public void onInterrupt() {
		Log.d(TAG, "onInterrupt");
	}
	
	@Override
	protected void onServiceConnected() {
		Log.d(TAG, "onServiceConnected");
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(getApplicationContext(),MainActivity.class);
		startActivity(intent);
		super.onServiceConnected();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "onUnbind");
		return super.onUnbind(intent);
	}
	
	

}
