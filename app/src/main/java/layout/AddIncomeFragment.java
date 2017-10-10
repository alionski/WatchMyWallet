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

import se.mah.aliona.watchmywallet.MainActivity;
import se.mah.aliona.watchmywallet.R;
import se.mah.aliona.watchmywallet.beans.Income;
import se.mah.aliona.watchmywallet.database.Contract;

/**
 * A simple {@link Fragment} subclass.
 */

public class AddIncomeFragment extends BaseFragment {
    private EditText mEtTitle;
    private EditText mEtSum;
    private Spinner mSpinnerCategory;
    private Button mButtonPickDate;
    private Button mButtonDone;

    private Income mNewIncome;
    private String mTitle;
    private long mDate;
    private int mCategory;
    private double mAmount;

    public AddIncomeFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_income, container, false);
        initialiseUI(view);
        setHasOptionsMenu(true);
        cleanContainer(container);
        return view;
    }

    private void initialiseUI(View view) {
        mEtTitle = view.findViewById(R.id.edit_text_title_new_income_fragment);
        mEtSum = view.findViewById(R.id.edit_text_sum_new_income_fragment);
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
        mButtonDone = view.findViewById(R.id.button_done_new_income_fragment);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputOK()) {
                    mMainActivity.saveIncomeToDB(mNewIncome);
                }
            }
        });
    }

    private boolean inputOK() {
        mTitle = mEtTitle.getText().toString();
        String amountText = mEtSum.getText().toString();

        if ( (mTitle == null || mTitle.equals(""))
                || (amountText == null || amountText.equals(""))
                || mDate == 0
                || mCategory == 0) {
            Snackbar snackbar = Snackbar.make(this.getView(),
                    "One of the properties is missing!",
                    Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        mNewIncome = new Income();
        mNewIncome.setIncTitle(mTitle);
        mAmount = Double.parseDouble(amountText);
        mNewIncome.setIncAmount(mAmount);
        mNewIncome.setIncCatId(mCategory);
        mNewIncome.setIncDate(mDate);

        Log.i(this.toString(),
                "\n" + mNewIncome.getIncTitle() + "\n" + mNewIncome.getIncAmount() + "\n"
        + mNewIncome.getIncCatId() + "\n" + mNewIncome.getIncDate());

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
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.appbar_button_refresh_transfers).setVisible(false);
        menu.findItem(R.id.appbar_button_refresh_charts).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
