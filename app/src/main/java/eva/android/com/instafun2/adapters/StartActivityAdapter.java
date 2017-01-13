package eva.android.com.instafun2.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import java.util.ArrayList;
import java.util.Random;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.activities.UserWallActivity;
import eva.android.com.instafun2.data.UserData;

public class StartActivityAdapter extends RecyclerView.Adapter<StartActivityAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<UserData> mUserdata;
    private Bundle bundle = new Bundle();
    private Intent intent;


    public StartActivityAdapter(Context context, ArrayList<UserData> userData) {
        mContext = context;
        mUserdata = userData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(inflater.inflate(R.layout.start_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Picasso.with(mContext)
                .load(mUserdata.get(i).userPhoto)
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_error)
                .into(viewHolder.getBackgroundImage());
//        int scale = new Random().nextInt(240)+400;
//        LinearLayout.LayoutParams layoutParms = new LinearLayout.LayoutParams(scale, scale);
//        layoutParms.setMargins(scale/5,scale/6,scale/7, scale/8);
//        viewHolder.getBackgroundImage().setLayoutParams(layoutParms);
        viewHolder.getBackgroundImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), UserWallActivity.class);
                bundle.putParcelable("userData", mUserdata.get(i));
                bundle.putParcelableArrayList("comments", mUserdata.get(i).comments);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });

        viewHolder.getBackgroundImage().reuse();
    }

    @Override
    public int getItemCount() {
        return mUserdata.size();
    }

    public static class ViewHolder extends ParallaxViewHolder {

        public ViewHolder(View v) {
            super(v);
        }

        @Override
        public int getParallaxImageId() {
            return R.id.backgroundImage;
        }
    }


}