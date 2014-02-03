package com.salatiga.code;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.salatiga.code.adapter.TabsPagerAdapter;

/**
 * Class ini adalah tampilan utama
 * @author Hendro Steven Tampake
 * @version 1.0
 *
 */
public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	public static final String WIFI = "Wi-Fi";
	public static final String ANY = "Any";
	// Whether there is a Wi-Fi connection.
	public static boolean wifiConnected = false;
	// Whether there is a mobile connection.
	public static boolean mobileConnected = false;
	// Whether the display should be refreshed.
	public static boolean refreshDisplay = true;
	// The user's current network preference setting.
	public static String sPref = null;
	// The BroadcastReceiver that tracks network connectivity changes.
	private NetworkReceiver receiver = new NetworkReceiver();

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Cuaca Terkini", "Gempa Terkini" };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		// Register BroadcastReceiver to track connection changes.
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkReceiver();
		this.registerReceiver(receiver, filter);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	// Refreshes the display if the network connection and the
	// pref settings allow it.
	@Override
	public void onStart() {
		super.onStart();

		// Gets the user's network preference settings
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		// Retrieves a string value for the preferences. The second parameter
		// is the default value to use if a preference value is not found.
		sPref = sharedPrefs.getString("listPref", "Any");

		updateConnectedFlags();

		// Only loads the page if refreshDisplay is true. Otherwise, keeps
		// previous
		// display. For example, if the user has set "Wi-Fi only" in prefs and
		// the
		// device loses its Wi-Fi connection midway through the user using the
		// app,
		// you don't want to refresh the display--this would force the display
		// of
		// an error page instead of stackoverflow.com content.
		//if (refreshDisplay) {
		//	loadPage();
		//}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			this.unregisterReceiver(receiver);
		}
	}

	// Checks the network connection and sets the wifiConnected and
	// mobileConnected
	// variables accordingly.
	private void updateConnectedFlags() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
			mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
		} else {
			wifiConnected = false;
			mobileConnected = false;
		}
	}

	// Uses AsyncTask subclass to download the XML feed from stackoverflow.com.
	// This avoids UI lock up. To prevent network operations from
	// causing a delay that results in a poor user experience, always perform
	// network operations on a separate thread from the UI.
	private void loadPage() {
		if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
				|| ((sPref.equals(WIFI)) && (wifiConnected))) {
			if(viewPager.getCurrentItem()==1){
				GempaFragment gempaFragment = (GempaFragment)viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
				gempaFragment.loadPage();
			}else if(viewPager.getCurrentItem()==0){
				CuacaFragment cuacaFragment = (CuacaFragment)viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
				cuacaFragment.loadData();
			}
		} else {
			showErrorPage();
		}
	}

	// Displays an error if the app is unable to load content.
	private void showErrorPage() {
		Toast.makeText(this,
				getResources().getString(R.string.connection_error),
				Toast.LENGTH_LONG).show();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// Handles the user's menu selection.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent settingsActivity = new Intent(getBaseContext(),
					SettingsActivity.class);
			startActivity(settingsActivity);
			return true;
		case R.id.action_refresh:
			loadPage();
			return true;
		case R.id.action_about:
			Intent infoActivity = new Intent(getBaseContext(),
					InfoActivity.class);
			startActivity(infoActivity);
			return true;
		case R.id.action_exit:
			System.exit(0);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public class NetworkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

			// Checks the user prefs and the network connection. Based on the
			// result, decides
			// whether
			// to refresh the display or keep the current display.
			// If the userpref is Wi-Fi only, checks to see if the device has a
			// Wi-Fi connection.
			if (WIFI.equals(sPref) && networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// If device has its Wi-Fi connection, sets refreshDisplay
				// to true. This causes the display to be refreshed when the
				// user
				// returns to the app.
				refreshDisplay = true;
				Toast.makeText(context, R.string.wifi_connected,
						Toast.LENGTH_SHORT).show();

				// If the setting is ANY network and there is a network
				// connection
				// (which by process of elimination would be mobile), sets
				// refreshDisplay to true.
			} else if (ANY.equals(sPref) && networkInfo != null) {
				refreshDisplay = true;

				// Otherwise, the app can't download content--either because
				// there is no network
				// connection (mobile or Wi-Fi), or because the pref setting is
				// WIFI, and there
				// is no Wi-Fi connection.
				// Sets refreshDisplay to false.
			} else {
				refreshDisplay = false;
				Toast.makeText(context, R.string.lost_connection,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
