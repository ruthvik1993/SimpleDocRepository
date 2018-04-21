package apnacomplex.docrepository_anjaneya.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TextView_RobotoLight extends AppCompatTextView {
    public TextView_RobotoLight(Context context) {
        super(context);
        init();
    }

    public TextView_RobotoLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextView_RobotoLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_light.ttf");
            setTypeface(tf, Typeface.NORMAL);
        }

    }
}