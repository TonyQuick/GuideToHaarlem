package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.AdminActivity;


public class AdminMainMenuFragment extends Fragment {

    Button createAttraction, editAttraction, deleteAttraction, manageSuggestions;
    MenuButtonListener listener;


    public AdminMainMenuFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = (MenuButtonListener) this.getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_main_menu, container, false);
        createAttraction = (Button)view.findViewById(R.id.create_attraction_button);
        editAttraction = (Button)view.findViewById(R.id.edit_attraction_button);
        deleteAttraction = (Button)view.findViewById(R.id.delete_attraction_button);
        manageSuggestions = (Button)view.findViewById(R.id.manage_suggestions_button);


        createAttraction.setOnClickListener(new ButtonClickListener());
        editAttraction.setOnClickListener(new ButtonClickListener());
        deleteAttraction.setOnClickListener(new ButtonClickListener());
        manageSuggestions.setOnClickListener(new ButtonClickListener());

        return view;
    }




    public interface MenuButtonListener{
        void onMenuButtonClicked(View v);
    }

    class ButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            listener.onMenuButtonClicked(v);
        }
    }


}
