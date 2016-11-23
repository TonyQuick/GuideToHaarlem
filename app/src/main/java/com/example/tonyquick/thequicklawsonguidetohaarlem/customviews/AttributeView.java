package com.example.tonyquick.thequicklawsonguidetohaarlem.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.AttractionEditorFragment;

/**
 * Created by Tony Quick on 10/11/2016.
 */

public class AttributeView extends FrameLayout {
    Context c;
    TextView title;
    EditText content;
    Boolean isAdded=false;
    View v;

    int titleID, status;


    public AttributeView(Context context) {
        super(context);
        init(context);
    }

    public AttributeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AttributeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context c){
        v = inflate(c, R.layout.layout_attribute_edit,null);
        this.addView(v);
        title = (TextView)v.findViewById(R.id.attribute_title1);
        content = (EditText)v.findViewById(R.id.attribute_edit_text);


    }



    public void setTitleId(int titleId){
        this.titleID=titleId;
        title.setText(getContext().getResources().getString(titleID));
    }

    public int getTitleID() {
        return titleID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        if(this.status== AttractionEditorFragment.FIELD_REQUIRED){
            title.setText(titleID);
            title.append("*");
            title.setTypeface(Typeface.DEFAULT_BOLD);
        }else{
            title.setText(titleID);
            title.setTypeface(Typeface.DEFAULT);
        }

    }
    public String getContent(){
        if (content.getText().toString()!="") {
            return content.getText().toString();
        } else{
            return null;
        }
    }

    public void setContent(String data){
        this.content.setText(data);

    }

    public void editTextFocus(){
        content.requestFocus();
    }

    public Boolean getAdded() {
        return isAdded;
    }

    public void setAdded(Boolean added) {
        isAdded = added;
    }
}
