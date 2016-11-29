package com.example.tonyquick.thequicklawsonguidetohaarlem.services;

import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 01/11/2016.
 */

public class GetFirebaseData {

    FirebaseDataHandlerInterface listener;
    DatabaseReference attractions;
    DatabaseReference suggestions;

    public static final String ATTRACTIONS_REFERENCE = "Attractions";
    public static final String SUGGESTIONS_REFERENCE = "Suggestions";



    GetFirebaseData(FirebaseDataHandlerInterface listener){
        this.listener=listener;


    }

   /* public void getDataset(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = mDatabase.getReference();

        final ValueEventListener valListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Attraction> dataset = new ArrayList<>();
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Log.d("AJQ",child.toString());
                    dataset.add(child.getValue(Attraction.class));

                }

                listener.initialDataset(dataset);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbRef.child(ATTRACTIONS_REFERENCE).addListenerForSingleValueEvent(valListener);

    }*/

    void addChangeListener(){
        attractions = FirebaseDatabase.getInstance().getReference().child(ATTRACTIONS_REFERENCE);
        final ChildEventListener changeListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listener.childAdded(dataSnapshot.getValue(Attraction.class));
                Log.d("Ajq","Child has been added");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                listener.childChanged(dataSnapshot.getValue(Attraction.class));
                Log.d("Ajq","Child has been changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listener.childRemoved(dataSnapshot.getKey());
                Log.d("AJQ child removed","snapshot is: " + dataSnapshot.toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        attractions.addChildEventListener(changeListener);

    }


    void addChangeListenerSuggestions(){
        suggestions = FirebaseDatabase.getInstance().getReference().child(SUGGESTIONS_REFERENCE);
        final ChildEventListener changeListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listener.suggestionAdded(dataSnapshot.getValue(Attraction.class));
                Log.d("Ajq","Child has been added");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listener.suggestionRemoved(dataSnapshot.getKey());
                Log.d("AJQ child removed","snapshot is: " + dataSnapshot.toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        suggestions.addChildEventListener(changeListener);

    }







    interface FirebaseDataHandlerInterface{
        void childAdded(Attraction a);
        void childChanged(Attraction a);
        void childRemoved(String id);

        void suggestionAdded(Attraction a);
        void suggestionRemoved(String id);


    }

}
