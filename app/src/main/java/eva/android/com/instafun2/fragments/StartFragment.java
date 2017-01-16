package eva.android.com.instafun2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.activities.UserWallActivity;
import eva.android.com.instafun2.data.UserData;
import eva.android.com.instafun2.parallax.ParallaxFragment;
import eva.android.com.instafun2.parallax.ParallaxRelativeLayout;

import static eva.android.com.instafun2.activities.MainActivity.helper;

public class StartFragment extends ParallaxFragment {

    ArrayList<UserData> mData = new ArrayList<>();
    Context context;
    View v;
    ParallaxRelativeLayout relativeLayout;
    private Bundle bundle = new Bundle();
    private Intent intent;

    public StartFragment() {
        mData.addAll(helper.getUserData());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_start_content, container, false);

        context = v.getContext().getApplicationContext();
        relativeLayout = (ParallaxRelativeLayout) v.findViewById(R.id.parallax);
        addAllViews();

        setParallaxRelativeLayout((ParallaxRelativeLayout) v.findViewById(R.id.parallax));
        return v;
    }

    public void addAllViews() {
        ArrayList<ImageView> imageView = new ArrayList<>();
        for(int i=0; i<mData.size(); i++) {
            int top = new Random().nextInt(1200);
            int left = new Random().nextInt(600);
            int tag = new Random().nextInt(4) + 1;
            int scale = new Random().nextInt(120)+200;
            final int finalI = i;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    scale, scale);
            layoutParams.setMargins(left, top, 0, 0);
            imageView.add(new ImageView(context));

            Picasso.with(context)
                    .load(mData.get(i).userPhoto)
                    .placeholder(R.drawable.ic_photo)
                    .error(R.drawable.ic_error)
                    .into(imageView.get(i));

            imageView.get(i).setLayoutParams(layoutParams);
            imageView.get(i).setTag(tag);
            imageView.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(view.getContext(), UserWallActivity.class);
                    bundle.putParcelable("userData",
                            mData.get(finalI));
                    bundle.putParcelableArrayList("comments", mData.get(finalI).comments);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            relativeLayout.addView(imageView.get(i));
        }
    }
}