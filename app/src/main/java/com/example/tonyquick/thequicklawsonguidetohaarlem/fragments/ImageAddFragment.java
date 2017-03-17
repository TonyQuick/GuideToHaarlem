package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.AlbumItem;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.GetFirebaseData;
import com.example.tonyquick.thequicklawsonguidetohaarlem.utilities.BitmapDecoder;
import com.example.tonyquick.thequicklawsonguidetohaarlem.utilities.UploadPictures;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ImageAddFragment extends Fragment {


    private Button submitBtn, imageSelectorBtn;
    private EditText imageTitleText;
    private ImageView selectedImage;
    private Uri pictureFileLoc = null;
    private InputMethodManager imm;
    private ImageAddFragmentEventHandler finishListener;

    private final int IMAGE_SELECTOR_REQ = 501;


    public ImageAddFragment() {
        // Required empty public constructor
    }


    public static ImageAddFragment newInstance() {
        ImageAddFragment fragment = new ImageAddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setFinishListener(){
        if (getActivity() instanceof ImageAddFragmentEventHandler){
            finishListener = (ImageAddFragmentEventHandler) getActivity();
        } else{
            Log.d("AJQ","Parent class is not an instance of ImageAddFragmentEventHandler");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setFinishListener();

        final Context altContext = new ContextThemeWrapper(getContext(),R.style.AppAltTheme);
        LayoutInflater altInflater = inflater.cloneInContext(altContext);
        View v =  altInflater.inflate(R.layout.fragment_image_add, container, false);

        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        selectedImage = (ImageView)v.findViewById(R.id.selected_image_view);
        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(submitBtn.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,IMAGE_SELECTOR_REQ);
            }
        });

        /*imageSelectorBtn = (Button)v.findViewById(R.id.add_image_button);
        imageSelectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(submitBtn.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,IMAGE_SELECTOR_REQ);
            }
        });*/
        submitBtn = (Button)v.findViewById(R.id.submit_button);
        submitBtn.setOnClickListener(new SubmitEntry());




        imageTitleText = (EditText)v.findViewById(R.id.title_edittext);


        if (pictureFileLoc!=null){
            addPicToFrame();
        }


        return v;
    }

    void addPicToFrame(){
        selectedImage.setImageBitmap(BitmapDecoder
                .decodeBitmapByFrameSize(getContext(),pictureFileLoc,selectedImage.getHeight(),selectedImage.getWidth()));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case IMAGE_SELECTOR_REQ:
                if (resultCode== Activity.RESULT_OK){
                    pictureFileLoc = data.getData();
                    addPicToFrame();
                }


        }
    }








    private class SubmitEntry implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if(checkFieldsValid()) {
                DatabaseReference newAlbumItem = FirebaseDatabase.getInstance().getReference().child(GetFirebaseData.ALBUM_REFERENCE).push();
                AlbumItem newEntry = new AlbumItem(imageTitleText.getText().toString(),newAlbumItem.getKey());
                newAlbumItem.setValue(newEntry);

                UploadPictures uploadAlbPics = new UploadPictures(newEntry,pictureFileLoc,getContext());
                uploadAlbPics.execute();

                finishListener.imageAdded();

            }


        }

        boolean checkFieldsValid(){
            if (imageTitleText.getText().toString().equals("")){
                Toast.makeText(getContext(),"Image title cannot be empty",Toast.LENGTH_SHORT).show();
                return false;
            }
            if (pictureFileLoc==null){
                Toast.makeText(getContext(),"No image has been selected",Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }



    }



    public interface ImageAddFragmentEventHandler{
        void imageAdded();
    }



}
