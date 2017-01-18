package eva.android.com.instafun2.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.data.Database;

public class SplashActivity extends AppCompatActivity {

    public static Database helper;

    WebView mWebView;
    Snackbar snackbar;
    ProgressBar progressBar;

    String clientId = "32d863705668431e9547d9ff01963904";
    String redirectUri = "http://slowfarm.github.io";
    String url = "https://www.instagram.com/oauth/authorize?client_id="
            +clientId+"&redirect_uri="
            +redirectUri+"&scope=basic+public_content&response_type=token";
    String login = "kypopthblu_poma";
    String password = "suzumo15";
    final String javaScript = "javascript: {" +
            "document.getElementById('id_username').value = '"+login +"';" +
            "document.getElementById('id_password').value = '"+password+"';" +
            "document.getElementsByClassName('button-green')[0].click();};";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        helper = Database.getInstance(this);

        snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "No connection",
                Snackbar.LENGTH_INDEFINITE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isNetworkDisconnected()){
                    snackbar.show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView = new WebView(SplashActivity.this);
                        mWebView.getSettings().setJavaScriptEnabled(true);
                        mWebView.getSettings().setDomStorageEnabled(true);
                        mWebView.setWebViewClient(new MyWebViewClient());
                        mWebView.loadUrl(url);
                    }
                });
            }}).start();
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (url.contains("access_token=")) {
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class)
                        .putExtra("url", url);
                startActivity(intent);
                finish();
                return true;
            } else {
                view.loadUrl(url);
                return true;
            }
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(url.contains("force_classic_login="))
                view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(javaScript);
                }
            }, 500);
        }
    }

    private boolean isNetworkDisconnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() == null;
    }
}
