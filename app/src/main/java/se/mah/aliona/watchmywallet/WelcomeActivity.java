package se.mah.aliona.watchmywallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import layout.util.CustomButton;

/**
 * The first activity that the user sees, if the user opens the app for the first time.
 * Its only purpose is to ask for a name and surname, then it starts the main activity.
 */

public class WelcomeActivity extends AppCompatActivity {
    private EditText mEtName;
    private EditText mEtSurname;
    private CustomButton mBtnContinue;
    private String mUserName;
    private String mUserSurname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initialisePreferences();
        if (newUser()) {
            initialiseUI();
        } else {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean newUser() {
        return (mUserName == null || mUserSurname == null);
    }

    private void initialiseUI() {
        mEtName = findViewById(R.id.et_name);
        mEtSurname = findViewById(R.id.et_surname);
        mBtnContinue = findViewById(R.id.btn_continue);
        mBtnContinue.setOnClickListener(new ContinueListener());
    }

    private class ContinueListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == mBtnContinue) {
                if (inputOk()) {
                    setUsernameSurname(mEtName.getText().toString(), mEtSurname.getText().toString());
                    startMainActivity();
                }
            }
        }

        private boolean inputOk() {
            boolean inputOk = true;
            if (TextUtils.isEmpty(mEtName.getText())) {
                inputOk = false;
                mEtName.setError("Add your name.");
            }
            if (TextUtils.isEmpty(mEtSurname.getText())) {
                inputOk = false;
                mEtSurname.setError("Add your surname.");
            }
            return inputOk;
        }
    }

    public void initialisePreferences() {
        Resources res = getResources();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mUserName = sharedPref.getString(res.getString(R.string.username), null);
        mUserSurname = sharedPref.getString(res.getString(R.string.usersurname), null);
    }

    private void setUsernameSurname(String name, String surname) {
        Resources res = getResources();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(res.getString(R.string.username), name);
        editor.putString(res.getString(R.string.usersurname), surname);
        editor.apply();
    }
}
