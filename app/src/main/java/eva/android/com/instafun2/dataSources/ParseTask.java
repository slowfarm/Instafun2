package eva.android.com.instafun2.dataSources;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class ParseTask extends AsyncTask<Void, Void, String> {
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
    protected String doInBackground(Void... params) {
        String resultJson = "";
        try {
            String request = url;
            URL url = new URL(request);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            urlConnection.getInputStream();
            System.out.println(urlConnection.getURL());
            OutputStreamWriter outputStream = new OutputStreamWriter(urlConnection.getOutputStream());
            outputStream.write(javaScript);
            outputStream.close();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            resultJson = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
