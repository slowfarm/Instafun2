package eva.android.com.instafun2.dataSources;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import eva.android.com.instafun2.activities.MainActivity;


public class UserDataTask extends AsyncTask<Void, String, String>{

    private String username;
    private String maxId;
    private Context context;

    public UserDataTask(String username, String maxId, Context context) {
        this.username = username;
        this.maxId = maxId;
        this.context = context;
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
            return buffer.toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "uhex";
        } catch (FileNotFoundException  e) {
            e.printStackTrace();
            return "fnfex";
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}