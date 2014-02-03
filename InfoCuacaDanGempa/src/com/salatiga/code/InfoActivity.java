package com.salatiga.code;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Class ini adalah tampilan Info aplikasi
 * @author Hendro Steven Tampake
 * @version 1.0
 *
 */
public class InfoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		WebView webView = (WebView)findViewById(R.id.webViewInfo);
		webView.loadUrl("file:///android_asset/page/info.html");
		//get action bar
		ActionBar actionBar = getActionBar();
		//Enabling Up / Back navigation
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
}
