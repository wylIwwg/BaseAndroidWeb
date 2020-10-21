package com.jdxy.hardware;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lvrenyang.io.BLEPrinting;
import com.lvrenyang.io.base.IOCallBack;
import com.lvrenyang.io.Pos;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SearchBLEActivity extends Activity implements OnClickListener, IOCallBack, LeScanCallback {

	private LinearLayout linearlayoutdevices;
	private ProgressBar progressBarSearchStatus;

	Button btnSearch,btnDisconnect,btnPrint;
	SearchBLEActivity mActivity;
	
	ExecutorService es = Executors.newScheduledThreadPool(30);
	Pos mPos = new Pos();
	BLEPrinting mBt = new BLEPrinting();
	
	private static String TAG = "SearchBTActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchbt);

		mActivity = this;
		
		progressBarSearchStatus = (ProgressBar) findViewById(R.id.progressBarSearchStatus);
		linearlayoutdevices = (LinearLayout) findViewById(R.id.linearlayoutdevices);
		
		btnSearch = (Button) findViewById(R.id.buttonSearch);
		btnDisconnect = (Button) findViewById(R.id.buttonDisconnect);
		btnPrint = (Button) findViewById(R.id.buttonPrint);
		btnSearch.setOnClickListener(this);
		btnDisconnect.setOnClickListener(this);
		btnPrint.setOnClickListener(this);
		btnSearch.setEnabled(true);
		btnDisconnect.setEnabled(false);
		btnPrint.setEnabled(false);
		
		mPos.Set(mBt);
		mBt.SetCallBack(this);
	}

	@Override
	protected void onDestroy() {

		StopScan();
		btnDisconnect.performClick();
		
		super.onDestroy();
	}

	private void StopScan()
	{
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (null != adapter) 
		{
			if (adapter.isEnabled())
			{
				progressBarSearchStatus.setIndeterminate(false);
				adapter.stopLeScan(this);
			}
		}
	}
	
	private void StartScan()
	{
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (null != adapter) 
		{
			if (adapter.isEnabled())
			{
				linearlayoutdevices.removeAllViews();
				progressBarSearchStatus.setIndeterminate(true);
				adapter.startLeScan(this);
			}
		}
	}
	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.buttonSearch: {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if (null == adapter) {
				finish();
				break;
			}

			if (!adapter.isEnabled()) {
				if (adapter.enable()) {
					while (!adapter.isEnabled())
						;
					Log.v(TAG, "Enable BluetoothAdapter");
				} else {
					finish();
					break;
				}
			}
			
			StartScan();
			break;
		}
		
		case R.id.buttonDisconnect:
			es.submit(new TaskClose(mBt));
			break;

		case R.id.buttonPrint:
			btnPrint.setEnabled(false);
			es.submit(new TaskPrint(mPos));
			break;
		}
	}


	@Override
	public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				if (device == null)
					return;
				final String address = device.getAddress();
				String name = device.getName();
				if (name == null)
					name = "BT";
				else if (name.equals(address))
					name = "BT";
				Button button = new Button(mActivity);
				button.setText(name + ": " + address);
				
				for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
				{
					Button btn = (Button)linearlayoutdevices.getChildAt(i);
					if(btn.getText().equals(button.getText()))
					{
						return;
					}
				}
				
				button.setGravity(Gravity.CENTER_VERTICAL
						| Gravity.LEFT);
				button.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(mActivity, "Connecting...", Toast.LENGTH_SHORT).show();
						btnSearch.setEnabled(false);
						linearlayoutdevices.setEnabled(false);
						for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
						{
							Button btn = (Button)linearlayoutdevices.getChildAt(i);
							btn.setEnabled(false);
						}
						btnDisconnect.setEnabled(false);
						btnPrint.setEnabled(false); 
						mActivity.StopScan();
						es.submit(new TaskOpen(mBt,address,mActivity));
						//es.submit(new TaskTest(mPos, mBt, address, mActivity));
					}
				});
				button.getBackground().setAlpha(100);
				linearlayoutdevices.addView(button);
			}
		});
	}

	public class TaskTest implements Runnable
	{
		Pos pos = null;
		BLEPrinting bt = null;
		String address = null;
		Context context = null;
		
		public TaskTest(Pos pos, BLEPrinting bt, String address, Context context)
		{
			this.pos = pos;
			this.bt = bt;
			this.address = address;
			this.context = context;
			pos.Set(bt);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			for(int i = 0; i < 1000; ++i)
			{
				long beginTime = System.currentTimeMillis();
				if(bt.Open(address, context))
				{
					long endTime = System.currentTimeMillis();
					pos.POS_S_Align(0);
					pos.POS_S_TextOut(i+ " " + "Open   UsedTime:" + (endTime - beginTime) + "\r\n", 0, 0, 0, 0, 0);
					beginTime = System.currentTimeMillis();
					int ticketResult = pos.POS_TicketSucceed(i, 30000);
					endTime = System.currentTimeMillis();
					pos.POS_S_TextOut(i+ " " + "Ticket UsedTime:" + (endTime - beginTime) + " " + (ticketResult == 0 ? "Succeed" : "Failed") +  "\r\n", 0, 0, 0, 0, 0);
					pos.POS_Beep(1, 500);
					byte[] status = new byte[1];
					pos.POS_QueryStatus(status, 3000, 2);
					bt.Close();
				}
			}
		}
	}
	
	public static class TaskOpen implements Runnable
	{
		BLEPrinting bt = null;
		String address = null;
		Context context = null;
		
		public TaskOpen(BLEPrinting bt, String address, Context context)
		{
			this.bt = bt;
			this.address = address;
			this.context = context;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			bt.Open(address, context);
		}
	}
	
	static int dwWriteIndex = 1;
	public class TaskPrint implements Runnable
	{
		Pos pos = null;
		
		public TaskPrint(Pos pos)
		{
			this.pos = pos;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			final int bPrintResult = Prints.PrintTicket(getApplicationContext(), pos, AppStart.nPrintWidth, AppStart.bCutter, AppStart.bDrawer, AppStart.bBeeper, AppStart.nPrintCount, AppStart.nPrintContent, AppStart.nCompressMethod);
			final boolean bIsOpened = pos.GetIO().IsOpened();
			
			mActivity.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity.getApplicationContext(), (bPrintResult == 0) ? getResources().getString(R.string.printsuccess) : getResources().getString(R.string.printfailed) + " " + Prints.ResultCodeToString(bPrintResult), Toast.LENGTH_SHORT).show();
					mActivity.btnPrint.setEnabled(bIsOpened);
				}
			});

		}
	}
	
	public static class TaskClose implements Runnable
	{
		BLEPrinting bt = null;
		
		public TaskClose(BLEPrinting bt)
		{
			this.bt = bt;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			bt.Close();
		}
		
	}
	
	@Override
	public void OnOpen() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				btnDisconnect.setEnabled(true);
				btnPrint.setEnabled(true);
				btnSearch.setEnabled(false);
				linearlayoutdevices.setEnabled(false);
				for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
				{
					Button btn = (Button)linearlayoutdevices.getChildAt(i);
					btn.setEnabled(false);
				}
				Toast.makeText(mActivity, "Connected", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void OnOpenFailed() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				btnDisconnect.setEnabled(false);
				btnPrint.setEnabled(false);
				btnSearch.setEnabled(true);
				linearlayoutdevices.setEnabled(true);
				for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
				{
					Button btn = (Button)linearlayoutdevices.getChildAt(i);
					btn.setEnabled(true);
				}
				Toast.makeText(mActivity, "Failed", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	public void OnClose() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				btnDisconnect.setEnabled(false);
				btnPrint.setEnabled(false);
				btnSearch.setEnabled(true);
				linearlayoutdevices.setEnabled(true);
				for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
				{
					Button btn = (Button)linearlayoutdevices.getChildAt(i);
					btn.setEnabled(true);
				}
			}
		});
	}

}
