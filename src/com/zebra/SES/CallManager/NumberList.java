package com.zebra.SES.CallManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NumberList extends Activity implements OnClickListener{
	private WhiteListDB mDbAdapter;
	private EditText numbr;
	private Button btnAdd;
	private Button btnTree;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		mDbAdapter = new WhiteListDB(this);
		mDbAdapter.open();

		numbr=(EditText) findViewById(R.id.editNumber);
		btnAdd=(Button) findViewById(R.id.btnSave);
		btnAdd.setOnClickListener(this);

	}
	
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnSave:
			if((numbr.getText().toString()!=null)&&(numbr.getText().toString().length()>=7)) 	{	
				mDbAdapter.createWhiteListEntry(numbr.getText().toString(), "", "");	 
				mDbAdapter.close();
				finish();
			} else 	{
				Toast.makeText(getApplicationContext(), "Invalid Number !!", Toast.LENGTH_SHORT).show();
			}

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDbAdapter.close();
	}

}
