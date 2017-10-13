package layout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Calendar;
import java.util.TimeZone;

import layout.util.CustomButton;
import se.mah.aliona.watchmywallet.MainActivity;
import se.mah.aliona.watchmywallet.R;
import se.mah.aliona.watchmywallet.beans.Income;
import se.mah.aliona.watchmywallet.database.Contract;

/**
 * A simple {@link Fragment} subclass.
 */

public class AddIncomeFragment extends BaseFragment {
    private final String TITLE = "title";
    private final String SUM = "sum";
    private final String CATEGORY = "category";
    private final String DATE = "date";

    private EditText mEtTitle;
    private EditText mEtSum;
    private Spinner mSpinnerCategory;
    private Button mButtonPickDate;

    private Income mNewIncome;
    private String mTitle = "";
    private long mDate = 0;
    private int mCategory = 0;
    private double mAmount = 0;
    private String mAmountString = "";

    public AddIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(this.toString(), "ON ACTIVITY CREATED");
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(this.toString(), "ON ATTACH");

        if (context instanceof MainActivity){
            mMainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(this.toString(), "ON START");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(this.toString(), "ON RESUME");
    }

    protected void restoreState(Bundle savedInstanceState) {
        Log.i(this.toString(), "Saved instance is not null!!!!!!!!!!!!!!!");
        mTitle = savedInstanceState.getString(TITLE);
        mAmountString = savedInstanceState.getString(SUM);
        mDate = savedInstanceState.getLong(DATE);
        mCategory = savedInstanceState.getInt(CATEGORY);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(this.toString(), "ON CREATE VIEW");
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
        View view = inflater.inflate(R.layout.fragment_add_income, container, false);
        initialiseUI(view);
        setHasOptionsMenu(true);
        cleanContainer(container);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.appbar_button_refresh_transfers).setVisible(false);
        menu.findItem(R.id.appbar_button_refresh_charts).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private void initialiseUI(View view) {
        mEtTitle = view.findViewById(R.id.edit_text_title_new_income_fragment);
        mEtTitle.setText(mTitle);
        mEtSum = view.findViewById(R.id.edit_text_sum_new_income_fragment);

        mEtSum.setText(mAmountString);

        mSpinnerCategory = view.findViewById(R.id.spinner_cat_new_income_fragment);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                mMainActivity.getDBController().getIncomeCategories(),
                new String[] { Contract.IncCats.COLUMN_INC_CAT_NAME },
                new int[] { android.R.id.text1 },
                1);

        mSpinnerCategory.setAdapter(adapter);
        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mSpinnerCategory.getSelectedItem();
                mCategory = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.IncCats._ID)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        mSpinnerCategory.setSelection(mCategory);

        mButtonPickDate = view.findViewById(R.id.button_pick_date_new_income_fragment);
        mButtonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog picker = new DatePickerDialog(getContext(), new DatePickerListener(),
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }
        });

        if (mDate != 0) {
            mButtonPickDate.setText(MainActivity.prettify(mDate));
        }

        CustomButton buttonDone = view.findViewById(R.id.button_done_new_income_fragment);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputOK()) {
                    saveIncome();
                    cleanUI();
                }
            }
        });
    }

    private void cleanUI() {
        mEtSum.setText("");
        mEtTitle.setText("");
        mSpinnerCategory.setSelection(0);
        mDate = 0;
    }

    private void saveIncome() {
        mNewIncome = new Income();
        mNewIncome.setIncTitle(mTitle);
        mNewIncome.setIncAmount(mAmount);
        mNewIncome.setIncCatId(mCategory);
        mNewIncome.setIncDate(mDate);

//        Log.i(this.toString(),
//                "\n" + mNewIncome.getIncTitle() + "\n" + mNewIncome.getIncAmount() + "\n"
//                        + mNewIncome.getIncCatId() + "\n" + mNewIncome.getIncDate());
        mMainActivity.saveIncomeToDB(mNewIncome);
    }

    private boolean inputOK() {
        mTitle = mEtTitle.getText().toString();
        mAmount = Double.parseDouble(mEtSum.getText().toString());

        if ( (mTitle == null || mTitle.equals(""))
                || mAmount == 0
                || mDate == 0
                || mCategory == 0) {
            Snackbar snackbar = Snackbar.make(this.getView(),
                    "One of the properties is missing!",
                    Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        return true;
    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            // year, month, day
            String year = String.valueOf(i);
            String month = String.valueOf(i1+1);
            String day = String.valueOf(i2);

            if (month.length() == 1) {
                month = "0".concat(month);
            }
            if (day.length() == 1) {
                day = "0".concat(day);
            }
            StringBuilder stringBuilder = new StringBuilder(year + month +day);
            mDate = Long.valueOf(stringBuilder.toString());
            mButtonPickDate.setText(MainActivity.prettify(mDate));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(this.toString(), "ON PAUSE");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(this.toString(), "ON STOP");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(this.toString(), "ON SAVE INSTANCE");
        super.onSaveInstanceState(outState);

        outState.putString(TITLE, mEtTitle.getText().toString());
        outState.putString(SUM, mEtSum.getText().toString());
        outState.putLong(DATE, mDate);
        outState.putInt(CATEGORY, mCategory);
    }
}
