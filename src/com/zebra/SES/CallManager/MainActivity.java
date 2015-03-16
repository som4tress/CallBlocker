package com.zebra.SES.CallManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity  extends Activity implements RadioGroup.OnCheckedChangeListener, OnClickListener {

	private SharedPreferences myPrefs; 
	private SharedPreferences.Editor editor; 

	private RadioButton _blockAllIncoming, _blockIncomingFromList, _cancelAllIncomingBlock, _allowIncomingFromList;
	private RadioButton _blockAllOutgoing, _blockOutgoingFromList, _cancelAllOutgoingBlock, _allowOutgoingFromList;
	
	//
	private Button btnAddNumber;
	private Button btnShowList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		editor = myPrefs.edit();     
		
		RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radGroup1);
		RadioGroup radiogroup2 = (RadioGroup) findViewById(R.id.radGroup2);
		
		radiogroup1.setOnCheckedChangeListener(this);
		radiogroup2.setOnCheckedChangeListener(this);
		
		// incoming radio button setting
		_blockAllIncoming=(RadioButton) findViewById(R.id.blockAllInoming);		
		_blockIncomingFromList=(RadioButton) findViewById(R.id.blockIncomingFromList);
		_cancelAllIncomingBlock=(RadioButton) findViewById(R.id.cancelIncomingBlocker);
		_allowIncomingFromList=(RadioButton) findViewById(R.id.allowIncomingFromList);
		
		// outgoing radio button setting
		_blockAllOutgoing=(RadioButton) findViewById(R.id.blockAllOutgoing);		
		_blockOutgoingFromList=(RadioButton) findViewById(R.id.blockOutgoingFromList);
		_cancelAllOutgoingBlock=(RadioButton) findViewById(R.id.cancelOutgoingBlocker);
		_allowOutgoingFromList=(RadioButton) findViewById(R.id.allowOutgoingFromList);
		
		setDefaultButtonChecked();
		// add and show button controls
		btnAddNumber=(Button) findViewById(R.id.btnAdd);
		btnShowList=(Button) findViewById(R.id.btnShow);
		btnAddNumber.setOnClickListener(this);
		btnShowList.setOnClickListener(this);
	}
	
	private  void setDefaultButtonChecked()
	{
		myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		
		String incomingValue = myPrefs.getString("INCOMING_BLOCK_MODE", "REMOVE_CALL_BLOCKING");
		String outgoingValue = myPrefs.getString("OUTGOING_BLOCK_MODE", "REMOVE_CALL_BLOCKING");
		
		if(incomingValue.equals("BLOCK_ALL_INCOMING"))  {
			_blockAllIncoming.setChecked(true);
		} else if(incomingValue.equals("BLOCK_ALL_INCOMING_FROM_LIST")) {
			_blockIncomingFromList.setChecked(true);
		} else if(incomingValue.equals("ALLOW_ALL_INCOMING_FROM_LIST")) {
			_allowIncomingFromList.setChecked(true);
		} else if(incomingValue.equals("REMOVE_CALL_BLOCKING")) 	{
			_cancelAllIncomingBlock.setChecked(true);
		} else 	{
			_cancelAllIncomingBlock.setChecked(true);
		}
		
		if(outgoingValue.equals("BLOCK_ALL_OUTGOING"))  {
			_blockAllOutgoing.setChecked(true);
		} else if(outgoingValue.equals("BLOCK_ALL_OUTGOING_FROM_LIST")) {
			_blockOutgoingFromList.setChecked(true);
		} else if(outgoingValue.equals("ALLOW_ALL_OUTGOING_FROM_LIST")) {
			_allowOutgoingFromList.setChecked(true);
		} else if(outgoingValue.equals("REMOVE_CALL_BLOCKING")) 	{
			_cancelAllOutgoingBlock.setChecked(true);
		} else 	{
			_cancelAllOutgoingBlock.setChecked(true);
		}
		
	}
	
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {		
		case R.id.blockAllInoming:
			editor.putString("INCOMING_BLOCK_MODE", "BLOCK_ALL_INCOMING");
			editor.commit();
			break;			
		case R.id.blockIncomingFromList:
			editor.putString("INCOMING_BLOCK_MODE", "BLOCK_ALL_INCOMING_FROM_LIST"); 
			editor.commit();
			break;
		case R.id.allowIncomingFromList:
			editor.putString("INCOMING_BLOCK_MODE", "ALLOW_ALL_INCOMING_FROM_LIST"); 
			editor.commit();
			break;
		case R.id.cancelIncomingBlocker:
			editor.putString("INCOMING_BLOCK_MODE", "REMOVE_CALL_BLOCKING"); 
			editor.commit();
			break;			
		case R.id.blockAllOutgoing:
			editor.putString("OUTGOING_BLOCK_MODE", "BLOCK_ALL_OUTGOING");
			editor.commit();
			break;			
		case R.id.blockOutgoingFromList:
			editor.putString("OUTGOING_BLOCK_MODE", "BLOCK_ALL_OUTGOING_FROM_LIST"); 
			editor.commit();
			break;
		case R.id.allowOutgoingFromList:
			editor.putString("OUTGOING_BLOCK_MODE", "ALLOW_ALL_OUTGOING_FROM_LIST"); 
			editor.commit();
			break;
		case R.id.cancelOutgoingBlocker:
			editor.putString("OUTGOING_BLOCK_MODE", "REMOVE_CALL_BLOCKING"); 
			editor.commit();
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings: 
			Intent i=new Intent(this, NumberList.class);
			startActivity(i);
			break;
		case R.id.menu_show: 
			Intent ii=new Intent(this, DBListActivity.class);
			startActivity(ii);
			break;
		}
		return true;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnAdd:
			Intent i=new Intent(this, NumberList.class);
			startActivity(i);
			break;
		case R.id.btnShow:
			Intent ii=new Intent(this, DBListActivity.class);
			startActivity(ii);
			break;
		default:
			break;
		}
	}
}
