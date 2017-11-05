package se.mah.aliona.watchmywallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import java.util.ArrayList;
import barcode.BarcodeCaptureActivity;
import layout.AddExpenditureFragment;
import layout.AddIncomeFragment;
import layout.AddNewTransferPopup;
import layout.BaseFragment;
import layout.SettingsFragment;
import layout.StatisticsFragment;
import layout.TransferDetailFragment;
import layout.TransfersFragment;
import se.mah.aliona.watchmywallet.beans.Expenditure;
import se.mah.aliona.watchmywallet.beans.Income;
import se.mah.aliona.watchmywallet.beans.WalletBarcode;
import se.mah.aliona.watchmywallet.database.DatabaseController;
import se.mah.aliona.watchmywallet.database.Contract;
import se.mah.aliona.watchmywallet.database.TransfersRepository;

public class MainActivity extends AppCompatActivity implements AddNewTransferPopup.Callback,
        NavigationView.OnNavigationItemSelectedListener {
    private String mUserName;
    private String mUserSurname;

    private static final int SCANNER_INTENT = 0;

    public static String SAVED_FRAGMENT;
    public static int CURRENT_FRAGMENT = 0; // always transfers on fresh start

    public static final int TRANSFERS = 0;
    public static final int STATISTICS = 1;
    public static final int SETTINGS = 2;
    public static final int NEW_EXP = 3;
    public static final int NEW_INC = 4;
    public static final int TRANS_DETAIL = 5;

    public static final String TRANSFERS_TAG = "transfers";
    public static final String STATISTICS_TAG = "stats";
    public static final String SETTINGS_TAG = "settings";
    public static final String NEW_EXP_TAG = "new_exp";
    public static final String NEW_INC_TAG = "new_inc";
    public static final String TRANS_DETAIL_TAG = "trans_detail";

    private DatabaseController mDBController;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer_layout);

        if (savedInstanceState != null) {
            CURRENT_FRAGMENT = savedInstanceState.getInt(SAVED_FRAGMENT);
        }

        mDBController = new DatabaseController(this.getApplicationContext());

        Toolbar appToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(appToolbar);

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, appToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initialisePreferences();
        setNewFragment(CURRENT_FRAGMENT, null, null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(this.toString(), "******************* CURRENT FRAGMENT ::: " + CURRENT_FRAGMENT);
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_FRAGMENT, CURRENT_FRAGMENT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_tollbar, menu);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    public DatabaseController getDBController() {
        return mDBController;
    }

    /** Swaps fragments in the main content view */
    public void setNewFragment(int position, Expenditure expDetail, Income incDetail){
        FragmentManager fm = getSupportFragmentManager();

        switch (position) {
            case TRANSFERS:
                TransfersFragment trans = (TransfersFragment) fm.findFragmentByTag(TRANSFERS_TAG);

                if (trans == null) {
                    trans = new TransfersFragment();
                }

                fm.beginTransaction()
                        .replace(R.id.fragment_main_holder, trans, TRANSFERS_TAG)
                        .commit();

                setTitle("Transfers");

                break;
            case STATISTICS:
                StatisticsFragment stats = (StatisticsFragment) fm.findFragmentByTag(STATISTICS_TAG);

                if (stats == null) {
                    stats = new StatisticsFragment();
                    stats.setUserName(mUserName, mUserSurname);
                }

                fm.beginTransaction()
                        .replace(R.id.fragment_main_holder, stats, STATISTICS_TAG)
                        .commit();

                setTitle("Statistics");

                break;
            case SETTINGS:
                SettingsFragment settings = (SettingsFragment) fm.findFragmentByTag(SETTINGS_TAG);

                if (settings == null) {
                    settings = new SettingsFragment();
                    settings.setUserName(mUserName, mUserSurname);
                }

                fm.beginTransaction()
                        .replace(R.id.fragment_main_holder, settings, SETTINGS_TAG)
                        .commit();

                setTitle("Settings");

                break;
            case NEW_EXP:
                AddExpenditureFragment exp = (AddExpenditureFragment) fm.findFragmentByTag(NEW_EXP_TAG);

                if (exp == null) {
                    exp = new AddExpenditureFragment();
                }

                exp.setUserName(mUserName, mUserSurname);

                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_main_holder, exp, NEW_EXP_TAG);
                if (CURRENT_FRAGMENT == TRANSFERS) {
                    transaction.addToBackStack("back_to_transfers");
                }

                transaction.commit();

                setTitle("Add New Expenditure");

                break;
            case NEW_INC:
                AddIncomeFragment inc = (AddIncomeFragment) fm.findFragmentByTag(NEW_INC_TAG);

                if (inc == null) {
                    inc = new AddIncomeFragment();
                }
                inc.setUserName(mUserName, mUserSurname);

                FragmentTransaction transaction2 = fm.beginTransaction();
                transaction2.replace(R.id.fragment_main_holder, inc, NEW_INC_TAG);
                if (CURRENT_FRAGMENT == TRANSFERS) {
                    transaction2.addToBackStack("back_to_transfers");
                }
                transaction2.commit();

                setTitle("Add New Income");
                break;
            case TRANS_DETAIL:
                TransferDetailFragment transDetail = new TransferDetailFragment();

                if (expDetail != null) {
                    transDetail = TransferDetailFragment.newInstance(expDetail);
                } else if (incDetail != null) {
                    transDetail = TransferDetailFragment.newInstance(incDetail);
                }

                FragmentTransaction transaction3 = fm.beginTransaction();
                transaction3.setCustomAnimations(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                transaction3.replace(R.id.fragment_main_holder, transDetail, TRANS_DETAIL_TAG);
                if (CURRENT_FRAGMENT == TRANSFERS) {
                    transaction3.addToBackStack("back_to_transfers");
                }
                transaction3.commit();
                setTitle("Transfer Detail");
                break;
        }
        CURRENT_FRAGMENT = position;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int index = fm.getBackStackEntryCount() - 1;
        if (index >= 0) {
            FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(index);
            if (backEntry != null) {
                String tag = backEntry.getName();
                if (tag.equals("back_to_transfers")) {
                    CURRENT_FRAGMENT = TRANSFERS;
                }
            }
        }
        super.onBackPressed();
    }

    public void showTransferDetail(int id, int type) {
        if (type == TransfersFragment.EXPENDITURES) {
            Expenditure exp = mDBController.getExpenditure(id);
            setNewFragment(TRANS_DETAIL, exp, null);
        } else if (type == TransfersFragment.INCOME) {
            Income inc = mDBController.getIncome(id);
            setNewFragment(TRANS_DETAIL, null, inc);
        }
    }

    public void initialisePreferences() {
        Resources res = getResources();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mUserName = sharedPref.getString(res.getString(R.string.username), null);
        mUserSurname = sharedPref.getString(res.getString(R.string.usersurname), null);
    }

    public void editPreferences(String name, String surname) {
        Resources res = getResources();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(res.getString(R.string.username), name);
        editor.putString(res.getString(R.string.usersurname), surname);
        editor.apply();
    }

    public String getUserName() {
        return mUserName;
    }

    public String getUserSurname() {
        return mUserSurname;
    }

    // CALLS TO DB

    public void saveExpenditureToDB(Expenditure exp) {
        mDBController.saveNewExpenditure(exp);
        setNewFragment(TRANSFERS, null, null);
    }

    public void saveExpenditureToDB(Expenditure exp, long newBarcodeNumber) {
        WalletBarcode barcode =
                new WalletBarcode();
        barcode.setBarcodeNumber(newBarcodeNumber);
        barcode.setProductName(exp.getExpTitle());
        barcode.setInitialPrice(exp.getExpCost());

        int expId = (int) mDBController.saveNewExpenditure(exp);
        int newBarcodeId = (int) mDBController.saveBarcode(barcode);

        mDBController.saveExpBarcode(newBarcodeId, expId);

        setNewFragment(TRANSFERS, null, null);
    }

    public void saveExpenditureToDB(Expenditure exp, int foundBarcodeId) {
        int expId = (int) mDBController.saveNewExpenditure(exp);
        mDBController.saveExpBarcode(foundBarcodeId, expId);
        setNewFragment(TRANSFERS, null, null);
    }

    public void saveIncomeToDB(Income income) {
        mDBController.saveNewIncome(income);
        setNewFragment(TRANSFERS, null, null);
    }

    public Cursor getExpenditureList(int cat, long start, long end) {
        if (cat == 0 || cat == -1) {
            // means we have to show all cats
            if (start == 0 || end == 0) {
                // means we need to show all cats and all dates
                return mDBController.getExpenditureList();
            } else {
                return mDBController.getExpenditureList(start, end);
            }
        } else {
            if (start == 0 || end == 0) {
                // means we have a specific category but need to show for all time
                return mDBController.getExpenditureList(cat);
            } else {
                // means we have both a category and time span
                return mDBController.getExpenditureList(cat, start, end);
            }
        }
    }

    public Cursor getIncomeList(int cat, long start, long end) {
        if (cat == 0 || cat == -1) {
            // means we have to show all cats
            if (start == 0 || end == 0) {
                // means we need to show all cats and all dates
                return mDBController.getIncomeList();
            } else {
                return mDBController.getIncomeList(start, end);
            }
        } else {
            if (start == 0 || end == 0) {
                // means we have a specific category but need to show for all time
                return mDBController.getIncomeList(cat);
            } else {
                // means we have both a category and time span
                return mDBController.getIncomeList(cat, start, end);
            }
        }
    }

    public Cursor getAllTransfersList() {
        return mDBController.getAllTransfers();
    }

    public Cursor getAllTransfersList(long start, long end) {
        return mDBController.getAllTransfers(start, end);
    }

    public void deleteTransfer(int type, int id) {
        if (type == BaseFragment.EXPENDITURES) {
            mDBController.deleteExpenditure(id);
        } else if (type == BaseFragment.INCOME) {
            mDBController.deleteIncome(id);
        }
        setNewFragment(TRANSFERS, null, null);
    }

    public ArrayList<PieEntry> getDataForExpPieChart(long start, long end) {
        ArrayList<PieEntry> list = new ArrayList<>();
        Cursor cursor;
        double sum = 0;
        if (start == 0 || end == 0) {
            cursor = mDBController.getDataForExpPieChart();
        } else {
            cursor = mDBController.getDataForExpPieChart(start, end);
        }

        while (cursor.moveToNext()) {
            sum += cursor.getDouble(cursor.getColumnIndexOrThrow(Contract.Exp.COLUMN_NAME_COST));
        }
        cursor.moveToPosition(-1);

        double onePercent = sum / 100; // double / percent

        while (cursor.moveToNext()) {
            double cost = cursor.getDouble(cursor.getColumnIndexOrThrow(Contract.Exp.COLUMN_NAME_COST));
            float percentage = (float) cost / (float) onePercent;
            String cat = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ExpCats.COLUMN_EXP_CAT_NAME));
            PieEntry entry = new PieEntry(percentage, cat);
            list.add(entry);
        }
        cursor.close();
        return list;
    }

    public ArrayList<PieEntry> getDataForIncPieChart(long start, long end) {
        ArrayList<PieEntry> list = new ArrayList<>();
        Cursor cursor;
        double sum = 0;
        if (start == 0 || end == 0) {
            cursor = mDBController.getDataForIncPieChart();
        } else {
            cursor = mDBController.getDataForIncPieChart(start, end);
        }

        while (cursor.moveToNext()) {
            sum += cursor.getDouble(cursor.getColumnIndexOrThrow(Contract.Inc.COLUMN_NAME_AMOUNT));
        }
        cursor.moveToPosition(-1);

        double onePercent = sum / 100; // double / percent

        while (cursor.moveToNext()) {
            double cost = cursor.getDouble(cursor.getColumnIndexOrThrow(Contract.Inc.COLUMN_NAME_AMOUNT));
            float percentage = (float) cost / (float) onePercent;
            String cat = cursor.getString(cursor.getColumnIndexOrThrow(Contract.IncCats.COLUMN_INC_CAT_NAME));
            PieEntry entry = new PieEntry(percentage, cat);
            list.add(entry);
        }
        cursor.close();
        return list;
    }

    public ArrayList<BarEntry> getDataForHorizontalChart(long start, long end) {
        ArrayList<BarEntry> list = new ArrayList<>();
        Cursor cursor;
        if (start == 0 || end == 0) {
            cursor = mDBController.getDataForHorizontalChart();
        } else {
            cursor = mDBController.getDataForHorizontalChart(start, end);
        }

        while (cursor.moveToNext()) {
            BarEntry entry;
            String type = "'" + cursor.getString(cursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_SOURCE))+"'";
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(TransfersRepository.UNION_COLUMN_AMOUNT));
            if (type.equals(TransfersRepository.EXP_SOURCE)) {
                entry = new BarEntry(0f, (float) amount);
                list.add(entry);
            } else if (type.equals(TransfersRepository.INCOME_SOURCE)){
                entry = new BarEntry(1f, (float) amount);
                list.add(entry);
            }
        }

        cursor.close();
        return list;
    }

    @NonNull
    public static String prettify(long date) {
        // 20171011 -- 11-10-2017
        String dateStr = String.valueOf(date);

        StringBuilder builder = new StringBuilder(dateStr.substring(6, dateStr.length())
                .concat("-")
                .concat(dateStr.substring(4, 6).concat("-")
                        .concat(dateStr.substring(0, 4))));
        return builder.toString();
    }

    /**
     * Shows a popup window
     */
    public void showAddNewTransferPopup() {
        AddNewTransferPopup popup = AddNewTransferPopup.newInstance(this);
        popup.show(getSupportFragmentManager(), "popup");
    }

    /**
     * Callback method for the tiny popup fragments
     */
    @Override
    public void onResult(int result) {
        if (result == 0) {
            setNewFragment(NEW_EXP, null, null);
        } else {
            setNewFragment(NEW_INC, null, null);
        }
    }

    public void scanBarcode() {
        Intent intent = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
        startActivityForResult(intent, SCANNER_INTENT);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNER_INTENT) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    AddExpenditureFragment exp =
                            (AddExpenditureFragment) getSupportFragmentManager().findFragmentByTag(NEW_EXP_TAG);

                    WalletBarcode dbBarcode =
                            mDBController.getBarcode(Long.parseLong(barcode.displayValue));
                    if (dbBarcode != null) {
                        exp.setFoundBarcode(dbBarcode);
                    } else {
                        exp.setBarcodeResult(barcode.displayValue);
                    }

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.fragment_main_holder),
                            R.string.barcode_success, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.fragment_main_holder),
                            String.format(getString(R.string.barcode_error),
                                    CommonStatusCodes.getStatusCodeString(resultCode)),
                            Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_transfers) {
            setNewFragment(TRANSFERS, null, null);
        } else if (id == R.id.nav_stats) {
            setNewFragment(STATISTICS, null, null);
        } else if (id == R.id.nav_settings) {
            setNewFragment(SETTINGS, null, null);
        }

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBController.closeDatabase();
    }
}
