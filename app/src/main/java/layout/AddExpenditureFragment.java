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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

import layout.util.CustomButton;
import se.mah.aliona.watchmywallet.MainActivity;
import se.mah.aliona.watchmywallet.R;
import se.mah.aliona.watchmywallet.beans.WalletBarcode;
import se.mah.aliona.watchmywallet.beans.Expenditure;
import se.mah.aliona.watchmywallet.database.Contract;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenditureFragment extends BaseFragment {
    private final String TITLE = "title";
    private final String SUM = "sum";
    private final String CATEGORY = "category";
    private final String DATE = "date";
    private final String BARCODE = "barcode";
    private final String BARCODE_NUMBER = "barcode_num";
    private final String NEW_BARCODE = "new_barcode";

    private TextView mBarcodeLabel;
    private TextView mBarcodeValue;
    private EditText mEtTitle;
    private EditText mEtCost;
    private Spinner mSpinnerCategory;
    private Button mButtonPickDate;
    private String mTitle = "";
    private long mDate;
    private int mCategory;
    private double mAmount;
    private String mAmountString = "";

    private WalletBarcode mBarcode;
    private long mBarcodeNumber;

    private boolean mNewBarcode = true;
    private Cursor mCategoryCursor;

    public AddExpenditureFragment() {
        // Required empty public constructor
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
        mBarcode = (WalletBarcode) savedInstanceState.getSerializable(BARCODE);
        mBarcodeNumber = savedInstanceState.getLong(BARCODE_NUMBER);
        mNewBarcode = savedInstanceState.getBoolean(NEW_BARCODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(this.toString(), "ON CREATE VIEW");
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
        View view = inflater.inflate(R.layout.fragment_add_expenditure, container, false);
        cleanContainer(container);
        setHasOptionsMenu(true);
        initialiseUI(view);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.appbar_button_refresh_transfers).setVisible(false);
        menu.findItem(R.id.appbar_button_refresh_charts).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private void initialiseUI(View view) {
        mBarcodeLabel = view.findViewById(R.id.barcode_label_text_view);
        mBarcodeValue = view.findViewById(R.id.barcode_value_text_view);

        mBarcodeValue.setVisibility(View.INVISIBLE);
        mBarcodeLabel.setVisibility(View.INVISIBLE);

        mEtTitle = view.findViewById(R.id.edit_text_title_new_exp_fragment);
        mEtCost = view.findViewById(R.id.edit_text_sum_new_exp_fragment);

        if (mNewBarcode && mBarcodeNumber != 0) {
            showBarcode(String.valueOf(mBarcodeNumber));
        } else if (mBarcode != null) {
            showBarcode(mBarcode);
        }

        mSpinnerCategory = view.findViewById(R.id.spinner_cat_new_exp_fragment);
        initialiseSpinner();

        mButtonPickDate = view.findViewById(R.id.button_pick_date_new_exp_fragment);
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

        ImageButton scannerButton = view.findViewById(R.id.button_scan_barcode);
        scannerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mMainActivity.scanBarcode();
            }
        });

        CustomButton buttonDone = view.findViewById(R.id.button_done_new_exp_fragment);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputOK()) {
                    saveExp();
                }
            }
        });
    }

    private void initialiseSpinner() {
        mCategoryCursor = mMainActivity.getDBController().getExpenditureCategories();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                mCategoryCursor,
                new String[] { Contract.ExpCats.COLUMN_EXP_CAT_NAME },
                new int[] { android.R.id.text1 },
                1);
        mSpinnerCategory.setAdapter(adapter);
        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mSpinnerCategory.getSelectedItem();
                mCategory = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.ExpCats._ID)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        mSpinnerCategory.setSelection(mCategory);
    }

    private void saveExp() {
        Expenditure newExpenditure = new Expenditure();
        newExpenditure.setExpTitle(mTitle);
        newExpenditure.setExpCost(mAmount);
        newExpenditure.setExpCatId(mCategory);
        newExpenditure.setExpDate(mDate);

        if (mNewBarcode && mBarcode == null) {
            mMainActivity.saveExpenditureToDB(newExpenditure, mBarcodeNumber);
        } else if (mBarcode != null){
            mMainActivity.saveExpenditureToDB(newExpenditure, mBarcode.getBarcodeID());
        } else {
            mMainActivity.saveExpenditureToDB(newExpenditure);
        }
    }

    private void cleanUI() {
        mEtCost.setText("");
        mEtTitle.setEnabled(true);
        mEtTitle.setText("");
        mSpinnerCategory.setSelection(0);
        mBarcodeLabel.setVisibility(View.INVISIBLE);
        mBarcodeValue.setText("");
        mBarcodeValue.setVisibility(View.INVISIBLE);
        mBarcode = null;
        mBarcodeNumber = 0;
        mDate = 0;
    }

    private boolean inputOK() {
        mTitle = mEtTitle.getText().toString();
        mAmount = Double.parseDouble(mEtCost.getText().toString());

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

    public void setBarcodeResult(String displayValue) {
        mBarcodeNumber = Long.parseLong(displayValue);

        showBarcode(displayValue);
    }

    public void setFoundBarcode(WalletBarcode barcode) {
        mBarcode = barcode;
        mNewBarcode = false;

        showBarcode(barcode);
    }

    private void showBarcode(String displayValue) {
        mBarcodeLabel.setVisibility(View.VISIBLE);
        mBarcodeValue.setVisibility(View.VISIBLE);
        mBarcodeValue.setText(displayValue);
    }

    private void showBarcode(WalletBarcode barcode) {
        mBarcodeLabel.setVisibility(View.VISIBLE);
        mBarcodeValue.setVisibility(View.VISIBLE);
        mBarcodeValue.setText(String.valueOf(barcode.getBarcodeNumber()));

        mEtTitle.setText(barcode.getProductName());
        mEtTitle.setEnabled(false);
        mEtCost.setText(String.valueOf(barcode.getInitialPrice()));
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
        cleanUI();
        Log.i(this.toString(), "ON STOP");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCategoryCursor.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(this.toString(), "ON SAVE INSTANCE");
        super.onSaveInstanceState(outState);
        outState.putString(TITLE, mEtTitle.getText().toString());
        outState.putLong(DATE, mDate);
        outState.putInt(CATEGORY, mCategory);
        outState.putString(SUM, mEtCost.getText().toString());
        outState.putSerializable(BARCODE, mBarcode);
        outState.putLong(BARCODE_NUMBER, mBarcodeNumber);
        outState.putBoolean(NEW_BARCODE, mNewBarcode);
    }
}

