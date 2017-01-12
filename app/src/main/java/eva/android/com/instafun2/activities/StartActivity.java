package eva.android.com.instafun2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import eva.android.com.instafun2.adapters.AutocompleteAdapter;
import eva.android.com.instafun2.adapters.StartActivityAdapter;
import eva.android.com.instafun2.adapters.TestRecyclerAdapter;
import eva.android.com.instafun2.adapters.UserWallAdapter;
import eva.android.com.instafun2.data.Comments;
import eva.android.com.instafun2.data.Database;
import eva.android.com.instafun2.data.Parser;
import eva.android.com.instafun2.R;
import eva.android.com.instafun2.data.UserData;
import eva.android.com.instafun2.dataSources.UserDataTask;
import eva.android.com.instafun2.dataSources.UserPhotoTask;


public class StartActivity extends AppCompatActivity {

    static Database helper;

    ParallaxRecyclerView recyclerView;

    Button button;
    String token = "";

    UserData userData;
    ArrayList<Comments> comments = new ArrayList<>();
    ArrayList<UserData> searchUsers = new ArrayList<>();
    String maxId = "";
    String username;

    Bundle bundle = new Bundle();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        helper = Database.getInstance(this);

        button = (Button)findViewById(R.id.button);
        intent = this.getIntent();
        String url = intent.getStringExtra("url");
        token = url.split("=")[1];

        recyclerView = (ParallaxRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        searchUsers.addAll(helper.getUserData());
        if(searchUsers.size()>0) {
            recyclerView.setAdapter(new TestRecyclerAdapter(this, searchUsers));
        }

        final AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.search);
        final AutocompleteAdapter adapter = new AutocompleteAdapter(this,android.R.layout.simple_dropdown_item_1line, token);
        search.setAdapter(adapter);

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String countryName = adapter.getItem(position).getName();
                search.setText(countryName);
            }
        });

        username = search.getText().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = "";
                try {
                    json = new UserDataTask(username, maxId).execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if(json != null) {
                    try {
                        userData = new Parser().userDataParser(json, username);
                        comments = userData.comments;
                        intent = new Intent(StartActivity.this, UserWallActivity.class);
                        bundle.putParcelable("userData", userData);
                        bundle.putParcelableArrayList("comments", comments);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        if(equalizer(searchUsers, userData))
                            helper.setUserData(userData.username, json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartActivity.this,
                                "Отсутствует доступ к странице пользователя",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else Toast.makeText(StartActivity.this,"Ползователь с таким именем отсутствует",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
            searchUsers.clear();
            searchUsers.addAll(helper.getUserData());
            if(searchUsers.size()>0) {
                recyclerView.setAdapter(new TestRecyclerAdapter(this, searchUsers));
        }
    }

    public boolean equalizer(ArrayList<UserData> data1, UserData data2) {
        boolean flag = true;
        for(int i=0; i< data1.size(); i++)
            if(data1.get(i).username.equals(data2.username)) {
                flag = false;
                break;
            }
        return flag;
    }
}
