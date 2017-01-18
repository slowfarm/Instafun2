package eva.android.com.instafun2.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.data.Database;

public class SplashActivity extends AppCompatActivity {

    public static Database helper;

    private WebView mWebView;
    private Snackbar snackbar;

    String request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        helper = Database.getInstance(this);

        request = "https://www.instagram.com/oauth/authorize?client_id="
                + getString(R.string.client_id)+"&redirect_uri="
                +getString(R.string.redirect_uri)+"&scope=basic+public_content&response_type=token";

        snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "No connection",
                Snackbar.LENGTH_INDEFINITE);

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
                        mWebView.loadUrl(request);
                    }
                });
            }}).start();
    }

    private class MyWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
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
        //waiting until page load
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(url.contains("force_classic_login="))
                view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(getString(R.string.javaScript));
                }
            }, 500);
        }
    }

    //checking the network status
    private boolean isNetworkDisconnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() == null;
    }
}
