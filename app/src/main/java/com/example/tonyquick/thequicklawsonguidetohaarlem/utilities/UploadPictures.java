package com.example.tonyquick.thequicklawsonguidetohaarlem.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by Tony Quick on 19/11/2016.
 */

public class UploadPictures extends AsyncTask<Void,Void,Void> {

    Attraction attraction;
    Uri picUri;
    Context context;


    public UploadPictures(Attraction attraction, Uri picUri, Context context){
        this.picUri= picUri;
        this.attraction=attraction;
        this.context=context;

    }



    @Override
    protected Void doInBackground(Void... params) {

        StorageReference smallRef = FirebaseStorage.getInstance().getReference().child("Attractions").child(attraction.getId());
        StorageReference largeRef = FirebaseStorage.getInstance().getReference().child("Attractions").child(attraction.getId()+"large");

        ByteArrayOutputStream smallBaos = new ByteArrayOutputStream();
        ByteArrayOutputStream largeBaos = new ByteArrayOutputStream();

        Bitmap smallBitmap = BitmapDecoder.decodeSampledBitmapFromResource(context,picUri,7);
        smallBitmap.compress(Bitmap.CompressFormat.JPEG,50,smallBaos);
        byte[] smallData = smallBaos.toByteArray();
        smallBitmap.recycle();

        UploadTask smallUpload = smallRef.putBytes(smallData);
        smallUpload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                attraction.setPictureLocationSmall(taskSnapshot.getDownloadUrl().toString());
                FirebaseDatabase.getInstance().getReference().child("Attractions").child(attraction.getId())
                        .child("pictureLocationSmall").setValue(attraction.getPictureLocationSmall());
            }
        });
        smallUpload.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Ajq",e.toString());
            }
        });

        smallData=null;

        Bitmap largeBitmap = BitmapDecoder.decodeSampledBitmapFromResource(context,picUri,2);
        largeBitmap.compress(Bitmap.CompressFormat.JPEG,100,largeBaos);
        byte[] largeData = largeBaos.toByteArray();
        largeBitmap.recycle();

        UploadTask largeUpload = largeRef.putBytes(largeData);
        largeUpload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                attraction.setPictureLocationLarge(taskSnapshot.getDownloadUrl().toString());
                FirebaseDatabase.getInstance().getReference().child("Attractions").child(attraction.getId())
                        .child("pictureLocationLarge").setValue(attraction.getPictureLocationLarge());
            }
        });
        largeUpload.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Ajq",e.toString());
            }
        });


        return null;
    }
}
