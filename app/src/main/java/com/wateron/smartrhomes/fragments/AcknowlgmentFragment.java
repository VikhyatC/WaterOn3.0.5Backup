package com.wateron.smartrhomes.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wateron.smartrhomes.R;

/**
 * Created by Vikhyat Chandra on 2/18/2018.
 */

public class AcknowlgmentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.acknowlgmentscreen,container,false);
        return view;
    }
}
