package layout.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import se.mah.aliona.watchmywallet.R;

/**
 * Created by aliona on 2017-10-10.
 */

public class CustomButton extends android.support.v7.widget.AppCompatButton {
    public CustomButton(Context context) {
        super(context);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccentGreen));
    }
}
