package eva.android.com.instafun2.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.data.UserData;
import eva.android.com.instafun2.activities.CommentsActivity;


public class UserWallAdapter extends RecyclerView.Adapter<UserWallAdapter.ViewHolder> {

    private Context mContext;
    private UserData mUserdata;
    private Bundle bundle = new Bundle();

    public UserWallAdapter(Context context, UserData userData) {
        mContext = context;
        mUserdata = userData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_user_wall_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Picasso.with(mContext)
                .load(mUserdata.photoStandardResolution.get(i))
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_error)
                .into(viewHolder.mImageView);
        viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putParcelable("data", mUserdata.comments.get(i));
                bundle.putString("photo", mUserdata.photoStandardResolution.get(i));
                Intent intent = new Intent(view.getContext(), CommentsActivity.class);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserdata.comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView mCardView;
        private final ImageView mImageView;

        ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.list_image);
            mCardView = (CardView) v.findViewById(R.id.card_view);
        }
    }
}
