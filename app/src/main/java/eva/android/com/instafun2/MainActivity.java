package eva.android.com.instafun2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends AppCompatActivity {

    WebView mWebView;
    String clientId = "32d863705668431e9547d9ff01963904";
    String redirectUri = "http://slowfarm.github.io";
    String url = "https://api.instagram.com/oauth/authorize/?client_id="
            +clientId+"&redirect_uri="
            +redirectUri+"&response_type=code";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView)findViewById(R.id.web_view);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (url.substring(0,redirectUri.length()).equals(redirectUri)) {
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return true; // Handle By application itself
            } else {
                view.loadUrl(url);
                return true;
            }
        }
    }
}
