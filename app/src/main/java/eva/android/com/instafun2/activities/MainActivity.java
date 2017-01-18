package eva.android.com.instafun2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.adapters.AutocompleteAdapter;
import eva.android.com.instafun2.data.Parser;
import eva.android.com.instafun2.data.UserData;
import eva.android.com.instafun2.dataSources.UserDataTask;
import eva.android.com.instafun2.fragments.MainParallaxFragment;

import static eva.android.com.instafun2.activities.SplashActivity.helper;


public class MainActivity extends AppCompatActivity {

    Button button;
    AutoCompleteTextView searchTextView;
    AutocompleteAdapter adapter;

    UserData userData;
    ArrayList<UserData> searchUsers = new ArrayList<>();
    String maxId = "";
    String username;
    String token = "";

    Bundle bundle = new Bundle();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchUsers = helper.getUserData();

        button = (Button)findViewById(R.id.button);
        intent = this.getIntent();
        token = intent.getStringExtra("url").split("=")[1];

        MainParallaxFragment fragment = new MainParallaxFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, fragment)
                .commit();

        searchTextView = (AutoCompleteTextView) findViewById(R.id.search);
        adapter = new AutocompleteAdapter(this,android.R.layout.simple_dropdown_item_1line, token);
        searchTextView.setAdapter(adapter);

        searchTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchTextView.setText(adapter.getItem(position).getName());
                startUserWallActivity();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startUserWallActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(searchUsers.size() != helper.getUserData().size()) {
            searchUsers = helper.getUserData();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, new MainParallaxFragment()).commit();
        }
    }

    private void startUserWallActivity() {
        username = searchTextView.getText().toString();
        String json = "";
        try {
            json = new UserDataTask(username, maxId).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(json != null) {
            try {
                userData = new Parser().userDataParser(json);
                intent = new Intent(MainActivity.this, UserWallActivity.class);
                bundle.putParcelable("userData", userData);
                bundle.putParcelableArrayList("comments", userData.comments);
                intent.putExtras(bundle);
                startActivity(intent);
                if(equalizer(searchUsers, userData)) {
                    helper.setUserData(json);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                switch(json) {
                    case "UnknownHostException":
                        Toast.makeText(MainActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                        break;
                    case "FileNotFoundException":
                        Toast.makeText(MainActivity.this, "no such user", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "access denied", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    private boolean equalizer(ArrayList<UserData> data1, UserData data2) {
        boolean flag = true;
        for(int i=0; i< data1.size(); i++)
            if (data1.get(i).username.equals(data2.username)) {
                flag = false;
                break;
            }
        return flag;
    }
}
