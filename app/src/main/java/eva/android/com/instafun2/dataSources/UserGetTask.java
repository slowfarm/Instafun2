package eva.android.com.instafun2.dataSources;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import eva.android.com.instafun2.data.JSONParser;
import eva.android.com.instafun2.data.Users;


public class UserGetTask extends AsyncTask<Void, Void, ArrayList<Users>> {

    private String resultJson = "";
    ArrayList<Users> users = new ArrayList<>();
    private CharSequence query;
    private String token;

    public UserGetTask(CharSequence query, String token) {
        this.query = query;
        this.token = token;
    }
    @Override
    protected ArrayList<Users> doInBackground(Void... params) {
        String request = "https://api.instagram.com/v1/users/search?q="
                +query+"&access_token="+token;
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
            resultJson = buffer.toString();
            users.addAll(new JSONParser().usersParser(resultJson));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    protected void onPostExecute(ArrayList<Users> result) {
        super.onPostExecute(result);
    }
}