package se.mah.aliona.watchmywallet.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import se.mah.aliona.watchmywallet.beans.Expenditure;
import se.mah.aliona.watchmywallet.beans.Income;

/**
 * Created by aliona on 2017-09-11.
 */

public class DatabaseController {
    private SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    private IncCategoryRepository mIncomeCats;
    private ExpCategoryRepository mExpenditureCats;
    private IncomeRepository mIncomeRepo;
    private ExpenditureRepository mExpRepo;
    private TransfersRepository mTransfersRepo;
    private int mOpenCounter;

    public DatabaseController(Context context) {
        mDbHelper = new MyWalletDBHelper(context);
        mIncomeCats = new IncCategoryRepository(this);
        mExpenditureCats = new ExpCategoryRepository(this);
        mIncomeRepo = new IncomeRepository(this);
        mExpRepo = new ExpenditureRepository(this);
        mTransfersRepo = new TransfersRepository(this);
        openDatabase(); // keep the db open while the app is being used and db controller is alive?
    }

    public SQLiteOpenHelper getDatabaseHelper() {
        return mDbHelper;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter++;
        if(mOpenCounter == 1) {
            // Opening new database
            mDatabase = mDbHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if(mOpenCounter == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    public Cursor getIncomeCategories() {
        return mIncomeCats.getAllCategories();
    }

    public Cursor getExpenditureCategories() {
        return mExpenditureCats.getAllCategories();
    }

    public void saveNewExpenditure(Expenditure exp) {
        mExpRepo.saveExpenditure(exp);
    }

    public void saveNewIncome(Income income) {
        mIncomeRepo.saveIncome(income);
    }

    public Cursor getIncomeList() {
        return mIncomeRepo.getIncomeList();
    }

    public Cursor getExpenditureList() {
        return mExpRepo.getExpenditureList();
    }

    public Cursor getExpenditureList(int cat) {
        return mExpRepo.getExpenditureList(cat);
    }

    public Cursor getExpenditureList(long start, long end) {
        return mExpRepo.getExpenditureList(start, end);
    }

    public Cursor getExpenditureList(int cat, long start, long end) {
        return mExpRepo.getExpenditureList(cat, start, end);
    }

    public Cursor getIncomeList(int cat) {

        return mIncomeRepo.getIncomeList(cat);
    }

    public Cursor getIncomeList(long start, long end) {
        return mIncomeRepo.getIncomeList(start, end);
    }

    public Cursor getIncomeList(int cat, long start, long end) {
        return mIncomeRepo.getIncomeList(cat, start, end);
    }

    public Cursor getAllTransfers() {
        return mTransfersRepo.getAllTransfersList();
    }

    public Cursor getAllTransfers(long start, long end) {
        return mTransfersRepo.getAllTransfersList(start, end);
    }

    public Cursor getDataForExpPieChart(long start, long end) {
        return mTransfersRepo.getDataForExpPieChart(start, end);
    }

    public Cursor getDataForExpPieChart() {
        return mTransfersRepo.getDataForExpPieChart();
    }

    public Cursor getDataForIncPieChart(long start, long end) {
        return mTransfersRepo.getDataForIncPieChart(start, end);
    }

    public Cursor getDataForIncPieChart() {
        return mTransfersRepo.getDataForIncPieChart();
    }

    public Cursor getDataForHorizontalChart() {
        return mTransfersRepo.getDataForHorizontalChart();
    }

    public Cursor getDataForHorizontalChart(long start, long end) {
        return mTransfersRepo.getDataForHorizontalChart(start, end);
    }

    public Expenditure getExpenditure(int id) {
        return mExpRepo.getExpenditure(id);
    }

    public Income getIncome(int id) {
        return mIncomeRepo.getIncome(id);
    }
}
