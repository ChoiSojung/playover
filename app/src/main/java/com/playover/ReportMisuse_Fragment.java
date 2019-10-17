package com.playover;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReportMisuse_Fragment extends Fragment {

    public ReportMisuse_Fragment() {
        // required empty pubic constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("misuse", "infragment");
        View rootView = inflater.inflate(R.layout.fragment_report_misuse, container, false);

        return rootView;
    }
}
