package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionGenericAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.decorators.SpacingDecorator;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.AttractionList;
import com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders.AttractionGenericViewHolder;

import java.util.ArrayList;


public class AttractionEditSelector extends Fragment implements AttractionGenericAdapter.AttractionGenClickListener {

    private static final String PARAM_STATE = "STATE";
    public static final String STATE_DELETE = "delete";
    public static final String STATE_EDIT = "edit";
    public static final String STATE_SUGGESTIONS = "suggestions";

    private Attraction selected;

    private String state;

    AttractionGenericAdapter.AttractionGenClickListener listener;
    ConfirmDeleteListener deleteConfirmListener;


    public AttractionEditSelector() {
        // Required empty public constructor
    }


    public static AttractionEditSelector newInstance(String state) {
        AttractionEditSelector fragment = new AttractionEditSelector();
        Bundle b = new Bundle();
        b.putString(PARAM_STATE,state);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.state = getArguments().getString(PARAM_STATE);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Context altContext = new ContextThemeWrapper(getContext(),R.style.AppAltTheme);
        LayoutInflater altInflater = inflater.cloneInContext(altContext);

        View v = altInflater.inflate(R.layout.fragment_attraction_edit_selector, container, false);
        TextView title = (TextView)v.findViewById(R.id.attraction_edit_selector_title);

        ArrayList<Attraction>data;


        if (state.equals(STATE_DELETE)){
            title.setText("Select attraction for deletion");
            listener=this;
            deleteConfirmListener = (ConfirmDeleteListener)getActivity();
            data = AttractionList.getInstance().getList();

        }else if (state.equals(STATE_SUGGESTIONS)){
            title.setText("Current Suggestions");
            listener=(AttractionGenericAdapter.AttractionGenClickListener)getActivity();
            data = AttractionList.getInstance().getSuggestions();
        }else{
            listener=(AttractionGenericAdapter.AttractionGenClickListener)getActivity();
            data = AttractionList.getInstance().getList();
        }



        Log.d("Ajq",String.valueOf(data.size()));

        RecyclerView recycler = (RecyclerView)v.findViewById(R.id.recycler_attraction_generic);

        AttractionGenericAdapter adapter = new AttractionGenericAdapter(data,listener,getContext());
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new SpacingDecorator(6,SpacingDecorator.SPACE_DECORATOR_ORIENTATION_VERTICAL));

        LinearLayoutManager linMan = new LinearLayoutManager(getContext());
        linMan.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linMan);


        return v;
    }


    @Override
    public void onAttractionGenClicked(Attraction a) {

        selected = a;
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage("Are you sure you wish to delete?");
        alert.setPositiveButton("Yes",deleteRecord);
        alert.setNegativeButton("No",deleteRecord);
        alert.show();
    }

    Dialog.OnClickListener deleteRecord = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    deleteConfirmListener.onDeleteConfirmed(selected);
            }
        }
    };

    public interface ConfirmDeleteListener{
        void onDeleteConfirmed(Attraction a);
    }


}
