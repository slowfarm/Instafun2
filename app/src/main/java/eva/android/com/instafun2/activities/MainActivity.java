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

import eva.android.com.instafun2.adapters.AutocompleteAdapter;
import eva.android.com.instafun2.data.Comments;
import eva.android.com.instafun2.data.Database;
import eva.android.com.instafun2.data.Parser;
import eva.android.com.instafun2.R;
import eva.android.com.instafun2.data.UserData;
import eva.android.com.instafun2.dataSources.UserDataTask;
import eva.android.com.instafun2.fragments.StartFragment;


public class MainActivity extends AppCompatActivity {

    public static Database helper;

    Button button;
    String token = "";

    UserData userData;
    ArrayList<UserData> searchUsers = new ArrayList<>();
    String maxId = "";
    String username;
    StartFragment fragment;

    Bundle bundle = new Bundle();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = Database.getInstance(this);
        searchUsers = helper.getUserData();

        button = (Button)findViewById(R.id.button);
        intent = this.getIntent();
        String url = intent.getStringExtra("url");
        token = url.split("=")[1];
        fragment = new StartFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container,
                fragment).commit();

        final AutoCompleteTextView searchTextView = (AutoCompleteTextView) findViewById(R.id.search);
        final AutocompleteAdapter adapter = new AutocompleteAdapter(this,android.R.layout.simple_dropdown_item_1line, token);
        searchTextView.setAdapter(adapter);

        searchTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String countryName = adapter.getItem(position).getName();
                searchTextView.setText(countryName);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = searchTextView.getText().toString();
                String json = "";
                try {
                    json = new UserDataTask(username, maxId).execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.toString(),Toast.LENGTH_SHORT);
                }
                if(json != null) {
                    try {
                        userData = new Parser().userDataParser(json);
                        intent = new Intent(MainActivity.this, UserWallActivity.class);
                        bundle.putParcelable("userData", userData);
                        bundle.putParcelableArrayList("comments", userData.comments);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        if(equalizer(searchUsers, userData)) helper.setUserData(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,
                                "Отсутствует доступ к странице пользователя",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else Toast.makeText(MainActivity.this,"Ползователь с таким именем отсутствует",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean equalizer(ArrayList<UserData> data1, UserData data2) {
        boolean flag = true;
        for(int i=0; i< data1.size(); i++)
            if (data1.get(i).username.equals(data2.username)) {
                flag = false;
                break;
        }
        return flag;
    }

    @Override
    protected void onResume() {
        searchUsers = helper.getUserData();
        fragment = new StartFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                fragment).commit();
        super.onResume();
    }
}
