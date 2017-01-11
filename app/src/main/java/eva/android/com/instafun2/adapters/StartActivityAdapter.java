package eva.android.com.instafun2.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.activities.UserWallActivity;
import eva.android.com.instafun2.data.UserData;


public class StartActivityAdapter extends RecyclerView.Adapter<StartActivityAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<UserData> mUserdata;
    private Bundle bundle = new Bundle();
    private Intent intent;

    public StartActivityAdapter(Context context, ArrayList<UserData> userData) {
        mContext = context;
        mUserdata = userData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.start_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Picasso.with(mContext)
                .load(mUserdata.get(i).photoStandardResolution.get(0))
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_error)
                .into(viewHolder.mImageView);
        int scale = new Random().nextInt(240)+400;
        viewHolder.mImageView.setLayoutParams(new LinearLayout.LayoutParams(scale, scale));

        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), UserWallActivity.class);
                bundle.putParcelable("userData", mUserdata.get(i));
                bundle.putParcelableArrayList("comments", mUserdata.get(i).comments);
                bundle.putString("username", mUserdata.get(i).username);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageView;

        ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.list_image);
        }
    }
}

