package eva.android.com.instafun2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.data.FragmentCommunicator;
import eva.android.com.instafun2.data.UserData;
import eva.android.com.instafun2.parallax.ParallaxFragment;
import eva.android.com.instafun2.parallax.ParallaxRelativeLayout;

public class MainFragment extends ParallaxFragment implements FragmentCommunicator {

    ArrayList<UserData> mData = new ArrayList<>();
    static ParallaxRelativeLayout relativeLayout;

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_content, container, false);
        relativeLayout = (ParallaxRelativeLayout) v.findViewById(R.id.parallax);
        relativeLayout.addAllViews(mData);
        setParallaxRelativeLayout(relativeLayout);
        return v;
    }

    @Override
    public void passDataToFragment(ArrayList<UserData> data) {
        mData = data;
    }
}