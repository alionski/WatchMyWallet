package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import layout.util.CustomButton;
import se.mah.aliona.watchmywallet.MainActivity;
import se.mah.aliona.watchmywallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener {
    private TextView mMessageText;
    private EditText mNewName;
    private EditText mNewSurname;
    private CustomButton mDoneButton;
    private String mNewNameString;
    private String mNewSurnameString;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity){
            mMainActivity = (MainActivity) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cleanContainer(container);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initialiseUI(view);
        return view;
    }

    private void initialiseUI(View view) {

        mMessageText = view.findViewById(R.id.tv_settings_message);
        setMessage();

        mNewName = view.findViewById(R.id.et_settings_name);
        mNewSurname = view.findViewById(R.id.et_settings_surname);

        mDoneButton = view.findViewById(R.id.btn_settings_done);
        mDoneButton.setOnClickListener(this);
    }

    private void setMessage() {
        String message = "Having an identity crisis,\n" + mName + " " + mSurname +
                "?\nWant to hide your taxes? \nAnother interesting reason? \nChange your name and surname here:";
        mMessageText.setText(message);
        Log.i(this.toString(), message);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.appbar_button_refresh_transfers).setVisible(false);
        menu.findItem(R.id.appbar_button_refresh_charts).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        mNewNameString = mNewName.getText().toString();
        mNewSurnameString = mNewSurname.getText().toString();
        mMainActivity.editPreferences(mNewNameString, mNewSurnameString);
        mMainActivity.initialisePreferences();

        setUserName(mMainActivity.getUserName(), mMainActivity.getUserSurname());
        setMessage();
    }


}