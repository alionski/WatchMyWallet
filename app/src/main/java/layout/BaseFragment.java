package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ViewGroup;

import se.mah.aliona.watchmywallet.MainActivity;

/**
 * Created by aliona on 2017-09-23.
 */

public abstract class BaseFragment extends Fragment {

    public static final int ALL = 0;
    public static final int EXPENDITURES = 1;
    public static final int INCOME = 2;

    protected MainActivity mMainActivity;
    protected String mName;
    protected String mSurname;

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.i(this.toString(), "ON ACTIVITY CREATED");
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(this.toString(), "ON ATTACH");

        if (context instanceof MainActivity){
            mMainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMainActivity = null;
    }

    public void setUserName(String name, String surname) {
        mName = name;
        mSurname = surname;
    }

    protected void cleanContainer(ViewGroup container) {
        if (container != null) {
            container.removeAllViews();
        }
    }

    protected abstract void restoreState(Bundle savedInstanceState);
}
