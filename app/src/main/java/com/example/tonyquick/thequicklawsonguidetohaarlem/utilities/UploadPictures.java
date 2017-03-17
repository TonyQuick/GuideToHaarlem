package com.example.tonyquick.thequicklawsonguidetohaarlem.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tonyquick.thequicklawsonguidetohaarlem.models.AlbumItem;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.GetFirebaseData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    AlbumItem albumItem;
    Uri picUri;
    Context context;

    private static String stateAtt="att";
    private static String stateAlb="alb";
    private String state;

    public UploadPictures(Attraction attraction, Uri picUri, Context context){
        this.picUri= picUri;
        this.attraction=attraction;
        this.context=context;
        state=stateAtt;

    }

    public UploadPictures(AlbumItem albumItem, Uri picUri, Context context){
        this.picUri= picUri;
        this.albumItem=albumItem;
        this.context=context;
        state=stateAlb;
    }



    @Override
    protected Void doInBackground(Void... params) {

        StorageReference smallRef=null;
        StorageReference largeRef=null;

        if(state.equals(stateAtt)) {
            smallRef = FirebaseStorage.getInstance().getReference().child("Attractions").child(attraction.getId());
            largeRef = FirebaseStorage.getInstance().getReference().child("Attractions").child(attraction.getId() + "large");
        }else if(state.equals(stateAlb)){
            smallRef = FirebaseStorage.getInstance().getReference().child("Album").child(albumItem.getId());
            largeRef = FirebaseStorage.getInstance().getReference().child("Album").child(albumItem.getId() + "large");

        }
        ByteArrayOutputStream smallBaos = new ByteArrayOutputStream();
        ByteArrayOutputStream largeBaos = new ByteArrayOutputStream();

        Bitmap smallBitmap = BitmapDecoder.decodeSampledBitmapFromResourceSetValues(context,picUri,7);
        smallBitmap.compress(Bitmap.CompressFormat.JPEG,50,smallBaos);
        byte[] smallData = smallBaos.toByteArray();
        smallBitmap.recycle();

        UploadTask smallUpload = smallRef.putBytes(smallData);
        smallUpload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (state.equals(stateAtt)) {
                    attraction.setPictureLocationSmall(taskSnapshot.getDownloadUrl().toString());
                    FirebaseDatabase.getInstance().getReference().child("Attractions").child(attraction.getId())
                            .child("pictureLocationSmall").setValue(attraction.getPictureLocationSmall());
                }else if (state.equals(stateAlb)) {
                    albumItem.setUrlSmall(taskSnapshot.getDownloadUrl().toString());
                    FirebaseDatabase.getInstance().getReference().child("Album").child(albumItem.getId())
                            .child("urlSmall").setValue(albumItem.getUrlLarge());

                }
            }
        });
        smallUpload.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Ajq",e.toString());
            }
        });

        smallData=null;

        Bitmap largeBitmap = BitmapDecoder.decodeSampledBitmapFromResourceSetValues(context,picUri,2);
        largeBitmap.compress(Bitmap.CompressFormat.JPEG,100,largeBaos);
        byte[] largeData = largeBaos.toByteArray();
        largeBitmap.recycle();

        UploadTask largeUpload = largeRef.putBytes(largeData);
        largeUpload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if(state.equals(stateAtt)) {
                    attraction.setPictureLocationLarge(taskSnapshot.getDownloadUrl().toString());
                    FirebaseDatabase.getInstance().getReference().child("Attractions").child(attraction.getId())
                            .child("pictureLocationLarge").setValue(attraction.getPictureLocationLarge());
                }else if(state.equals(stateAlb)){
                    FirebaseDatabase.getInstance().getReference().child(GetFirebaseData.ALBUM_REFERENCE).child(albumItem.getId())
                            .child("urlLarge").setValue(taskSnapshot.getDownloadUrl().toString());
                }
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
