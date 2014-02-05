package com.salatiga.code;

import java.util.Scanner;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
	private String tanggal;
	private double latitude;
	private double longitude;	
	private String magnitude;
	private String kedalaman;
	private String dirasakan;
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
		tanggal = sender.getExtras().getString("tanggal");		
		latitude = Double.valueOf(sender.getExtras().getString("latitude"));
		longitude = Double.valueOf(sender.getExtras().getString("longitude"));
		magnitude = sender.getExtras().getString("magnitude");
		kedalaman = sender.getExtras().getString("kedalaman");		
		description = sender.getExtras().getString("description");
		dirasakan = sender.getExtras().getString("dirasakan");

		try {
			// Loading map
			initilizeMap();

		} catch (Exception e) {
			Toast.makeText(this, "Gagal menampilkan peta", Toast.LENGTH_LONG).show();
		}

	}

	
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
			
			// Setting a custom info window adapter for the google map
	        googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
	 
	            private Scanner scanner;

				// Use default InfoWindow frame
	            @Override
	            public View getInfoWindow(Marker arg0) {
	                return null;
	            }
	 
	            // Defines the contents of the InfoWindow
	            @Override
	            public View getInfoContents(Marker arg0) {
	 
	                // Getting view from the layout file map_info.xml
	                View view = getLayoutInflater().inflate(R.layout.map_info, null);	 
	                // Getting reference to the TextView 
	                TextView txtWilayah =  (TextView) view.findViewById(R.id.txtWilayah);
	                TextView txtTanggalJam = (TextView)view.findViewById(R.id.txtTanggalJam);		
	                TextView txtMagnitude = (TextView)view.findViewById(R.id.txtMagnitude);
	                TextView txtSr = (TextView)view.findViewById(R.id.txtSr);
	                
	                txtWilayah.setText(description);
	        		txtTanggalJam.setText(tanggal);
	        		scanner = new Scanner(magnitude);
	        		double magnitude = scanner.nextDouble();
	        		
	        		txtMagnitude.setText(magnitude+"");
	        		if(magnitude<3.9){
	        			txtMagnitude.setTextColor(view.getResources().getColor(R.color.level1));
	        			txtSr.setTextColor(view.getResources().getColor(R.color.level1));
	        		}else if(magnitude>=4.0 && magnitude<=5.9){
	        			txtMagnitude.setTextColor(view.getResources().getColor(R.color.level2));
	        			txtSr.setTextColor(view.getResources().getColor(R.color.level2));
	        		}else if(magnitude>=6.0 && magnitude<=6.9){
	        			txtMagnitude.setTextColor(view.getResources().getColor(R.color.level3));
	        			txtSr.setTextColor(view.getResources().getColor(R.color.level3));
	        		}else if(magnitude>=6.9){
	        			txtMagnitude.setTextColor(view.getResources().getColor(R.color.level4));
	        			txtSr.setTextColor(view.getResources().getColor(R.color.level4));
	        		}
	 
	                // Returning the view containing InfoWindow contents
	                return view;
	 
	            }
	        });
			
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
