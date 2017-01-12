package eva.android.com.instafun2.dataSources;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserPhotoTask extends AsyncTask<Void, Void, String> {

    private String username;
    private String token;

    public UserPhotoTask(String username, String token) {
        this.username = username;
        this.token = token;
    }
    @Override
    protected String doInBackground(Void... params) {
        String request = "https://api.instagram.com/v1/users/{user-id}/?access_token=ACCESS-TOKEN";
        String result = "";
        try {
            URL url = new URL(request);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}