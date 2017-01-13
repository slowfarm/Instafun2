package eva.android.com.instafun2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;
import java.util.concurrent.ExecutionException;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.dataSources.ParseTask;


public class MainActivity extends AppCompatActivity {

    WebView mWebView;
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
        setContentView(R.layout.activity_main);
//        mWebView = (WebView)findViewById(R.id.web_view);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.setWebViewClient(new MyWebViewClient());
//        mWebView.loadUrl(url);
        String url = "";
        try {
            url = new ParseTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(url);
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
//        finish();

    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (url.contains("token=")) {
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                return true; // Handle By application itself
            } else {
                view.loadUrl(url);
                return true;
            }
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(mWebView, url);
            mWebView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(javaScript);
                }
            }, 500);
        }
    }
}
