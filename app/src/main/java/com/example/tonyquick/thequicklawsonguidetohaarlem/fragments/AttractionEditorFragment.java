package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.customviews.AttributeView;
import com.example.tonyquick.thequicklawsonguidetohaarlem.customviews.AttributeViewListData;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.AttractionList;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.GetFirebaseData;
import com.example.tonyquick.thequicklawsonguidetohaarlem.utilities.BitmapDecoder;
import com.example.tonyquick.thequicklawsonguidetohaarlem.utilities.UploadPictures;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;


public class AttractionEditorFragment extends Fragment implements CheckBox.OnCheckedChangeListener {

    CheckBox isRestaurantCB, isBarCB, isCoffeeShopCB, isCafeCB, isThingToDoCB, isPhotoOpCB;
    TextView imageText, latText, lonText;
    EditText attTitleEditTxt, attDescriptionEditTxt;
    Button submitBtn, findOnMapBtn, addImageBtn, deleteBtn, searchGoogleBtn;
    LinearLayout attributeHolder;
    View v;

    InputMethodManager imm;


    private Uri pictureFileLoc;

    public static final int FIELD_REQUIRED = 0;
    public static final int FIELD_OPTIONAL = 1;
    public static final int FIELD_NOT_VAILABLE = 2;

    private String title, description, vibe, priceRange, quickEatOrSitdown, cuisineType, speciality, timeRequired, whereToGetTickets, subject, thingToDoType, pictureLocation;
    private double lat, lon;
    private ArrayList<String> orderSuggestions, photoToDoSuggestions;
    private Attraction current;

    private AttributeView vibeView, priceRangeView, quickEatOrSitdownView, cuisineTypeView, specialityView, timeRequiredView, whereToGetTicketsView, subjectView, thingToDoTypeView;
    private AttributeViewListData orderSuggestionsView, pSuggestionsView;

    private static final String ARG_STATUS = "STATUS";
    private static final String ARG_CALLED_FROM = "CALLED FROM";
    public static final String STATE_ATTRACTION_EDITOR_CREATE = "CREATE";
    public static final String STATE_ATTRACTION_EDITOR_EDIT = "EDIT";
    public static final String STATE_ATTRACTION_EDITOR_SUGGESTION = "SUGGESTION";
    public static final String STATE_ATTRACTION_EDITOR_MANAGE_SUGGESTIONS = "MANAGE SUGGESTIONS";

    private final int IMAGE_SELECTOR_REQ = 501;
    private final int PLACE_AUTOCOMPLETE_REQ = 502;

    ImageView testImageView;


    private AttractionEditorListener finishListener;

    private String status;
    private String calledFrom;  //indicates which menu this fragment was called from, if relevant


    public AttractionEditorFragment() {
        // Required empty public constructor
    }

    public static AttractionEditorFragment newInstance(String status, String calledFrom) {
        AttractionEditorFragment fragment = new AttractionEditorFragment();
        Bundle b = new Bundle();
        b.putString(ARG_STATUS,status);
        b.putString(ARG_CALLED_FROM,calledFrom);
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.status = getArguments().getString(ARG_STATUS);
            this.calledFrom = getArguments().getString(ARG_CALLED_FROM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_create_attraction, container, false);

        TextView title = (TextView)v.findViewById(R.id.attraction_editor_title);
        switch (status){
            case STATE_ATTRACTION_EDITOR_EDIT:
                title.setText(R.string.attraction_editor_title_edit);
                break;
            case STATE_ATTRACTION_EDITOR_SUGGESTION:
                title.setText(R.string.attraction_editor_title_suggestion);
                break;
            case STATE_ATTRACTION_EDITOR_MANAGE_SUGGESTIONS:
                title.setText(R.string.verify_suggestion);
                break;
        }

        searchGoogleBtn = (Button) v.findViewById(R.id.search_google);
        searchGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AJQ","I've been clicked");
                try{
                    AutocompleteFilter filter = new AutocompleteFilter.Builder()
                            .setTypeFilter(Place.TYPE_COUNTRY)
                            .setCountry("NL")
                            .build();
                    Intent i = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(filter)
                            .build(getActivity());

                    startActivityForResult(i,PLACE_AUTOCOMPLETE_REQ);

                }catch (GooglePlayServicesRepairableException googlePSRE){
                    Log.d("AJQ RepairableException",googlePSRE.toString());

                }catch (GooglePlayServicesNotAvailableException googlePSNAE){
                    Log.d("AJQ NotAvailable",googlePSNAE.toString());

                }

            }
        });

        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        finishListener = (AttractionEditorListener)getActivity();

        isRestaurantCB = (CheckBox)v.findViewById(R.id.restaurant_checkbox);
        isBarCB = (CheckBox)v.findViewById(R.id.bar_checkbox);
        isCafeCB = (CheckBox)v.findViewById(R.id.cafe_checkbox);
        isCoffeeShopCB = (CheckBox)v.findViewById(R.id.coffee_shop_checkbox);
        isThingToDoCB = (CheckBox)v.findViewById(R.id.thing_to_do_checkbox);
        isPhotoOpCB = (CheckBox)v.findViewById(R.id.photo_op_checkbox);

        attTitleEditTxt = (EditText)v.findViewById(R.id.att_name_edit_text);
        attDescriptionEditTxt =(EditText)v.findViewById(R.id.att_description_edit_text);
        latText = (TextView)v.findViewById(R.id.latitude_edit_text);
        lonText = (TextView) v.findViewById(R.id.longitude_edit_text);



        submitBtn = (Button)v.findViewById(R.id.submit_attraction_button);
        submitBtn.setOnClickListener(new submitListener());

        deleteBtn = (Button)v.findViewById(R.id.delete_suggestion_button);
        if (status.equals(STATE_ATTRACTION_EDITOR_MANAGE_SUGGESTIONS)){
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("Are you sure you wish to delete this suggestion?");
                    alert.setPositiveButton("Yes",deleteRecord);
                    alert.setNegativeButton("No",deleteRecord);
                    alert.show();
                }
            });

        }else{
            deleteBtn.setVisibility(View.GONE);
        }




        findOnMapBtn = (Button)v.findViewById(R.id.find_on_map_button);
        findOnMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(findOnMapBtn.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                finishListener.getCoordsFromMap();
            }
        });

        addImageBtn = (Button)v.findViewById(R.id.add_image_button);
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(addImageBtn.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,IMAGE_SELECTOR_REQ);

            }
        });
        testImageView = (ImageView)v.findViewById(R.id.bitmap_test);

        if (pictureFileLoc!=null){
            putPicInFrame();
        }



        isRestaurantCB.setOnCheckedChangeListener(this);
        isBarCB.setOnCheckedChangeListener(this);
        isCafeCB.setOnCheckedChangeListener(this);
        isCoffeeShopCB.setOnCheckedChangeListener(this);
        isThingToDoCB.setOnCheckedChangeListener(this);
        isPhotoOpCB.setOnCheckedChangeListener(this);


        attributeHolder = (LinearLayout)v.findViewById(R.id.specific_attribute_container);

        vibeView = new AttributeView(getContext());
        vibeView.setTitleId(R.string.card_vibe);

        priceRangeView = new AttributeView(getContext());
        priceRangeView.setTitleId(R.string.card_price_range);

        quickEatOrSitdownView = new AttributeView(getContext());
        quickEatOrSitdownView.setTitleId(R.string.card_eat_type);

        cuisineTypeView = new AttributeView(getContext());
        cuisineTypeView.setTitleId(R.string.card_cuisine);

        specialityView = new AttributeView(getContext());
        specialityView.setTitleId(R.string.card_speciality);

        timeRequiredView = new AttributeView(getContext());
        timeRequiredView.setTitleId(R.string.card_time_required);

        whereToGetTicketsView = new AttributeView(getContext());
        whereToGetTicketsView.setTitleId(R.string.card_where_tickets);

        subjectView = new AttributeView(getContext());
        subjectView.setTitleId(R.string.card_subject);

        thingToDoTypeView = new AttributeView(getContext());
        thingToDoTypeView.setTitleId(R.string.card_type);

        orderSuggestionsView = new AttributeViewListData(getContext());
        orderSuggestionsView.setTitleId(R.string.card_order_suggestions);

        pSuggestionsView = new AttributeViewListData(getContext());
        pSuggestionsView.setTitleId(R.string.card_suggestions);


        setAllUnavailable();

        if (status.equals(STATE_ATTRACTION_EDITOR_EDIT)||status.equals(STATE_ATTRACTION_EDITOR_MANAGE_SUGGESTIONS)){
            addDataFromRecord();
        }

        if (status.equals(STATE_ATTRACTION_EDITOR_SUGGESTION)){
            switch (calledFrom){
                case MainActivity.STATE_RESTAURANTS:
                    isRestaurantCB.setChecked(true);
                    break;
                case MainActivity.STATE_BARS:
                    isBarCB.setChecked(true);
                    break;
                case MainActivity.STATE_CAFES:
                    isCafeCB.setChecked(true);
                    break;
                case MainActivity.STATE_COFFEE_SHOPS:
                    isCoffeeShopCB.setChecked(true);
                    break;
                case MainActivity.STATE_THINGS_TO_DO:
                    isThingToDoCB.setChecked(true);
                    break;
                case MainActivity.STATE_PHOTO_OPPORTUNITIES:
                    isPhotoOpCB.setChecked(true);
                    break;

            }

            updateViews();
        }

        return v;
    }


    private void setAllUnavailable(){
        vibeView.setStatus(FIELD_NOT_VAILABLE);
        priceRangeView.setStatus(FIELD_NOT_VAILABLE);
        quickEatOrSitdownView.setStatus(FIELD_NOT_VAILABLE);
        cuisineTypeView.setStatus(FIELD_NOT_VAILABLE);
        specialityView.setStatus(FIELD_NOT_VAILABLE);
        timeRequiredView.setStatus(FIELD_NOT_VAILABLE);
        whereToGetTicketsView.setStatus(FIELD_NOT_VAILABLE);
        subjectView.setStatus(FIELD_NOT_VAILABLE);
        thingToDoTypeView.setStatus(FIELD_NOT_VAILABLE);
        orderSuggestionsView.setStatus(FIELD_NOT_VAILABLE);
        pSuggestionsView.setStatus(FIELD_NOT_VAILABLE);

    }

    private void setOptional(AttributeView v){
        if (v.getStatus()!=FIELD_REQUIRED){
            v.setStatus(FIELD_OPTIONAL);
        }
    }

    private void setRequired(AttributeView v){
        v.setStatus(FIELD_REQUIRED);

    }




    private void updateViews(){
        setAllUnavailable();
        if (isRestaurantCB.isChecked()){
            setRequired(cuisineTypeView);
            setRequired(quickEatOrSitdownView);
            setOptional(vibeView);
            setOptional(priceRangeView);
            setOptional(specialityView);
            setOptional(orderSuggestionsView);


        }

        if (isBarCB.isChecked()){
            setRequired(vibeView);
            setOptional(specialityView);
            setOptional(priceRangeView);
            setOptional(cuisineTypeView);
            setOptional(orderSuggestionsView);
        }

        if (isCafeCB.isChecked()){
            setRequired(vibeView);
            setOptional(specialityView);
            setOptional(cuisineTypeView);
            setOptional(quickEatOrSitdownView);
            setOptional(priceRangeView);
            setOptional(orderSuggestionsView);

        }

        if (isCoffeeShopCB.isChecked()){
            setRequired(vibeView);
            setOptional(specialityView);
            setOptional(priceRangeView);
            setOptional(orderSuggestionsView);
        }

        if (isThingToDoCB.isChecked()){
            setRequired(thingToDoTypeView);
            setRequired(timeRequiredView);
            setRequired(priceRangeView);
            setOptional(whereToGetTicketsView);
            setOptional(pSuggestionsView);
        }

        if (isPhotoOpCB.isChecked()){
            setRequired(subjectView);
            setOptional(pSuggestionsView);
        }



        handleViewChange(vibeView);
        handleViewChange(cuisineTypeView);
        handleViewChange(quickEatOrSitdownView);
        handleViewChange(priceRangeView);
        handleViewChange(specialityView);
        handleViewChange(orderSuggestionsView);
        handleViewChange(thingToDoTypeView);
        handleViewChange(timeRequiredView);
        handleViewChange(whereToGetTicketsView);
        handleViewChange(subjectView);
        handleViewChange(pSuggestionsView);



    }

    //remove or add views to menu based on check boxes
    private void handleViewChange(AttributeView v){
        if (v.getStatus()!= FIELD_NOT_VAILABLE && !v.getAdded()){
            attributeHolder.addView(v);
            v.setAdded(true);
        }
        if (v.getStatus()==FIELD_NOT_VAILABLE && v.getAdded()){
            attributeHolder.removeView(v);
            v.setAdded(false);
        }

    }


    //if editing a record, add data from current record to menu
    private void addDataFromRecord(){
        current = AttractionList.getInstance().getCurrentAttractionInScope();
        attTitleEditTxt.setText(current.getTitle());
        attDescriptionEditTxt.setText(current.getDescription());
        latText.setText(String.valueOf(current.getLat()));
        lonText.setText(String.valueOf(current.getLon()));

        isRestaurantCB.setChecked(current.isRestaurant());
        isBarCB.setChecked(current.isBar());
        isCoffeeShopCB.setChecked(current.isCoffeeShop());
        isCafeCB.setChecked(current.isCafe());
        isThingToDoCB.setChecked(current.isThingToDo());
        isPhotoOpCB.setChecked(current.isPhotoOpportunity());

        vibeView.setContent(current.getVibe());
        priceRangeView.setContent(current.getPriceRange());
        quickEatOrSitdownView.setContent(current.getQuickEatOrSitdown());
        cuisineTypeView.setContent(current.getCuisineType());
        specialityView.setContent(current.getSpeciality());
        timeRequiredView.setContent(current.getSpeciality());
        whereToGetTicketsView.setContent(current.getWhereToGetTickets());
        subjectView.setContent(current.getSubject());
        thingToDoTypeView.setContent(current.getThingToDoType());
        orderSuggestionsView.setList(current.getOrderSuggestions());
        pSuggestionsView.setList(current.getPhotoOrToDoSuggestions());

        updateViews();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateViews();

    }

    //ensure all 'required' fields are filled
    private Boolean checkFieldsLegal(){

        if(attTitleEditTxt.getText().toString().equals("")){
            Toast.makeText(getContext(),"Name cannot be empty",Toast.LENGTH_SHORT).show();
            attTitleEditTxt.requestFocus();
            attTitleEditTxt.getParent().requestChildFocus(attTitleEditTxt,attTitleEditTxt);
            return false;
        }

        if(attDescriptionEditTxt.getText().toString().equals("")){
            Toast.makeText(getContext(),"Description cannot be empty",Toast.LENGTH_SHORT).show();
            attDescriptionEditTxt.requestFocus();
            attDescriptionEditTxt.getParent().requestChildFocus(attDescriptionEditTxt,attDescriptionEditTxt);
            return false;
        }

        if (latText.getText().toString().equals("")){
            Toast.makeText(getContext(),"Latitude cannot be empty",Toast.LENGTH_SHORT).show();
            latText.getParent().requestChildFocus(latText,latText);

        }

        if (lonText.getText().toString().equals("")){
            Toast.makeText(getContext(),"Longitude cannot be empty",Toast.LENGTH_SHORT).show();
            lonText.getParent().requestChildFocus(lonText,lonText);
            return false;
        }

        if (!attributeViewRequiredCheck(vibeView))
            return false;
        if (!attributeViewRequiredCheck(cuisineTypeView))
            return false;
        if (!attributeViewRequiredCheck(quickEatOrSitdownView))
            return false;
        if (!attributeViewRequiredCheck(priceRangeView))
            return false;
        if (!attributeViewRequiredCheck(thingToDoTypeView))
            return false;
        if (!attributeViewRequiredCheck(timeRequiredView))
            return false;
        if (!attributeViewRequiredCheck(subjectView))
            return false;

        return true;


    }

    //method to test if field is required and if it is blank, throw error and display toast
    public boolean attributeViewRequiredCheck(AttributeView v){
        if (v.getStatus()==FIELD_REQUIRED&&v.getContent().equals("")){
            String error = getResources().getString(v.getTitleID()) + " cannot be empty";
            Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
            v.editTextFocus();
            v.getParent().requestChildFocus(v,v);
            return false;
        }
        return true;
    }

    public void setCoords(LatLng pos){
        latText.setText(String.valueOf(pos.getLatitude()));
        lonText.setText(String.valueOf(pos.getLongitude()));
        Log.d("Ajq",pos.toString());
        Log.d("AJQ","cock suckers");


    }

    private void putPicInFrame(){
     /**   Bitmap pictureOfAttraction = BitmapDecoder.decodeSampledBitmapFromResourceSetValues(getContext(),pictureFileLoc,8);

        ByteArrayOutputStream likeABaos = new ByteArrayOutputStream();
        Log.d("Ajq","here");
        pictureOfAttraction.compress(Bitmap.CompressFormat.JPEG,50,likeABaos);
        pictureOfAttraction.recycle();
        byte[] dataBytes = likeABaos.toByteArray();

        Bitmap test = BitmapFactory.decodeByteArray(dataBytes,0,dataBytes.length);
        Log.d("Ajq","test no byes is: "+ test.getByteCount());
**/
        testImageView.getLayoutParams().height = 500;
        Bitmap selectedImage = BitmapDecoder.decodeBitmapByFrameSize(getContext(),pictureFileLoc,testImageView.getHeight(),testImageView.getWidth());
        testImageView.setImageBitmap(selectedImage);
    }


    //activity results for picture selector and place autocomplete
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IMAGE_SELECTOR_REQ:
                if (resultCode== Activity.RESULT_OK){
                    pictureFileLoc = data.getData();
                    putPicInFrame();

                    break;
                }
            case PLACE_AUTOCOMPLETE_REQ:
                if (resultCode==Activity.RESULT_OK){
                    Place place = PlaceAutocomplete.getPlace(getContext(),data);
                    attTitleEditTxt.setText(place.getName());
                    latText.setText(String.valueOf(place.getLatLng().latitude));
                    lonText.setText(String.valueOf(place.getLatLng().longitude));

                }else if(resultCode==PlaceAutocomplete.RESULT_ERROR){
                    Toast.makeText(getContext(),"Unable to load Location",Toast.LENGTH_SHORT).show();

                }




        }
    }
    class submitListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            imm.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


            if (!checkFieldsLegal()){
                return;
            }

            //assign edittexts to appropriate variables
            title = attTitleEditTxt.getText().toString();
            lat = Double.parseDouble(latText.getText().toString());
            lon = Double.parseDouble(lonText.getText().toString());
            description = attDescriptionEditTxt.getText().toString();
            vibe = vibeView.getContent();
            cuisineType = cuisineTypeView.getContent();
            priceRange = priceRangeView.getContent();
            quickEatOrSitdown = quickEatOrSitdownView.getContent();
            speciality = specialityView.getContent();
            timeRequired = timeRequiredView.getContent();
            whereToGetTickets = whereToGetTicketsView.getContent();
            subject=subjectView.getContent();
            thingToDoType = thingToDoTypeView.getContent();
            orderSuggestions = orderSuggestionsView.getList();
            photoToDoSuggestions = pSuggestionsView.getList();

            //use attraction builder to build attraction object

            Attraction.AttractionBuilder builder = new Attraction.AttractionBuilder(title,lat,lon,description);
            if (isRestaurantCB.isChecked())
                builder=builder.addRestaurant(cuisineType,vibe,speciality,quickEatOrSitdown,priceRange,orderSuggestions);
            if (isBarCB.isChecked())
                builder=builder.addBar(vibe,speciality,cuisineType,priceRange,orderSuggestions);
            if (isCafeCB.isChecked())
                builder=builder.addCafe(vibe,speciality,cuisineType,quickEatOrSitdown,priceRange,orderSuggestions);
            if (isCoffeeShopCB.isChecked())
                builder=builder.addCoffeeShop(vibe,speciality,priceRange,orderSuggestions);
            if (isThingToDoCB.isChecked())
                builder=builder.addThingToDo(thingToDoType,priceRange,timeRequired,whereToGetTickets,photoToDoSuggestions);
            if (isPhotoOpCB.isChecked())
                builder=builder.addPhotoOpporunity(subject,photoToDoSuggestions);


            Attraction temp = builder.getAttraction();

            if (status.equals(STATE_ATTRACTION_EDITOR_CREATE)){

                DatabaseReference newRecord = FirebaseDatabase.getInstance().getReference().child(GetFirebaseData.ATTRACTIONS_REFERENCE).push();
                temp.setId(newRecord.getKey());
                newRecord.setValue(temp);
                Toast.makeText(getContext(),"Successfully added",Toast.LENGTH_LONG).show();


            }else if(status.equals(STATE_ATTRACTION_EDITOR_EDIT)){
                temp.setId(current.getId());
                temp.setPictureLocationSmall(current.getPictureLocationSmall());
                temp.setPictureLocationLarge(current.getPictureLocationLarge());
                FirebaseDatabase.getInstance().getReference().child(GetFirebaseData.ATTRACTIONS_REFERENCE).child(temp.getId()).setValue(temp);
                Toast.makeText(getContext(), "Attraction successfully edited", Toast.LENGTH_LONG).show();


            }else if(status.equals(STATE_ATTRACTION_EDITOR_SUGGESTION)){
                DatabaseReference newSuggestion = FirebaseDatabase.getInstance().getReference().child(GetFirebaseData.SUGGESTIONS_REFERENCE).push();
                Toast.makeText(getContext(), "Suggestion has been submitted", Toast.LENGTH_LONG).show();
                temp.setId(newSuggestion.getKey());
                newSuggestion.setValue(temp);

            }else if(status.equals(STATE_ATTRACTION_EDITOR_MANAGE_SUGGESTIONS)){
                temp.setId(current.getId());
                temp.setPictureLocationSmall(current.getPictureLocationSmall());
                temp.setPictureLocationLarge(current.getPictureLocationLarge());

                FirebaseDatabase.getInstance().getReference().child(GetFirebaseData.ATTRACTIONS_REFERENCE)
                        .child(temp.getId()).setValue(temp);
                FirebaseDatabase.getInstance().getReference().child(GetFirebaseData.SUGGESTIONS_REFERENCE)
                        .child(current.getId()).removeValue();

                Toast.makeText(getContext(), "Suggestion successfully added", Toast.LENGTH_LONG).show();
            }

            if (pictureFileLoc!=null) {
                UploadPictures picUpload = new UploadPictures(temp, pictureFileLoc,getContext());
                picUpload.execute();
            }

            finishListener.editorFinish();

        }
    }

    Dialog.OnClickListener deleteRecord = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    finishListener.deleteSuggestion(current);
            }
        }
    };

    public interface AttractionEditorListener {
        void editorFinish();
        void getCoordsFromMap();
        void deleteSuggestion(Attraction current);
    }
}
