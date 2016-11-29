package com.example.tonyquick.thequicklawsonguidetohaarlem.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;


/**
 * Created by Tony Quick on 23/11/2016.
 */

public class SeparatingLine extends LinearLayout{

    Context context;
    View line;

    public SeparatingLine(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public SeparatingLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeparatingLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){

        View temp = inflate(context, R.layout.separating_line,null);
        this.addView(temp);
        line = temp.findViewById(R.id.sep_line_view);

    }

    public void setColorWhite(){
        line.setBackgroundResource(R.color.white_color);
    }
}
