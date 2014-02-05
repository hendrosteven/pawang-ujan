package com.salatiga.code;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * Class ini adalah tampilan untuk tab gempa
 * 
 * @author Hendro Steven Tampake
 * @version 1.0
 * 
 */
public class GempaFragment extends Fragment {

	private static final String URL = "http://data.bmkg.go.id/gempadirasakan.xml";
	// private static final String URL =
	// "http://data.bmkg.go.id/gempaterkini.xml";

	private ListView listGempa;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.gempa, container, false);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		loadPage();
		listGempa = (ListView) getView().findViewById(R.id.listGempa);
		listGempa.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final Gempa itemGempa = (Gempa) parent
						.getItemAtPosition(position);
				Intent mapActivity = new Intent(getView().getContext(),
						MapActivity.class);
				mapActivity.putExtra("tanggal", itemGempa.getTanggal());
				mapActivity.putExtra("latitude", itemGempa.getLatitude());
				mapActivity.putExtra("longitude", itemGempa.getLongitude());
				mapActivity.putExtra("magnitude", itemGempa.getMagnitude());
				mapActivity.putExtra("kedalaman", itemGempa.getKedalaman());
				mapActivity.putExtra("description", itemGempa.getKeterangan());
				mapActivity.putExtra("dirasakan", itemGempa.getDirasakan());
				startActivity(mapActivity);
			}

		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void loadPage() {
		if (((MainActivity.sPref.equals(MainActivity.ANY)) && (MainActivity.wifiConnected || MainActivity.mobileConnected))
				|| ((MainActivity.sPref.equals(MainActivity.WIFI)) && (MainActivity.wifiConnected))) {
			// AsyncTask subclass
			new DownloadXmlTask().execute(URL);
		}
	}

	// Implementation of AsyncTask used to download XML feed from
	// stackoverflow.com.
	private class DownloadXmlTask extends AsyncTask<String, Void, List<Gempa>> {

		protected ProgressBar progress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = (ProgressBar) getView().findViewById(R.id.progressBar);
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected List<Gempa> doInBackground(String... urls) {
			try {
				return loadXmlFromNetwork(urls[0]);
			} catch (IOException e) {
				return new ArrayList<Gempa>();
			} catch (XmlPullParserException e) {
				return new ArrayList<Gempa>();
			}
		}

		@Override
		protected void onPostExecute(List<Gempa> result) {

			listGempa.setAdapter(new GempaListAdapter(getView().getContext(),
					result));
			progress.setVisibility(View.INVISIBLE);
			
		}
	}

	private List<Gempa> loadXmlFromNetwork(String urlString)
			throws XmlPullParserException, IOException {
		InputStream stream = null;
		GempaXmlParser gempaParser = new GempaXmlParser();
		List<Gempa> gempas = null;

		try {
			stream = downloadUrl(urlString);
			gempas = gempaParser.parse(stream);			
		} finally {
			if (stream != null) {
				stream.close();
			}
		}

		return gempas;
	}

	// Given a string representation of a URL, sets up a connection and gets
	// an input stream.
	private InputStream downloadUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		InputStream stream = conn.getInputStream();
		return stream;
	}
}
