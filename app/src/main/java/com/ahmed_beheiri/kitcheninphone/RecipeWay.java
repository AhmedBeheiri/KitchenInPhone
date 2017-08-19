package com.ahmed_beheiri.kitcheninphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeWay extends AppCompatActivity {
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.toolbar_actionbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipeway);
        ButterKnife.bind(this);
        setupToolbar(getString(R.string.app_name));
        Intent i = getIntent();
        String sourceurl = i.getStringExtra("sourceurl");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(sourceurl);

    }
    private void setupToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
    }

}
