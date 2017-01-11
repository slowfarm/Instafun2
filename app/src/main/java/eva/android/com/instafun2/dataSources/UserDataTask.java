package eva.android.com.instafun2.dataSources;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class UserDataTask extends AsyncTask<Void, Void, String> {

    private String resultJson;
    private String username;
    private String maxId;

    public UserDataTask(String username, String maxId) {
        this.username = username;
        this.maxId = maxId;
    }
    @Override
    protected String doInBackground(Void... params) {
        try {
            String request = "https://www.instagram.com/"+username+"/media/?max_id="+maxId;
            URL url = new URL(request);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
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