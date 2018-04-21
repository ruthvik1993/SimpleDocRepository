package apnacomplex.docrepository_anjaneya.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ruthvik on 7/9/2017.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public GridSpacingItemDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public GridSpacingItemDecoration(@NonNull Context context, int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}
