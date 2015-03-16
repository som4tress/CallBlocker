package com.zebra.SES.CallManager;

import java.util.ArrayList;




import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DBListActivity extends android.app.ListActivity  implements OnItemLongClickListener{
	/** Called when the activity is first created. */
	private static final int ACTIVITY_CREATE=0;
	private static final int ACTIVITY_EDIT=1;
	private WhiteListDB mDbAdapter;
	private   ArrayList<String> numberList=null;
	private   ArrayList<String> idList=null;
	// private int[] position=new int[100];
	// private String[] titleItems=new String[100];
	private int clickItem;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		numberList = new ArrayList<String>();
		idList = new ArrayList<String>();
		mDbAdapter=new WhiteListDB(this);
		mDbAdapter.open();

		registerForContextMenu(getListView());
		displayList();
	}
	public void displayList()
	{   
		Cursor c=mDbAdapter.fetchAllNumberss();
		numberList.clear();
		idList.clear();
		startManagingCursor(c);
		c.moveToFirst();
		//NEW
		if (c .moveToFirst()) {

			while (c.isAfterLast() == false) {
				String  id= c.getString(c.getColumnIndex(WhiteListDB.KEY_ROWID)); 	

				String name = c.getString(c
						.getColumnIndex(WhiteListDB.KEY_TITLE));
				numberList.add(name);
				idList.add(id);

				c.moveToNext();
			}
		}
		//
		ArrayAdapter<String> ad =new ArrayAdapter<String>(this, R.layout.row, R.id.text1, numberList);  
		setListAdapter(ad);

	}
	@Override
	public int getSelectedItemPosition() {
		// TODO Auto-generated method stub
		return super.getSelectedItemPosition();
	}
	//
	// list item selected
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// clickItem=position;
		clickItem=Integer.parseInt(idList.get(position)); 
		Toast.makeText(getBaseContext(), "number  "+clickItem, Toast.LENGTH_SHORT).show();

	}
	// creating menu 


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater mi = getMenuInflater(); 
		mi.inflate(R.menu.items_menu, menu); 
	}
	//
	//
	@Override
	public boolean onContextItemSelected(MenuItem item) { 

		switch(item.getItemId()) {
		case R.id.menu_delete:
			Boolean bb = mDbAdapter.deleteEntry(clickItem + 1);                                  
			displayList();
			return true;
		case R.id.menu_cancel:
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	public boolean onItemLongClick(AdapterView<?> arg, View arg1, int pos,
			long id) {
		clickItem=Integer.parseInt(idList.get(pos)); 
		Toast.makeText(getBaseContext(), "number  "+clickItem, Toast.LENGTH_SHORT).show();

		// TODO Auto-generated method stub
		return true;
	}

}