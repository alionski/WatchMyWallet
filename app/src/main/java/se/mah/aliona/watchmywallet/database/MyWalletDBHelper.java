package se.mah.aliona.watchmywallet.database;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import se.mah.aliona.watchmywallet.R;

/**
 * Created by aliona on 2017-09-07.
 */

public class MyWalletDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "WatchMyWallet.db";
    private Context ctx;

    private static final String SQL_CREATE_EXPENDITURE_TABLE =
            "CREATE TABLE " + Contract.Exp.TABLE_NAME + " (" +
                    Contract.Exp._ID + " INTEGER PRIMARY KEY," +
                    Contract.Exp.COLUMN_NAME_TITLE + " TEXT," +
                    Contract.Exp.COLUMN_NAME_COST  + " REAL," +
                    Contract.Exp.COLUMN_NAME_DATE + " INTEGER," +
                    Contract.Exp.COLUMN_NAME_CATEGORY + " INTEGER, " +
                    "FOREIGN KEY(" + Contract.Exp.COLUMN_NAME_CATEGORY + ") REFERENCES " +
                    Contract.ExpCats.TABLE_NAME + "(" + Contract.ExpCats._ID + "))";
    private static final String SQL_CREATE_INCOME_TABLE =
            "CREATE TABLE " + Contract.Inc.TABLE_NAME + " (" +
                    Contract.Inc._ID + " INTEGER PRIMARY KEY," +
                    Contract.Inc.COLUMN_NAME_TITLE + " TEXT," +
                    Contract.Inc.COLUMN_NAME_AMOUNT  + " REAL," +
                    Contract.Inc.COLUMN_NAME_DATE + " INTEGER," +
                    Contract.Inc.COLUMN_NAME_CATEGORY + " INTEGER, " +
                    "FOREIGN KEY(" + Contract.Inc.COLUMN_NAME_CATEGORY  + ") REFERENCES " +
                    Contract.IncCats.TABLE_NAME + "(" + Contract.IncCats._ID + "))";
    private static final String SQL_CREATE_EXP_CAT_TABLE =
            "CREATE TABLE " + Contract.ExpCats.TABLE_NAME + " (" +
                    Contract.ExpCats._ID + " INTEGER PRIMARY KEY," +
                    Contract.ExpCats.COLUMN_EXP_CAT_NAME + " TEXT)";
    private static final String SQL_CREATE_INC_CAT_TABLE =
            "CREATE TABLE " + Contract.IncCats.TABLE_NAME + " (" +
                    Contract.IncCats._ID + " INTEGER PRIMARY KEY," +
                    Contract.IncCats.COLUMN_INC_CAT_NAME + " TEXT)";
    private static final String SQL_CREATE_BARCODES_TABLE =
            "CREATE TABLE " + Contract.Barcodes.TABLE_NAME + " (" +
                    Contract.Barcodes._ID + " INTEGER PRIMARY KEY, " +
                    Contract.Barcodes.COLUMN_NAME_BARCODE + " INTEGER, " +
                    Contract.Barcodes.COLUMN_NAME_PRODUCT_NAME + " TEXT, " +
                    Contract.Barcodes.COLUMN_NAME_INITIAL_PRICE + " REAL)";
    private static final String SQL_CREATE_EXP_BARCODES_TABLE =
            "CREATE TABLE " + Contract.ExpBarcode.TABLE_NAME + " (" +
                    Contract.ExpBarcode._ID + " INTEGER PRIMARY KEY, " +
                    Contract.ExpBarcode.COLUMN_NAME_BARCODE_ID + " INTEGER, " +
                    Contract.ExpBarcode.COLUMN_NAME_EXPENDITURE_ID + " INTEGER, " +
                    "FOREIGN KEY(" + Contract.ExpBarcode.COLUMN_NAME_BARCODE_ID + ") REFERENCES " +
                    Contract.Barcodes.TABLE_NAME + "(" + Contract.Barcodes._ID + "), " +
                    "FOREIGN KEY(" + Contract.ExpBarcode.COLUMN_NAME_EXPENDITURE_ID + ") REFERENCES " +
                    Contract.Exp.TABLE_NAME + "(" + Contract.Exp._ID + "))";



    private static final String SQL_DELETE_EXP_TABLE =
            "DROP TABLE IF EXISTS " + Contract.Exp.TABLE_NAME;
    private static final String SQL_DELETE_INC_TABLE =
            "DROP TABLE IF EXISTS " + Contract.Inc.TABLE_NAME;
    private static final String SQL_DELETE_EXP_CAT_TABLE =
            "DROP TABLE IF EXISTS " + Contract.ExpCats.TABLE_NAME;
    private static final String SQL_DELETE_INC_CAT_TABLE =
            "DROP TABLE IF EXISTS " + Contract.IncCats.TABLE_NAME;
    private static final String SQL_DELETE_BARCODES_TABLE =
            "DROP TABLE IF EXISTS " + Contract.Barcodes.TABLE_NAME;
    private static final String SQL_DELETE_EXP_BARCODES_TABLE =
            "DROP TABLE IF EXISTS " + Contract.ExpBarcode.TABLE_NAME;

    public MyWalletDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_EXPENDITURE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INCOME_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EXP_CAT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INC_CAT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_BARCODES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EXP_BARCODES_TABLE);
        populateCategories(sqLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.w(MyWalletDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(SQL_DELETE_EXP_TABLE);
        db.execSQL(SQL_DELETE_INC_TABLE);
        db.execSQL(SQL_DELETE_EXP_CAT_TABLE);
        db.execSQL(SQL_DELETE_INC_CAT_TABLE);
        db.execSQL(SQL_DELETE_BARCODES_TABLE);
        db.execSQL(SQL_DELETE_EXP_BARCODES_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void populateCategories(SQLiteDatabase db) {
        Resources res = ctx.getResources();
        String[] expenditureCats = res.getStringArray(R.array.expenditure_categories);
        String[] incomeCats = res.getStringArray(R.array.income_categories);

        db.beginTransaction();
        try {
            for (String expCat : expenditureCats) {
                db.execSQL("INSERT INTO " +
                                Contract.ExpCats.TABLE_NAME + "(" +
                                Contract.ExpCats.COLUMN_EXP_CAT_NAME + ") VALUES ('" +
                        expCat + "')");
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.beginTransaction();
        try {
            for (String incCat : incomeCats) {
                db.execSQL("INSERT INTO " +
                        Contract.IncCats.TABLE_NAME + "(" +
                        Contract.IncCats.COLUMN_INC_CAT_NAME + ") VALUES ('" +
                        incCat + "')");
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
