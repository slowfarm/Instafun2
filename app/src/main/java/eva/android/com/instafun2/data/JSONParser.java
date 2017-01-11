package eva.android.com.instafun2.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eva.android.com.instafun2.data.Users;

/**
 * Created by u102 on 11.01.2017.
 */

public class JSONParser {
    public ArrayList<Users> usersParser(String strJson) throws JSONException {
        ArrayList<Users> usersList = new ArrayList<>();
        JSONObject json = new JSONObject(strJson);
        JSONArray data = json.getJSONArray("data");
        for(int i=0; i< data.length(); i++) {
            Users users = new Users();
            users.setName(data.getJSONObject(i).getString("username"));
            usersList.add(users);
        }
        return usersList;
    }
}
