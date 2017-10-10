package layout;


import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

import se.mah.aliona.watchmywallet.MainActivity;
import se.mah.aliona.watchmywallet.R;
import se.mah.aliona.watchmywallet.database.Contract;
import se.mah.aliona.watchmywallet.database.TransfersRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransfersFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String ALL_TIME_CHECKED = "all_time_checked";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";

    private static final String ALL_TYPES_CHECKED = "all_types_checked";
    private static final String TRANSFER_TYPE = "transfer_type";
    private static final String TRANSFER_CATEGORY = "transfer_category";


    public static final int ALL = -1;
    public static final int EXPENDITURES = 1;
    public static final int INCOME = 2;

    private static int CURSOR_TYPE;
    private Cursor mListViewCursor;
    private Cursor mIncomeCatsCursor;
    private Cursor mExpenditureCatsCursor;
    private SimpleCursorAdapter mIncomeCatsAdapter;
    private SimpleCursorAdapter mExpCatsAdapter;

    private CheckBox mCheckBoxAllTime;
    private boolean mAllTimeChecked = true;

    private CheckBox mCheckBoxAllTypes;
    private boolean mAllTypesChecked = true;

    private Button mStartDateButton;
    private Button mEndDateButton;

    private String[] mTransferTypes = {"Expenses", "Income"};
    private Spinner mTransferTypesSpinner;
    private Spinner mTransferCatSpinner;
    private int mTypeToShow = -1;
    private int mCatToShow = -1;

    private ListView mTransfersListView;

    private FloatingActionButton mButtonNewTransfer;

    private long mStartDate = 0;
    private long mEndDate = 0;

    public TransfersFragment() {
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
    public void onSaveInstanceState(Bundle outState) {
        Log.i(this.toString(), "ON SAVE INSTANCE");
        super.onSaveInstanceState(outState);
        outState.putBoolean(ALL_TIME_CHECKED, mAllTimeChecked);
        outState.putLong(START_DATE, mStartDate);
        outState.putLong(END_DATE, mEndDate);

        outState.putBoolean(ALL_TYPES_CHECKED, mAllTypesChecked);
        outState.putInt(TRANSFER_TYPE, mTypeToShow);
        outState.putInt(TRANSFER_CATEGORY, mCatToShow);
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
    public void onDetach() {
        super.onDetach();
        Log.i(this.toString(), "ON DETACH");
    }

    private void restoreState(Bundle savedInstanceState) {
        Log.i(this.toString(), "Saved instance is not null!!!!!!!!!!!!!!!");

        mAllTimeChecked = savedInstanceState.getBoolean(ALL_TIME_CHECKED);
        mStartDate = savedInstanceState.getLong(START_DATE);
        mEndDate = savedInstanceState.getLong(END_DATE);

        mAllTypesChecked = savedInstanceState.getBoolean(ALL_TYPES_CHECKED);
        mTypeToShow = savedInstanceState.getInt(TRANSFER_TYPE, mTypeToShow);
        mCatToShow = savedInstanceState.getInt(TRANSFER_CATEGORY);
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
        cleanContainer(container);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_transfers, container, false);
        initialiseUI(view);
        return view;
    }

    // Called from MainActivity after the controller has been set bc otherwise everything crashes with NPE!!
    public void initialiseUI(View view) {

        mListViewCursor = reloadDatabase();

        loadSpinnerCursors();
        initialiseSpinners(view);

        mTransfersListView = view.findViewById(R.id.transfers_list_view);
        mTransfersListView.setOnItemClickListener( new TransfersListListener());
        initialiseListView(mListViewCursor);

        mStartDateButton = view.findViewById(R.id.transfers_button_start_date);
        mStartDateButton.setOnClickListener(this);
        mStartDateButton.setEnabled(!mAllTimeChecked);

        mEndDateButton = view.findViewById(R.id.transfers_button_end_date);
        mEndDateButton.setOnClickListener(this);
        mEndDateButton.setEnabled(!mAllTimeChecked);

        mCheckBoxAllTime = view.findViewById(R.id.transfers_check_box_select_all_time);
        mCheckBoxAllTime.setOnCheckedChangeListener(this);
        mCheckBoxAllTime.setChecked(mAllTimeChecked);

        mCheckBoxAllTypes = view.findViewById(R.id.transfers_check_box_select_all_transfer_types);
        mCheckBoxAllTypes.setOnCheckedChangeListener(this);
        mCheckBoxAllTypes.setChecked(mAllTypesChecked);

        mButtonNewTransfer = view.findViewById(R.id.floating_btn_add_transfer);
        mButtonNewTransfer.setOnClickListener(this);

    }

    private void loadSpinnerCursors() {

        Cursor dbCursorInc = mMainActivity.getDBController().getIncomeCategories();
        MatrixCursor extrasInc = new MatrixCursor(new String[] { "_id",
                Contract.IncCats.COLUMN_INC_CAT_NAME });
        extrasInc.addRow(new String[] { String.valueOf(ALL), "All" });
        Cursor[] cursorsInc = { extrasInc, dbCursorInc };
        mIncomeCatsCursor = new MergeCursor(cursorsInc);

        Cursor dbCursorExp = mMainActivity.getDBController().getExpenditureCategories();
        MatrixCursor extrasExp = new MatrixCursor(new String[] { "_id",
                Contract.ExpCats.COLUMN_EXP_CAT_NAME });
        extrasExp.addRow(new String[] { String.valueOf(ALL), "All" });
        Cursor[] cursorsExp = { extrasExp, dbCursorExp };
        mExpenditureCatsCursor = new MergeCursor(cursorsExp);
    }

    private void initialiseSpinners(View view) {
        mTransferTypesSpinner = view.findViewById(R.id.transfers_spinner_transfer_type);
        mTransferCatSpinner = view.findViewById(R.id.transfers_spinner_transfer_category);

        ArrayAdapter<String> transferTypesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                mTransferTypes);
        transferTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTransferTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    mTypeToShow = EXPENDITURES;
                    mCatToShow = ALL;
                    mTransferCatSpinner.setAdapter(mExpCatsAdapter);
                } else if (i == 1) {
                    mTypeToShow = INCOME;
                    mCatToShow = ALL;
                    mTransferCatSpinner.setAdapter(mIncomeCatsAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        mTransferTypesSpinner.setAdapter(transferTypesAdapter);
        ///// PRESELECTING
        mTransferTypesSpinner.setSelection(mTypeToShow);

        mTransferCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mTransferCatSpinner.getSelectedItem();
                mCatToShow = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.IncCats._ID)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        mIncomeCatsAdapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                mIncomeCatsCursor,
                new String[] { Contract.IncCats.COLUMN_INC_CAT_NAME },
                new int[] { android.R.id.text1 },
                1);

        mExpCatsAdapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                mExpenditureCatsCursor,
                new String[] { Contract.ExpCats.COLUMN_EXP_CAT_NAME },
                new int[] { android.R.id.text1 },
                1);

        ///// PRESELECTING
        if (mTypeToShow == EXPENDITURES && ! mAllTypesChecked) {
            mTransferCatSpinner.setAdapter(mExpCatsAdapter);
            mTransferCatSpinner.setSelection(mCatToShow);
        } else if (mTypeToShow == INCOME && !mAllTypesChecked) {
            mTransferCatSpinner.setSelection(mCatToShow);
            mTransferCatSpinner.setAdapter(mIncomeCatsAdapter);
        } else {
            mTransferCatSpinner.setAdapter(mIncomeCatsAdapter);
        }
    }

    private void initialiseListView(Cursor cursor) {
        CursorAdapter adapter = (CursorAdapter) mTransfersListView.getAdapter();
        if (adapter == null) {
            mTransfersListView.setAdapter( new TransfersListAdapter(getContext(),
                    cursor, 1));
        } else {
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
            mListViewCursor = cursor;
        }
    }

    private Cursor reloadDatabase() {
        Log.i(this.toString(), "mTypeToShow = " + mTypeToShow + "\n");
        Cursor cursor = null;
        if (mAllTimeChecked) {
            Log.i(this.toString(), "SHOW ALL TIME");
            if (mAllTypesChecked) {
                CURSOR_TYPE = ALL;
                cursor = mMainActivity.getAllTransfersList();
            } else if (mTypeToShow == EXPENDITURES) {
                CURSOR_TYPE = EXPENDITURES;
                cursor = mMainActivity.getExpenditureList(mCatToShow, 0, 0);
            } else if (mTypeToShow == INCOME) {
                CURSOR_TYPE = INCOME;
                cursor = mMainActivity.getIncomeList(mCatToShow, 0,0);
            }
        } else {
            Log.i(this.toString(), "SHOW SPECIFIC DATES");
            // check if the dates are ok
            if (mStartDate == 0 || mEndDate == 0) {
//                View view = this.getView();
//                if (view != null ) { // means we are dealing with preselection and this fragment is not ready yet
                    Snackbar snackbar = Snackbar.make(this.getView(), "One of the dates is missing!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
//                }
            } else if (mEndDate < mStartDate) {
//                View view = this.getView();
//                if (view != null ) {
                    Snackbar snackbar = Snackbar.make(this.getView(), "End date can't be before start date!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
//                }
            } else  {
                // dates are chosen
                if (mAllTypesChecked) {
                    CURSOR_TYPE = ALL;
                    // show both categories for the required time
                    cursor = mMainActivity.getAllTransfersList(mStartDate, mEndDate);
                } else if (mTypeToShow == EXPENDITURES) {
                    CURSOR_TYPE = EXPENDITURES;
                    cursor = mMainActivity.getExpenditureList(mCatToShow, mStartDate, mEndDate);
                } else if (mTypeToShow == INCOME) {
                    CURSOR_TYPE = INCOME;
                    cursor = mMainActivity.getIncomeList(mCatToShow, mStartDate, mEndDate);
                }
            }
        }
        return cursor;
    }

    private void requestRefreshList() {
        Log.i(this.toString(), "requestRefreshList() reached");

        Cursor oldCursor = mListViewCursor;
        mListViewCursor = reloadDatabase();
        initialiseListView(mListViewCursor);

        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    private class TransfersListAdapter extends CursorAdapter {

        public TransfersListAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_view_item_transfers, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            switch (CURSOR_TYPE) {
                case ALL:
                    bindAll(view, context, cursor);
                    break;
                case EXPENDITURES:
                    bindExpenditures(view, context, cursor);
                    break;
                case INCOME:
                    bindIncome(view, context, cursor);
                    break;
            }
        }

        private void bindAll(View view, Context context, Cursor cursor) {

            ImageView transferCatImg = view.findViewById(R.id.transfers_cat_image);
            TextView transferTitle = view.findViewById(R.id.transfers_title_list_view_item);
            TextView transferAmount = view.findViewById(R.id.transfers_sum_list_view_item);
            TextView transferDate = view.findViewById(R.id.transfers_date_list_view_item);

            String cat = cursor.getString(cursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_CATEGORY));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_TITLE));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_AMOUNT));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_DATE));

            transferTitle.setText(title);
            transferDate.setText(mMainActivity.prettify(date));

            String source = "'" + cursor.getString(cursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_SOURCE)) +"'";
            if (source.equals(TransfersRepository.EXP_SOURCE)) {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.dirtyWhite));
                transferAmount.setTextColor(ContextCompat.getColor(context, R.color.expenditureColor));
                transferAmount.setText("-"+String.valueOf(amount));
            } else if (source.equals(TransfersRepository.INCOME_SOURCE)){
                view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
                transferAmount.setTextColor(ContextCompat.getColor(context, R.color.colorAccentGreen));
                transferAmount.setText(String.valueOf(amount));
            }

            switch (cat) {
                case "Salary":
                    transferCatImg.setImageResource(R.drawable.ic_work_black_24dp);
                    break;
                case "Other":
                    if (source.equals(TransfersRepository.INCOME_SOURCE)) {
                        transferCatImg.setImageResource(R.drawable.ic_card_giftcard_black_24dp);
                    } else {
                        transferCatImg.setImageResource(R.drawable.ic_receipt_black_24dp);
                    }
                    break;
                case "Food":
                    transferCatImg.setImageResource(R.drawable.ic_local_dining_black_24dp);
                    break;
                case "Leisure":
                    transferCatImg.setImageResource(R.drawable.ic_rowing_black_24dp);
                    break;
                case "Travels":
                    transferCatImg.setImageResource(R.drawable.ic_flight_takeoff_black_24dp);
                    break;
                case "Housing":
                    transferCatImg.setImageResource(R.drawable.ic_weekend_black_24dp);
                    break;
            }
        }

        private void bindExpenditures(View view, Context context, Cursor cursor) {

            ImageView transferCatImg = view.findViewById(R.id.transfers_cat_image);
            TextView transferTitle = view.findViewById(R.id.transfers_title_list_view_item);
            TextView transferAmount = view.findViewById(R.id.transfers_sum_list_view_item);
            TextView transferDate = view.findViewById(R.id.transfers_date_list_view_item);

            String cat = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ExpCats.COLUMN_EXP_CAT_NAME));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Exp.COLUMN_NAME_TITLE));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Contract.Exp.COLUMN_NAME_COST));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Exp.COLUMN_NAME_DATE));

            switch (cat) {
                case "Other":
                    transferCatImg.setImageResource(R.drawable.ic_receipt_black_24dp);
                    break;
                case "Food":
                    transferCatImg.setImageResource(R.drawable.ic_local_dining_black_24dp);
                    break;
                case "Leisure":
                    transferCatImg.setImageResource(R.drawable.ic_rowing_black_24dp);
                    break;
                case "Travels":
                    transferCatImg.setImageResource(R.drawable.ic_flight_takeoff_black_24dp);
                    break;
                case "Housing":
                    transferCatImg.setImageResource(R.drawable.ic_weekend_black_24dp);
                    break;
            }
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.dirtyWhite));
            transferAmount.setTextColor(ContextCompat.getColor(context, R.color.expenditureColor));
            transferTitle.setText(title);
            transferAmount.setText("-" + String.valueOf(amount));
            transferDate.setText(mMainActivity.prettify(date));
        }

        private void bindIncome(View view, Context context, Cursor cursor) {

            ImageView transferCatImg = view.findViewById(R.id.transfers_cat_image);
            TextView transferTitle = view.findViewById(R.id.transfers_title_list_view_item);
            TextView transferAmount = view.findViewById(R.id.transfers_sum_list_view_item);
            TextView transferDate = view.findViewById(R.id.transfers_date_list_view_item);

            String cat = cursor.getString(cursor.getColumnIndexOrThrow(Contract.IncCats.COLUMN_INC_CAT_NAME));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Inc.COLUMN_NAME_TITLE));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Contract.Inc.COLUMN_NAME_AMOUNT));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Inc.COLUMN_NAME_DATE));

            switch (cat) {
                case "Salary":
                    transferCatImg.setImageResource(R.drawable.ic_work_black_24dp);
                    break;
                case "Other":
                    transferCatImg.setImageResource(R.drawable.ic_card_giftcard_black_24dp);
                    break;
            }
            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
            transferAmount.setTextColor(ContextCompat.getColor(context, R.color.colorAccentGreen));
            transferTitle.setText(title);
            transferAmount.setText(String.valueOf(amount));
            transferDate.setText(mMainActivity.prettify(date));
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.appbar_button_refresh_charts).setVisible(false);
        menu.findItem(R.id.appbar_button_refresh_transfers).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        if (view == mButtonNewTransfer) {
            mMainActivity.showAddNewTransferPopup();
        } else if (view == mStartDateButton) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog picker = new DatePickerDialog(getContext(), new StartDatePickerListener(),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        } else if (view == mEndDateButton) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog picker = new DatePickerDialog(getContext(), new EndDatePickerListener(),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbar_button_refresh_transfers:
//                Log.i(this.toString(), "onOptionsItemSelected(MenuItem item) reached");
                requestRefreshList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (compoundButton == mCheckBoxAllTypes) {
            if (checked) {
                mAllTypesChecked = true;
                mTransferTypesSpinner.setEnabled(false);
                mTransferCatSpinner.setEnabled(false);
            } else if (!checked) {
                mAllTypesChecked = false;
                mTransferTypesSpinner.setEnabled(true);
                mTransferCatSpinner.setEnabled(true);
            }
        } else if (compoundButton == mCheckBoxAllTime) {
            if (checked) {
                mAllTimeChecked = true;
                mStartDateButton.setEnabled(false);
                mEndDateButton.setEnabled(false);
            } else if (!checked) {
                mAllTimeChecked = false;
                mStartDateButton.setEnabled(true);
                mEndDateButton.setEnabled(true);
            }
        }
    }

    private class StartDatePickerListener implements DatePickerDialog.OnDateSetListener {

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
            mStartDate = Long.valueOf(stringBuilder.toString());
            mStartDateButton.setText(mMainActivity.prettify(mStartDate));
        }
    }

    private class EndDatePickerListener implements DatePickerDialog.OnDateSetListener {

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
            mEndDate = Long.valueOf(stringBuilder.toString());
            mEndDateButton.setText(mMainActivity.prettify(mEndDate));
        }
    }

    private class TransfersListListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mListViewCursor.moveToPosition(position);
            Log.i(this.toString(), "POSITION: " + position);
            switch(CURSOR_TYPE) {
                case ALL:
                    String source = "'" + mListViewCursor.getString(mListViewCursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_SOURCE)) +"'";
//                    _id = mListViewCursor.getInt(mListViewCursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_ID));
                    if (source.equals(TransfersRepository.EXP_SOURCE)) {
                        mMainActivity.showTransferDetail((int) id, EXPENDITURES);
                    } else if (source.equals(TransfersRepository.INCOME_SOURCE)){
                        mMainActivity.showTransferDetail((int) id, INCOME);
                    }
                    break;
                case EXPENDITURES:
//                    _id = mListViewCursor.getLong(mListViewCursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_ID));
                    mMainActivity.showTransferDetail((int) id, EXPENDITURES);
                    break;
                case INCOME:
//                    _id = mListViewCursor.getInt(mListViewCursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_ID));
                    mMainActivity.showTransferDetail((int) id, INCOME);
//                    Log.i(this.toString(), "ID: " + id);
                    break;
            }
        }
    }
}
