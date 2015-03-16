package com.zebra.SES.CallManager;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class OutgoingCallController extends BroadcastReceiver {

	private static final int MODE_WORLD_READABLE = 1;
	private final String TAG = "OutgoingCallController";
	private SharedPreferences myPrefs; 
	private ITelephony telephonyService;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		if(null == bundle)
			return;

		String outgoingPhonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		Log.i(TAG, "Outgoing call to " + outgoingPhonenumber);		

		myPrefs = context.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		String outgoingBlockingMode = myPrefs.getString("OUTGOING_BLOCK_MODE", "not retrieved");

		WhiteListDB mDbAdapter = new WhiteListDB(context);
		mDbAdapter.open();
		Cursor c = mDbAdapter.fetchAllNumberss();

		if(outgoingBlockingMode.equals("BLOCK_ALL_OUTGOING")) {
			blockOutgoingCall(context, bundle);
			setResultData(null);
			Toast.makeText(context, "Outgoing call to " + outgoingPhonenumber + " has been blocked !!", Toast.LENGTH_SHORT).show();
		} else if(outgoingBlockingMode.equals("BLOCK_ALL_OUTGOING_FROM_LIST")) {
			if(c.moveToFirst())	{
				while (c.isAfterLast() == false) {  
					String	title= c.getString(c.getColumnIndex(WhiteListDB.KEY_TITLE)); 
					if(title.contains(outgoingPhonenumber))  {
						blockOutgoingCall(context, bundle);
						setResultData(null);
						Toast.makeText(context, "Outgoing call to " + outgoingPhonenumber + " has been blocked !!", Toast.LENGTH_SHORT).show();
						break;
					}
					c.moveToNext();
				} 
			}
		} else if(outgoingBlockingMode.equals("ALLOW_ALL_OUTGOING_FROM_LIST")) {
			boolean allowCallFlag = false;
			if(c.moveToFirst())	{
				while (c.isAfterLast() == false) {  
					String	title= c.getString(c.getColumnIndex(WhiteListDB.KEY_TITLE)); 
					if(title.contains(outgoingPhonenumber))  {
						allowCallFlag = true;
						Toast.makeText(context, "Call from " + outgoingPhonenumber + " has been allowed !!", Toast.LENGTH_SHORT).show();
						break;
					}
					c.moveToNext();
				} 
			}

			if(allowCallFlag == false) {
				blockOutgoingCall(context, bundle);
				setResultData(null);
			}
		}

		c.close();
		mDbAdapter.close();
	}

	public void blockOutgoingCall(Context c, Bundle b)
	{
		TelephonyManager telephony = (TelephonyManager) 
				c.getSystemService(Context.TELEPHONY_SERVICE);  
		try {
			Class cls = Class.forName(telephony.getClass().getName());
			Method m = cls.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			telephonyService = (ITelephony) m.invoke(telephony);
			//telephonyService.silenceRinger();
			telephonyService.endCall();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
