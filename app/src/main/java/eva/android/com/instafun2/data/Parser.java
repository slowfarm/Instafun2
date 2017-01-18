package eva.android.com.instafun2.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Parser {

    public UserData userDataParser(String strJson) throws JSONException {
        UserData userData;
        ArrayList<Comments> comments = new ArrayList<>();
        ArrayList<String> photoStandardResolution = new ArrayList<>();

        JSONObject json = new JSONObject(strJson);
        JSONArray items = json.getJSONArray("items");
        JSONObject user = items.getJSONObject(0).getJSONObject("user");

        String userPhoto = user.getString("profile_picture");
        String username = user.getString("username");
        String maxId = items.getJSONObject(items.length()-1).getString("id");

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
            JSONObject standard_resolution = items.getJSONObject(i).getJSONObject("images")
                    .getJSONObject("standard_resolution");
            photoStandardResolution.add(standard_resolution.getString("url"));
            comments.add(new Comments(name, text));
        }
        userData = new UserData(userPhoto, username, photoStandardResolution, maxId, comments);
        return userData;
    }
    //autoCompleteTextView data parser
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
