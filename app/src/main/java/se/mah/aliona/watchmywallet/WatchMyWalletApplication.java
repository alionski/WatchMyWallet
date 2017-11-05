package se.mah.aliona.watchmywallet;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Extended Application class. Needed only to launch LeakCanary.
 * Created by aliona on 2017-10-24.
 */

public class WatchMyWalletApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
