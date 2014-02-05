package com.salatiga.code;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * Class ini adalah Tampilan untuk tab cuaca
 * @author Hendro Steven Tampake
 * @version 1.0
 */

public class CuacaFragment extends Fragment {

	private static final String URL_WEATHER = "http://api.openweathermap.org/data/2.5/weather";
	private static final String URL_DAILY = "http://api.openweathermap.org/data/2.5/forecast/daily";
	private static final String IMG_URL = "http://openweathermap.org/img/w/";

	private double latitude;
	private double longitude;
	private GPSTracker gps = null;
	private TextView cariKota;
	private Button cariBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.cuaca, container, false);		
		cariBtn = (Button)rootView.findViewById(R.id.btnCari);
		cariBtnClick();

		return rootView;
	}

	private void cariBtnClick() {
		cariBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadData();				
			}
		});
		
	}

	@Override
	public void onStart() {
		super.onStart();
		loadData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void loadData() {
		if (((MainActivity.sPref.equals(MainActivity.ANY)) && (MainActivity.wifiConnected || MainActivity.mobileConnected))
				|| ((MainActivity.sPref.equals(MainActivity.WIFI)) && (MainActivity.wifiConnected))) {
			// AsyncTask subclass
			// get location
			gps = new GPSTracker(getView().getContext());
			// check if GPS enabled
			if (gps.canGetLocation()) {
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
			} else {
				gps.showSettingsAlert();
			}
			cariKota = (TextView) getView().findViewById(R.id.txtKota);
			String strKota = cariKota.getText().toString().trim();
			new DownloadJsonTask(latitude, longitude, strKota).execute();
		}
	}

	// Implementation of AsyncTask used to download XML feed from
	// stackoverflow.com.
	private class DownloadJsonTask extends AsyncTask<Void, Void, String> {

		protected ProgressBar progress;
		private double lat;
		private double lon;
		private String city;

		public DownloadJsonTask(double lat, double lon, String kota) {
			this.lat = lat;
			this.lon = lon;
			this.city = kota;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = (ProgressBar) getView().findViewById(
					R.id.progressBarCuaca);
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Void... loc) {
			String html = "";
			try {
				//get current weather		
				html = parseJSONWeather(getJSONWeather(city,lat,lon));
				html += parseJSONDaily(getJSONDaily(city,lat,lon));

			} catch (Exception e) {
				html = "Gagal mendapatkan informasi cuaca...";
			}
			return html;
		}

		@Override
		protected void onPostExecute(String result) {
			progress.setVisibility(View.INVISIBLE);
			// Displays the HTML string in the UI via a WebView
			WebView myWebView = (WebView) getView().findViewById(
					R.id.webViewCuaca);
			myWebView.loadData(result, "text/html", null);
		}
	}
	
	private String getJSONDaily(String city, double lat, double lon){
		String jsonStr = "";
		return jsonStr;
	}
	
	private String parseJSONDaily(String jsonStr) {
		StringBuffer strBuffer = new StringBuffer();
		// parse json string
		if (jsonStr != null) {
			try {
				
			} catch (Exception e) {
				strBuffer.append("Gagal mendapatkan informasi cuaca harian...");
			}
		} else {
			strBuffer.append("Gagal mendapatkan informasi cuaca harian... ");
		}
		return strBuffer.toString();
	}

	
	private String getJSONWeather(String city,double lat, double lon){
		ServiceHandler sh = new ServiceHandler();

		// params setup
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (city.length() > 0) {
			params.add(new BasicNameValuePair("q", city));
		} else {
			params.add(new BasicNameValuePair("lat", String
					.valueOf(lat)));
			params.add(new BasicNameValuePair("lon", String
					.valueOf(lon)));
		}
		params.add(new BasicNameValuePair("mode", "json"));
		params.add(new BasicNameValuePair("units", "metric"));
		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall(URL_WEATHER, ServiceHandler.GET,
				params);
		return jsonStr;
	}

	private String parseJSONWeather(String jsonStr) {
		StringBuffer strBuffer = new StringBuffer();
		// parse json string
		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				String city = jsonObj.getString("name");
				JSONObject main = jsonObj.getJSONObject("main");
				String temp = main.getString("temp");
				String humidity = main.getString("humidity") + " %";
				String pressure = main.getString("pressure") + " hpa";
				JSONArray weather = jsonObj.getJSONArray("weather");
				String cloudiness = weather.getJSONObject(0).getString(
						"description");
				String icon = weather.getJSONObject(0).getString("icon");
				strBuffer.append("<html><body>");
				strBuffer.append("<br/><center>");
				strBuffer
						.append("<table width=\'70%\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\'>")
						.append("<tr>")
						.append("<td colspan=\'2\' align=\'center\' valign=\'middle\'>")
						.append("<h1>" + city + "</h1>").append("</td>")
						.append("</tr>").append("<tr>")
						.append("<td align=\'right\' valign=\'middle\'>")
						.append("<img src=\'" + IMG_URL + icon + "\'>")
						.append("</td>")
						.append("<td align=\'left\' valign=\'middle\'>")
						.append("<h4>" + temp + " &deg;C</h4>").append("</td>")
						.append("</tr>").append("</table>");
				strBuffer.append("<table>");
				strBuffer.append("<tr>").append("<td>").append("Cloudiness")
						.append("</td>").append("<td>:</td>").append("<td>")
						.append(cloudiness).append("</td>").append("</tr>");
				strBuffer.append("<tr>").append("<td>").append("Pressure")
						.append("</td>").append("<td>:</td>").append("<td>")
						.append(pressure).append("</td>").append("</tr>");
				strBuffer.append("<tr>").append("<td>").append("Humidity")
						.append("</td>").append("<td>:</td>").append("<td>")
						.append(humidity).append("</td>").append("</tr>");
				strBuffer.append("</table>");
				strBuffer.append("</center>");
				strBuffer.append("</body></html>");
			} catch (Exception e) {
				strBuffer.append("Gagal mendapatkan informasi cuaca...");
			}
		} else {
			strBuffer.append("Gagal mendapatkan informasi cuaca... ");
		}
		return strBuffer.toString();
	}
	
	

}
