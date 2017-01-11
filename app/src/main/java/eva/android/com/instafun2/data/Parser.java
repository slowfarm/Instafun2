package eva.android.com.instafun2.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eva.android.com.instafun2.data.Comments;
import eva.android.com.instafun2.data.UserData;

public class Parser {
    public UserData userDataParser(String strJson, String username) throws JSONException {

        UserData userData;
        ArrayList<Comments> comments = new ArrayList<>();
        ArrayList<String> photoLowResolution = new ArrayList<>();
        ArrayList<String> photoStandartResolution = new ArrayList<>();
        String maxId;

        JSONObject json = new JSONObject(strJson);
        JSONArray items = json.getJSONArray("items");
        for(int i=0; i< items.length(); i++) {
            JSONObject images = items.getJSONObject(i).getJSONObject("images");
            JSONObject low_resolution = images.getJSONObject("low_resolution");
            JSONObject standard_resolution = images.getJSONObject("standard_resolution");
            photoLowResolution.add(low_resolution.getString("url"));
            photoStandartResolution.add(standard_resolution.getString("url"));
        }
        maxId = items.getJSONObject(items.length()-1).getString("id");
        for(int i=0; i< items.length(); i++) {
            ArrayList<String> name = new ArrayList<>();
            ArrayList<String> text = new ArrayList<>();
            JSONObject comment = items.getJSONObject(i).getJSONObject("comments");
            if(Integer.parseInt(comment.getString("count")) != 0) {
                JSONArray data = comment.getJSONArray("data");
                for (int j = 0; j < data.length(); j++) {
                    text.add(data.getJSONObject(j).getString("text"));
                    JSONObject from = data.getJSONObject(j).getJSONObject("from");
                    name.add(from.getString("username"));
                }
            }
            comments.add(new Comments(name, text));
        }
        userData = new UserData(username,photoLowResolution, photoStandartResolution, maxId, comments);
        return userData;
    }
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
