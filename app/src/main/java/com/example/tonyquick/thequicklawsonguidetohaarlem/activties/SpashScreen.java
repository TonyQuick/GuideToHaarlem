package com.example.tonyquick.thequicklawsonguidetohaarlem.activties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();


    }
}
