package se.mah.aliona.watchmywallet;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class ScannerActivity extends Activity {
    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        mResult = findViewById(R.id.textView);
    }

}
