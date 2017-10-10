package layout;

import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import se.mah.aliona.watchmywallet.MainActivity;

/**
 * Created by aliona on 2017-09-23.
 */

public abstract class BaseFragment extends Fragment {
    protected MainActivity mMainActivity;
    protected String mName;
    protected String mSurname;

    public void setUserName(String name, String surname) {
        mName = name;
        mSurname = surname;
    }

    protected void cleanContainer(ViewGroup container) {
        if (container != null) {
            container.removeAllViews();
        }
    }

//    public void setMainActivity(MainActivity activity) {
//        mMainActivity = activity;
//    }
}
