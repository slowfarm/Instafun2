package eva.android.com.instafun2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import eva.android.com.instafun2.data.Comments;
import eva.android.com.instafun2.adapters.CommentsAdapter;
import eva.android.com.instafun2.R;


public class CommentsActivity extends AppCompatActivity {

    private Comments comments;
    private String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Intent intent = this.getIntent();
        if(intent.getExtras() != null) {
            comments = intent.getParcelableExtra("data");
            photo = intent.getStringExtra("photo");
        }
        ImageView imageView = (ImageView) findViewById(R.id.photo);
        Picasso.with(this)
                .load(photo)
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_error)
                .into(imageView);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommentsAdapter adapter = new CommentsAdapter(comments);
        recyclerView.setAdapter(adapter);
    }

}
