package com.jdxy.hardware;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class AppStart extends Activity implements OnClickListener {

    public static int nPrintWidth = 384;
    public static boolean bCutter = false;
    public static boolean bDrawer = false;
    public static boolean bBeeper = true;
    public static int nPrintCount = 1;
    public static int nCompressMethod = 0;
    public static boolean bAutoPrint = false;
    public static int nPrintContent = 0;

    private RadioButton
            radio58, radio80,
            radioPrintCount1, radioPrintCount10, radioPrintCount100, radioPrintCount1000,
            radioPrintContentS, radioPrintContentM, radioPrintContentL;
    private CheckBox chkCutter, chkDrawer, chkBeeper, chkPictureCompress, chkAutoPrint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_private);

        SortedMap<String, Charset> map = Charset.availableCharsets();
        Set<String> keys = map.keySet();
        if (keys != null) {
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                Object key = iterator.next();
                Log.i("AppStart", "key " + key);
            }
        }

        /* 启动WIFI */
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        switch (wifiManager.getWifiState()) {
            case WifiManager.WIFI_STATE_DISABLED:
                wifiManager.setWifiEnabled(true);
                break;
            default:
                break;
        }

        /* 启动蓝牙 */
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null != adapter) {
            if (!adapter.isEnabled()) {
                if (!adapter.enable()) {
                    finish();
                    return;
                }
            }
        }

        //checkBluetoothPermission();
        if (!isGpsEnable(this))
            enableGps(this);

        radio58 = (RadioButton) findViewById(R.id.radioButtonTicket58);
        radio80 = (RadioButton) findViewById(R.id.radioButtonTicket80);
        radioPrintCount1 = (RadioButton) findViewById(R.id.radioButtonPrintCount1);
        radioPrintCount10 = (RadioButton) findViewById(R.id.radioButtonPrintCount10);
        radioPrintCount100 = (RadioButton) findViewById(R.id.radioButtonPrintCount100);
        radioPrintCount1000 = (RadioButton) findViewById(R.id.radioButtonPrintCount1000);
        radioPrintContentS = (RadioButton) findViewById(R.id.radioButtonPrintContentS);
        radioPrintContentM = (RadioButton) findViewById(R.id.radioButtonPrintContentM);
        radioPrintContentL = (RadioButton) findViewById(R.id.radioButtonPrintContentL);
        chkCutter = (CheckBox) findViewById(R.id.checkBoxCutter);
        chkDrawer = (CheckBox) findViewById(R.id.checkBoxDrawer);
        chkBeeper = (CheckBox) findViewById(R.id.checkBoxBeeper);
        chkPictureCompress = (CheckBox) findViewById(R.id.checkBoxPictureCompress);
        chkAutoPrint = (CheckBox) findViewById(R.id.checkBoxAutoPrint);

        findViewById(R.id.btnTestBT).setOnClickListener(this);
        findViewById(R.id.btnTestBLE).setOnClickListener(this);
        findViewById(R.id.btnTestUSB).setOnClickListener(this);
        findViewById(R.id.btnTestNET).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (radio58.isChecked())
            nPrintWidth = 384;
        else if (radio80.isChecked())
            nPrintWidth = 576;

        if (radioPrintCount1.isChecked())
            nPrintCount = 1;
        else if (radioPrintCount10.isChecked())
            nPrintCount = 10;
        else if (radioPrintCount100.isChecked())
            nPrintCount = 100;
        else if (radioPrintCount1000.isChecked())
            nPrintCount = 1000;

        if (radioPrintContentS.isChecked())
            nPrintContent = 1;
        else if (radioPrintContentM.isChecked())
            nPrintContent = 2;
        else if (radioPrintContentL.isChecked())
            nPrintContent = 3;

        bCutter = chkCutter.isChecked();
        bDrawer = chkDrawer.isChecked();
        bBeeper = chkBeeper.isChecked();

        nCompressMethod = chkPictureCompress.isChecked() ? 1 : 0;
        bAutoPrint = chkAutoPrint.isChecked();

        switch (v.getId()) {
            case R.id.btnTestBT: {
                Intent intent = new Intent(AppStart.this, SearchBTActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.btnTestBLE: {
                Intent intent = new Intent(AppStart.this, SearchBLEActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.btnTestUSB: {
                Intent intent = new Intent(AppStart.this, ConnectUSBActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.btnTestNET: {
                Intent intent = new Intent(AppStart.this, ConnectIPActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

	/*
	private void checkBluetoothPermission() {
		if (Build.VERSION.SDK_INT >= 23) {
			//校验是否已具有模糊定位权限
			if (ContextCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_COARSE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
			}
		}
	}*/

    private boolean isGpsEnable(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    private void enableGps(final Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 0);
    }

}
