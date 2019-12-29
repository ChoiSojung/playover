package app.playover;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.playover.R;


public class Terms_Fragment extends Fragment {


    public Terms_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String inClass = "inClass";
        String inClassMsg = "InClass Terms Fragment";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreate";
        String onCreateMsg = "On Create";
        Log.i(onCreate, onCreateMsg);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String inClass = "inClass";
        String inClassMsg = "InClass Terms Fragment";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreateView";
        String onCreateMsg = "On Create View";
        Log.i(onCreate, onCreateMsg);
        View rootView = inflater.inflate(R.layout.fragment_terms, container, false);
        return rootView;
    }

}
