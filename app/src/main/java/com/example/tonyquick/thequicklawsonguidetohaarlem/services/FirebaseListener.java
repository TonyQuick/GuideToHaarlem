package com.example.tonyquick.thequicklawsonguidetohaarlem.services;

import android.util.Log;

import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 01/11/2016.
 */

public class FirebaseListener {

    DataChangedCallback changeListener;

    public FirebaseListener(){


        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = mDatabase.getReference();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Attraction> awway = new ArrayList<>();
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Log.d("AJQ",child.toString());
                    AttractionList.getInstance().addAttraction(child.getValue(Attraction.class));
                }

                Log.d("AJQ",""+awway.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };





        dbRef.child("Attractions").addListenerForSingleValueEvent(listener);



    }






    public interface DataChangedCallback{
        void onFireBaseDataChanged();

    }

}
