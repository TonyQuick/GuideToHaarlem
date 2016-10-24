package com.example.tonyquick.thequicklawsonguidetohaarlem.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Tony Quick on 19/10/2016.
 */

public class SpacingDecorator extends RecyclerView.ItemDecoration {

    private int space;
    private String type;
    public static final String SPACE_DECORATOR_ORIENTATION_HORIZONTAL = "horizontal";
    public static final String SPACE_DECORATOR_ORIENTATION_VERTICAL = "vertical";


    public SpacingDecorator(int space, String type) {
        this.space = space;
        this.type = type;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (type.equals(SPACE_DECORATOR_ORIENTATION_HORIZONTAL)) {
            outRect.right = space;
        }else if (type.equals(SPACE_DECORATOR_ORIENTATION_VERTICAL)){
            outRect.top = space;
        }

    }
}
