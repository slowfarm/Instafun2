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
import eva.android.com.instafun2.data.Parser;
import eva.android.com.instafun2.R;
import eva.android.com.instafun2.data.UserData;
import eva.android.com.instafun2.dataSources.UserDataGetTask;


public class StartActivity extends AppCompatActivity {

    Button button;
    String token = "";
    String json = "";

    UserData userData;
    ArrayList<Comments> comments = new ArrayList<>();
    String maxId = "";

    Bundle bundle = new Bundle();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        button = (Button)findViewById(R.id.button);
        intent = this.getIntent();
        String url = intent.getStringExtra("url");
        token = url.split("=")[1];

        final AutoCompleteTextView userSearch = (AutoCompleteTextView) findViewById(R.id.search);
        final AutocompleteAdapter adapter = new AutocompleteAdapter(this,android.R.layout.simple_dropdown_item_1line, token);
        userSearch.setAdapter(adapter);

        //when autocomplete is clicked
        userSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String countryName = adapter.getItem(position).getName();
                userSearch.setText(countryName);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    json = new UserDataGetTask(userSearch.getText().toString(), maxId).execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if(json != null) {
                    try {
                        userData = new Parser().userDataParser(json);
                        comments = userData.comments;
                        intent = new Intent(StartActivity.this, UserWallActivity.class);
                        bundle.putParcelable("userData", userData);
                        bundle.putParcelableArrayList("comments", comments);
                        bundle.putString("username", userSearch.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
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
}
