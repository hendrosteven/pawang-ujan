package com.salatiga.code;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Class untuk tampilan lokasi gempa
 * @author Hendro Steven Tampake
 * @version 1.0
 *
 */
public class MapActivity extends Activity {

	// Google Map
	private GoogleMap googleMap;
	private double latitude;
	private double longitude;
	private String description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		// get action bar
		ActionBar actionBar = getActionBar();
		// Enabling Up / Back navigation
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//ambil data param
		Intent sender = getIntent();
		latitude = Double.valueOf(sender.getExtras().getString("latitude"));
		longitude = Double.valueOf(sender.getExtras().getString("longitude"));
		description = sender.getExtras().getString("description");

		try {
			// Loading map
			initilizeMap();

		} catch (Exception e) {
			Toast.makeText(this, "Gagal menampilkan peta", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// create marker
			MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(description);			 
			// adding marker
			googleMap.addMarker(marker);
			CameraPosition cameraPosition = new CameraPosition.Builder().target(
	                new LatLng(latitude, longitude)).zoom(6).build();
	 
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Maaf! tidak dapat menampilkan peta", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	

}
