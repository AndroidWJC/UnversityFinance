package com.hqj.universityfinance.explore;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hqj.universityfinance.R;

/**
 * Created by wang on 17-9-12.
 */

public class ExploreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore_fragment, container, false);
        return view;
    }
}
