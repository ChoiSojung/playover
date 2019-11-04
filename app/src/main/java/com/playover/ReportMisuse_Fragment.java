package com.playover;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.playover.datamodels.MisuseReportDataModel;
import com.playover.models.MisuseReport;

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
        String reportersUserName = getArguments().getString(Constants.KEY_UID);

        View rootView = inflater.inflate(R.layout.fragment_report_misuse, container, false);
        TextView reportUsernameTV = (TextView)rootView.findViewById(R.id.misuse_username);
        reportUsernameTV.setText("Please fill out your misuse report for " + selectedUserName);
        EditText reportEditText = (EditText)rootView.findViewById(R.id.misuse_report);

        submitReportBtn = rootView.findViewById(R.id.submitReportBtn);
        submitReportBtn.setOnClickListener(v -> {
            String misuseReport = reportEditText.getText().toString();
            MisuseReport newReport = new MisuseReport(misuseReport, selectedUserUid, reportersUserName);
            Log.i("misuse", newReport.getReport());
            MisuseReportDataModel reportVm = new MisuseReportDataModel();
            reportVm.putMessageThread(newReport);

            //Show the toast msg "Misuse report is stored in database"
            Toast toast = Toast.makeText(getActivity(),"Thank you for reporting this issue.", Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity( Gravity.CENTER, 0, 0);

            // Redirect back to SelectedHotel_Fragment
            /*SelectedHotel_Fragment SelectedHotel_Fragment = new SelectedHotel_Fragment ();
            FragmentManager manager = getFragmentManager ();
            manager.beginTransaction ()
                    .replace ( R.id.item_list_checked_in,SelectedHotel_Fragment, SelectedHotel_Fragment.getTag () )
                    .commit ();
*/

            openAnotherActivity();
            //getActivity().onBackPressed();

            /*new View.OnClickListener (){
                @Override
                public void onClick(View v) {
                    SelectedHotel_Fragment SelectedHotel_Fragment = new SelectedHotel_Fragment ();
                    FragmentManager manager = getFragmentManager ();
                    manager.beginTransaction ()
                            .replace ( R.id.containerCheckIn,SelectedHotel_Fragment, SelectedHotel_Fragment.getTag () )
                            .commit ();

                    //openAnotherActivity;
                }};*/

        });
/*
        submitReportBtn.setOnClickListener ( new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                openAnotherActivity();
            }

            private void openAnotherActivity() {
                Intent myIntent = new Intent(ReportMisuse_Fragment.this.getActivity(), SelectedHotel_Fragment.class);
                startActivity ( myIntent );

            }
        } );
*/



        return rootView;
    }

    private void openAnotherActivity() {
        Intent myIntent = new Intent(ReportMisuse_Fragment.this.getActivity(), SelectedHotel_Fragment.class);
        startActivity ( myIntent );
    }
}
