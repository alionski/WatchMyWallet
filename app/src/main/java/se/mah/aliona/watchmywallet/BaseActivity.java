package se.mah.aliona.watchmywallet;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by aliona on 2017-09-23.
 */

public abstract class BaseActivity extends AppCompatActivity {


//    public void initialisePreferences() {
//        Resources res = getResources();
//        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
//                res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//        mUserName = sharedPref.getString(res.getString(R.string.username), null);
//        mUserSurname = sharedPref.getString(res.getString(R.string.usersurname), null);
//    }
//
//    public void editPreferences(String newName, String newSurname) {
//        Resources res = getResources();
//        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
//                res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(res.getString(R.string.username), newName);
//        editor.putString(res.getString(R.string.usersurname), newSurname);
//        editor.commit();
//    }
}
