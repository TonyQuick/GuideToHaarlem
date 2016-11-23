package com.example.tonyquick.thequicklawsonguidetohaarlem.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 10/11/2016.
 */

public class AttributeViewListData extends AttributeView{

    TextView contentText;
    ArrayList<String> contentList;
    ImageButton addButton, deleteButton;

    int titleID;

    public AttributeViewListData(Context context) {
        super(context);
    }

    public AttributeViewListData(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AttributeViewListData(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(Context c) {

        v=inflate(c, R.layout.layout_attribute_edit_multiple,null);
        this.addView(v);

        contentList = new ArrayList<>();
        title = (TextView)v.findViewById(R.id.attribute_title1);
        contentText = (TextView)v.findViewById(R.id.attribute_contents_text);
        updateContentDisplay();
        content = (EditText) v.findViewById(R.id.suggestion_edit_text);
        addButton = (ImageButton)v.findViewById(R.id.add_suggestion_button);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!content.getText().toString().equals("")){
                    contentList.add(content.getText().toString());
                    updateContentDisplay();
                    content.setText("");
                }


            }
        });
        deleteButton = (ImageButton)v.findViewById(R.id.delete_suggestion_button);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contentList.size()>0){
                    contentList.remove(contentList.size()-1);
                    updateContentDisplay();

                }
            }
        });



    }

    public ArrayList<String> getList() {
        if (contentList.size()>0) {
            return contentList;
        }else{
            return null;
        }
    }

    public void setList(ArrayList<String> content) {
        this.contentList = content;
        updateContentDisplay();
    }

    public void updateContentDisplay(){
        if (contentList.size()==0) {
            contentText.setText("No suggestions to display");

        }else{
            contentText.setText(contentList.get(0));
            for(int i = 1; i< contentList.size(); i++){
                contentText.append(System.getProperty("line.separator"));
                contentText.append(contentList.get(i));
            }
        }

    }



}
