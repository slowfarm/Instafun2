package eva.android.com.instafun2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import eva.android.com.instafun2.data.Comments;
import eva.android.com.instafun2.data.Parser;
import eva.android.com.instafun2.R;
import eva.android.com.instafun2.data.UserData;
import eva.android.com.instafun2.dataSources.UserDataTask;
import eva.android.com.instafun2.adapters.UserWallAdapter;


public class UserWallActivity extends AppCompatActivity implements SwipyRefreshLayout.OnRefreshListener {

    UserWallAdapter adapter;
    RecyclerView recyclerView;
    UserData userData;
    ArrayList<Comments> comments = new ArrayList<>();
    SwipyRefreshLayout mSwipeRefreshLayout;
    String json = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wall);

        mSwipeRefreshLayout = (SwipyRefreshLayout)findViewById(R.id.swipyrefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        Intent intent = this.getIntent();
        if(intent.getExtras() != null) {
            userData = intent.getParcelableExtra("userData");
            comments = intent.getParcelableArrayListExtra("comments");
        }
        userData.comments = comments;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new UserWallAdapter(this, userData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        try {
            json = new UserDataTask(userData.username, userData.maxId).execute().get();
            userData.add(new Parser().userDataParser(json));
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Конец списка", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
