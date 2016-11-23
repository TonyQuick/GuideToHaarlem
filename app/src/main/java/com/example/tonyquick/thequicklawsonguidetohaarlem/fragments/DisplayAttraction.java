package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.AttractionList;
import com.example.tonyquick.thequicklawsonguidetohaarlem.utilities.ViewBuilders;


public class DisplayAttraction extends Fragment {

    private static final String ARG_CURRENT_STATE = "currentstate";
    private Button directionOnMap;
    private LinearLayout mainContent;
    private DirectionsOnMapListener listener;
    private Attraction currentAtt;

    private String currentState;

    public DisplayAttraction() {

    }



    public static DisplayAttraction newInstance(String param1) {
        DisplayAttraction fragment = new DisplayAttraction();
        Bundle args = new Bundle();
        args.putString(ARG_CURRENT_STATE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentState = getArguments().getString(ARG_CURRENT_STATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Context altContext = new ContextThemeWrapper(getContext(),R.style.AppAltTheme);
        LayoutInflater altInflater = inflater.cloneInContext(altContext);

        View v = altInflater.inflate(R.layout.fragment_display_attraction, container, false);
        currentAtt = AttractionList.getInstance().getCurrentAttractionInScope();

        directionOnMap = (Button)v.findViewById(R.id.direction_on_map);
        mainContent = (LinearLayout)v.findViewById(R.id.content_container);
        TextView title = (TextView)v.findViewById(R.id.attraction_title);
        title.setText(currentAtt.getTitle());


        ViewBuilders viewBuilder = new ViewBuilders(getContext(),inflater);
        mainContent = viewBuilder.attractionCardContentBuilder(currentState, currentAtt,mainContent);




        listener = (MainActivity)getActivity();
        directionOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDirectionsOnMapClicked();
            }
        });


        return v;




    }




    public interface DirectionsOnMapListener{
        void onDirectionsOnMapClicked();
    }


}
