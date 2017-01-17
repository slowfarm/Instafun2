package eva.android.com.instafun2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.Transporter;
import eva.android.com.instafun2.data.UserData;
import eva.android.com.instafun2.parallax.ParallaxFragment;
import eva.android.com.instafun2.parallax.ParallaxRelativeLayout;

import static eva.android.com.instafun2.activities.SplashActivity.helper;

public class MainFragment extends ParallaxFragment implements Transporter {

    private ParallaxRelativeLayout relativeLayout;
    ArrayList<UserData> mData = new ArrayList<>();
    private static MainFragment instance;

    public MainFragment() {
        mData = helper.getUserData();
    }

    public static MainFragment getInstance() {
        if (instance == null) {
            instance = new MainFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start_content, container, false);
        relativeLayout = (ParallaxRelativeLayout) v.findViewById(R.id.parallax);
        relativeLayout.addAllViews(mData);
        setParallaxRelativeLayout(relativeLayout);
        return v;
    }

    @Override
    public void addView(UserData data) {
        relativeLayout = (ParallaxRelativeLayout) getView().findViewById(R.id.parallax);
        relativeLayout.addView(data);
        setParallaxRelativeLayout(relativeLayout);
    }
}