package com.example.tonyquick.thequicklawsonguidetohaarlem.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Tony Quick on 19/10/2016.
 */

public class HorizontalSpaceDecorator extends RecyclerView.ItemDecoration {

    private int spaceRight;

    public HorizontalSpaceDecorator(int spaceRight) {
        this.spaceRight = spaceRight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right=spaceRight;
    }
}
