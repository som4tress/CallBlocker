package com.zebra.SES.CallManager;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IncomingCallController extends BroadcastReceiver {
	private static final int MODE_WORLD_READABLE = 1;
	private ITelephony telephonyService;
	private String incommingNumber;
	private SharedPreferences myPrefs; 
	private String TAG = "INCOMINGCALLCONTROLLER";


	@Override
	public void onReceive(Context context, Intent intent) {		
		myPrefs = context.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		String incomingBlockingMode = myPrefs.getString("INCOMING_BLOCK_MODE", "not retrieved");

		Bundle bb = intent.getExtras();  
		String state = bb.getString(TelephonyManager.EXTRA_STATE);		
		if(state == null) return;

		if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
			if(!incomingBlockingMode.equals("REMOVE_CALL_BLOCKING")) {
				incommingNumber = bb.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
				if(incommingNumber == null) incommingNumber = "Unknown";

				Log.i(TAG, "Incoming call from " + incommingNumber);

				WhiteListDB mDbAdapter = new WhiteListDB(context);
				mDbAdapter.open();
				Cursor c = mDbAdapter.fetchAllNumberss();

				if(incomingBlockingMode.equals("BLOCK_ALL_INCOMING"))
				{
					blockIncomingCall(context, bb);
				} else if(incomingBlockingMode.equals("BLOCK_ALL_INCOMING_FROM_LIST")) {					
					if(c.moveToFirst())	{
						while (c.isAfterLast() == false) {  
							String	title= c.getString(c.getColumnIndex(WhiteListDB.KEY_TITLE)); 
							if(title.contains(incommingNumber))  {
								blockIncomingCall(context, bb);
								Toast.makeText(context, "Call from " + incommingNumber + " has been blocked !!", Toast.LENGTH_SHORT).show();
								break;
							}
							c.moveToNext();
						} 
					}
				} else if(incomingBlockingMode.equals("ALLOW_ALL_INCOMING_FROM_LIST")) {		
					boolean allowCallFlag = false;
					if(c.moveToFirst())	{
						while (c.isAfterLast() == false) {  
							String	title= c.getString(c.getColumnIndex(WhiteListDB.KEY_TITLE)); 
							if(title.contains(incommingNumber))  {
								allowCallFlag = true;
								Toast.makeText(context, "Call from " + incommingNumber + " has been allowed !!", Toast.LENGTH_SHORT).show();
								break;
							}
							c.moveToNext();
						} 
					}

					if(allowCallFlag == false)
						blockIncomingCall(context, bb);						
				}

				c.close();
				mDbAdapter.close();
			}			
		} // END IF INCOMING CALL HANDLER
	}

	public void blockIncomingCall(Context c, Bundle b)
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
	
	public String getContactDisplayNameByNumber(String number, Context c) {

		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		String data=null;
		ContentResolver contentResolver =c.getContentResolver();
		Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
				ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

		try {
			if (contactLookup != null && contactLookup.getCount() > 0) {
				contactLookup.moveToNext();
				data = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));				
			}
		} finally {
			if (contactLookup != null) {
				contactLookup.close();
			}
		}


		return data;
	}  
}
