package com.playover;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.playover.datamodels.MisuseReportDataModel;

public class ReportMisuse_Fragment extends Fragment {

    public ReportMisuse_Fragment() {
        // required empty pubic constructor
    }

    private Button submitReportBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String selectedUserUid = getArguments().getString(Constants.KEY_BUDUID);
        String selectedUserName = getArguments().getString(Constants.KEY_BUDNAME);

        View rootView = inflater.inflate(R.layout.fragment_report_misuse, container, false);
        TextView reportUsernameTV = (TextView)rootView.findViewById(R.id.misuse_username);
        reportUsernameTV.setText("Please fill out your misuse report for " + selectedUserName);
        EditText reportEditText = (EditText)rootView.findViewById(R.id.misuse_report);

        submitReportBtn = rootView.findViewById(R.id.submitReportBtn);
        submitReportBtn.setOnClickListener(v -> {
            String misuseReport = reportEditText.getText().toString();
            Log.i("misuse", misuseReport);
            MisuseReportDataModel reportVm = new MisuseReportDataModel();
            //reportVm.putMessageThread(misuseReport);
            //reportVm.setValue("yooo");
        });

        return rootView;
    }
}
