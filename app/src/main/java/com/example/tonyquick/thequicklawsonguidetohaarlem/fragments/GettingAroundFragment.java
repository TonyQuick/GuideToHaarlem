package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GettingAroundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GettingAroundFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public GettingAroundFragment() {
        // Required empty public constructor
    }

    public static GettingAroundFragment newInstance() {
        GettingAroundFragment fragment = new GettingAroundFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_getting_around, container, false);
        TextView bikeHyperlink = (TextView)v.findViewById(R.id.bike_hyperlink_textview);
        bikeHyperlink.setMovementMethod(LinkMovementMethod.getInstance());

        TextView marketplaceLinkTextView = (TextView)v.findViewById(R.id.link_9292_textview);
        marketplaceLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "nl.negentwee";
                try {
                    Intent marketplaceIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + url));
                    startActivity(marketplaceIntent);
                }catch (ActivityNotFoundException e){
                    Log.d("AJQ",e.toString());
                    Toast.makeText(getContext(),"Cannot find app on marketplace",Toast.LENGTH_SHORT).show();
                }

            }
        });


        return v;
    }

}
