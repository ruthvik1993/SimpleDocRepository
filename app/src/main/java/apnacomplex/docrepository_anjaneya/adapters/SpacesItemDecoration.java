package apnacomplex.docrepository_anjaneya.adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by prakh on 7/6/2017.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int spanCount;

    public SpacesItemDecoration(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = space;

        // Add top margin only for the top row to avoid double space between items

        if (spanCount == 2) {
            if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }

            // Add left margin only for the left column to avoid double space between items
            if (parent.getChildLayoutPosition(view) % 2 == 0) {
                outRect.left = space;
                outRect.right = space;
            } else {
                outRect.left = 0;
                outRect.right = space;
            }
        }
    }
}
