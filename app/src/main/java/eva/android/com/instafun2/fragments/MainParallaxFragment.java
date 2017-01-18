package eva.android.com.instafun2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.data.UserData;
import eva.android.com.instafun2.parallax.ParallaxFragment;
import eva.android.com.instafun2.parallax.ParallaxRelativeLayout;

import static eva.android.com.instafun2.activities.SplashActivity.helper;

public class MainParallaxFragment extends ParallaxFragment {

    ArrayList<UserData> mData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_content, container, false);
        mData = helper.getUserData();
        ParallaxRelativeLayout relativeLayout = (ParallaxRelativeLayout) v.findViewById(R.id.parallax);
        relativeLayout.addAllViews(mData);
        setParallaxRelativeLayout(relativeLayout);
        return v;
    }
}