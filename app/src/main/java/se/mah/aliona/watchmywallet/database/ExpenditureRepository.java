package se.mah.aliona.watchmywallet.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import se.mah.aliona.watchmywallet.beans.Expenditure;

/**
 * Created by aliona on 2017-09-07.
 */

public class ExpenditureRepository {
    private Bundle bundle;
    public static final int CURSOR_ID = 1;
    public static final String CURSOR_FROM = "CURSOR_FROM";
    private DatabaseController ctrl;
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private String[] allColumns =
            {Contract.Exp._ID,
                    Contract.Exp.COLUMN_NAME_TITLE,
                    Contract.Exp.COLUMN_NAME_COST,
                    Contract.Exp.COLUMN_NAME_DATE,
                    Contract.Exp.COLUMN_NAME_CATEGORY};

    public ExpenditureRepository(DatabaseController ctrl) {
        this.ctrl = ctrl;
        dbHelper = ctrl.getDatabaseHelper();
        bundle = new Bundle();
        bundle.putInt(CURSOR_FROM, CURSOR_ID);
    }


    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // db operations methods follow here

    public void saveExpenditure(Expenditure exp) {
        // Gets the data repository in write mode
        SQLiteDatabase db = ctrl.openDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.Exp.COLUMN_NAME_TITLE, exp.getExpTitle());
        values.put(Contract.Exp.COLUMN_NAME_COST, exp.getExpCost());
        values.put(Contract.Exp.COLUMN_NAME_DATE, exp.getExpDate());
        values.put(Contract.Exp.COLUMN_NAME_CATEGORY, exp.getExpCatId());
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(Contract.Exp.TABLE_NAME, null, values);
    }

    public Cursor getExpenditureList() {
        SQLiteDatabase db = ctrl.openDatabase();
        String query = "SELECT exp." + Contract.Exp._ID +  ", " +
                    "exp." + Contract.Exp.COLUMN_NAME_TITLE + ", " +
                    "exp." + Contract.Exp.COLUMN_NAME_COST + ", " +
                    "exp." + Contract.Exp.COLUMN_NAME_DATE + ", " +
                    "cats." + Contract.ExpCats.COLUMN_EXP_CAT_NAME + " " +
                " FROM " + Contract.Exp.TABLE_NAME +
                " exp INNER JOIN " +
                    Contract.ExpCats.TABLE_NAME + " cats ON exp." +
                    Contract.Exp.COLUMN_NAME_CATEGORY + " = cats." +
                    Contract.ExpCats._ID +
                " ORDER BY " + Contract.Exp.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, null);
        DatabaseUtils.dumpCursor(cursor);
        return cursor;
    }

    public Cursor getExpenditureList(int cat) {
        String[] params = new String[]{ String.valueOf(cat) };
        SQLiteDatabase db = ctrl.openDatabase();
        String query = "SELECT exp." + Contract.Exp._ID +  ", " +
                    "exp." + Contract.Exp.COLUMN_NAME_TITLE + ", " +
                    "exp." + Contract.Exp.COLUMN_NAME_COST + ", " +
                    "exp." + Contract.Exp.COLUMN_NAME_DATE + ", " +
                    "cats." + Contract.ExpCats.COLUMN_EXP_CAT_NAME + " " +
                " FROM " + Contract.Exp.TABLE_NAME +
                " exp INNER JOIN " +
                    Contract.ExpCats.TABLE_NAME + " cats ON exp." +
                    Contract.Exp.COLUMN_NAME_CATEGORY + " = cats." +
                    Contract.ExpCats._ID +
                " WHERE cats." + Contract.ExpCats._ID + "=?" +
                " ORDER BY " + Contract.Exp.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, params);
        DatabaseUtils.dumpCursor(cursor);
        return cursor;
    }

    public Cursor getExpenditureList(int cat, long start, long end) {
        String[] params = new String[]{ String.valueOf(cat), String.valueOf(start), String.valueOf(end) };
        SQLiteDatabase db = ctrl.openDatabase();
        String query = "SELECT exp." + Contract.Exp._ID +  ", " +
                        "exp." + Contract.Exp.COLUMN_NAME_TITLE + ", " +
                        "exp." + Contract.Exp.COLUMN_NAME_COST + ", " +
                        "exp." + Contract.Exp.COLUMN_NAME_DATE + ", " +
                        "cats." + Contract.ExpCats.COLUMN_EXP_CAT_NAME + " " +
                " FROM " + Contract.Exp.TABLE_NAME +
                    " exp INNER JOIN " +
                    Contract.ExpCats.TABLE_NAME + " cats ON exp." +
                    Contract.Exp.COLUMN_NAME_CATEGORY + " = cats." +
                    Contract.ExpCats._ID +
                " WHERE cats." + Contract.ExpCats._ID + "=? AND " +
                    Contract.Exp.COLUMN_NAME_DATE + ">=? AND " +
                    Contract.Exp.COLUMN_NAME_DATE + "<=?" +
                " ORDER BY " + Contract.Exp.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, params);
        DatabaseUtils.dumpCursor(cursor);
        return cursor;
    }

    public Cursor getExpenditureList(long start, long end) {
        String[] params = new String[]{ String.valueOf(start), String.valueOf(end) };
        SQLiteDatabase db = ctrl.openDatabase();
        String query = "SELECT exp." + Contract.Exp._ID +  ", " +
                    "exp." + Contract.Exp.COLUMN_NAME_TITLE + ", " +
                    "exp." + Contract.Exp.COLUMN_NAME_COST + ", " +
                    "exp." + Contract.Exp.COLUMN_NAME_DATE + ", " +
                    "cats." + Contract.ExpCats.COLUMN_EXP_CAT_NAME + " " +
                " FROM " + Contract.Exp.TABLE_NAME +
                " exp INNER JOIN " +
                    Contract.ExpCats.TABLE_NAME + " cats ON exp." +
                    Contract.Exp.COLUMN_NAME_CATEGORY + " = cats." +
                    Contract.ExpCats._ID +
                " WHERE " +
                    Contract.Exp.COLUMN_NAME_DATE + ">=? AND " +
                    Contract.Exp.COLUMN_NAME_DATE + "<=?" +
                " ORDER BY " + Contract.Exp.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, params);
        cursor.respond(bundle);
        return cursor;
    }

    public Expenditure getExpenditure(int id) {
        Expenditure exp = new Expenditure();
        String[] params = new String[]{String.valueOf(id) };
        String query = "SELECT exp." + Contract.Exp._ID +  ", " +
                            "exp." + Contract.Exp.COLUMN_NAME_TITLE + ", " +
                            "exp." + Contract.Exp.COLUMN_NAME_COST + ", " +
                            "exp." + Contract.Exp.COLUMN_NAME_DATE + ", " +
                            "cats." + Contract.ExpCats.COLUMN_EXP_CAT_NAME + " " +
                        "FROM " + Contract.Exp.TABLE_NAME +
                            " exp INNER JOIN " +
                        Contract.ExpCats.TABLE_NAME + " cats ON exp." +
                            Contract.Exp.COLUMN_NAME_CATEGORY + " = cats." +
                            Contract.ExpCats._ID +
                        " WHERE exp." +
                        Contract.Exp._ID + " = ?";

        SQLiteDatabase db = ctrl.openDatabase();
        Cursor cursor = db.rawQuery(query, params);

        cursor.moveToFirst();
        exp.setExpID(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Exp._ID)));
        exp.setExpTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Exp.COLUMN_NAME_TITLE)));
        exp.setExpCost(cursor.getDouble(cursor.getColumnIndexOrThrow(Contract.Exp.COLUMN_NAME_COST)));
        exp.setExpDate(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Exp.COLUMN_NAME_DATE)));
        exp.setExpCat(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ExpCats.COLUMN_EXP_CAT_NAME)));

        DatabaseUtils.dumpCursor(cursor);
        cursor.close();
        return exp;
    }
}


