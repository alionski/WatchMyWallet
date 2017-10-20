package se.mah.aliona.watchmywallet.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import se.mah.aliona.watchmywallet.beans.Income;

/**
 * Created by aliona on 2017-09-11.
 */

public class IncomeRepository {
    public static final int CURSOR_ID = 2;
    private DatabaseController ctrl;
    private SQLiteOpenHelper dbHelper;
    private String[] allColumns =
            {Contract.Inc._ID,
                    Contract.Inc.COLUMN_NAME_TITLE,
                    Contract.Inc.COLUMN_NAME_AMOUNT,
                    Contract.Inc.COLUMN_NAME_DATE,
                    Contract.Inc.COLUMN_NAME_CATEGORY};

    public IncomeRepository(DatabaseController ctrl) {
        this.ctrl = ctrl;
    }

    // db operations methods follow here

    public void saveIncome(Income income) {
        SQLiteDatabase db = ctrl.openDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.Inc.COLUMN_NAME_TITLE, income.getIncTitle());
        values.put(Contract.Inc.COLUMN_NAME_AMOUNT, income.getIncAmount());
        values.put(Contract.Inc.COLUMN_NAME_DATE, income.getIncDate());
        values.put(Contract.Inc.COLUMN_NAME_CATEGORY, income.getIncCatId());
        // Insert the new row, returning the primary key value of the new row
        db.insert(Contract.Inc.TABLE_NAME, null, values);
    }

    public Cursor getIncomeList() {
        SQLiteDatabase db = ctrl.openDatabase();
        String query = "SELECT income." + Contract.Inc._ID +  ", " +
                        "income." + Contract.Inc.COLUMN_NAME_TITLE + ", " +
                        "income." + Contract.Inc.COLUMN_NAME_AMOUNT + ", " +
                        "income." + Contract.Inc.COLUMN_NAME_DATE + ", " +
                        "cats." + Contract.IncCats.COLUMN_INC_CAT_NAME + " " +
                "FROM " + Contract.Inc.TABLE_NAME +
                " income INNER JOIN " +
                        Contract.IncCats.TABLE_NAME + " cats ON income." +
                        Contract.Inc.COLUMN_NAME_CATEGORY + " = cats." +
                        Contract.IncCats._ID +
                        " ORDER BY " + Contract.Inc.COLUMN_NAME_DATE+ " DESC";

        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getIncomeList(int cat) {
        SQLiteDatabase db = ctrl.openDatabase();
        String[] params = new String[]{ String.valueOf(cat) };
        String query = "SELECT income." + Contract.Inc._ID +  ", " +
                    "income." + Contract.Inc.COLUMN_NAME_TITLE + ", " +
                    "income." + Contract.Inc.COLUMN_NAME_AMOUNT + ", " +
                    "income." + Contract.Inc.COLUMN_NAME_DATE + ", " +
                    "cats." + Contract.IncCats.COLUMN_INC_CAT_NAME + " " +
                "FROM " + Contract.Inc.TABLE_NAME +
                " income INNER JOIN " +
                    Contract.IncCats.TABLE_NAME + " cats ON income." +
                    Contract.Inc.COLUMN_NAME_CATEGORY + " = cats." +
                    Contract.IncCats._ID +
                " WHERE cats." + Contract.IncCats._ID + "=?" +
                " ORDER BY " + Contract.Inc.COLUMN_NAME_DATE+ " DESC";

        Cursor cursor = db.rawQuery(query, params);
        return cursor;
    }

    public Cursor getIncomeList(long start, long end) {
        SQLiteDatabase db = ctrl.openDatabase();
        String[] params = new String[]{ String.valueOf(start), String.valueOf(end) };
        String query = "SELECT income." + Contract.Inc._ID +  ", " +
                    "income." + Contract.Inc.COLUMN_NAME_TITLE + ", " +
                    "income." + Contract.Inc.COLUMN_NAME_AMOUNT + ", " +
                    "income." + Contract.Inc.COLUMN_NAME_DATE + ", " +
                    "cats." + Contract.IncCats.COLUMN_INC_CAT_NAME + " " +
                "FROM " + Contract.Inc.TABLE_NAME +
                " income INNER JOIN " +
                    Contract.IncCats.TABLE_NAME + " cats ON income." +
                    Contract.Inc.COLUMN_NAME_CATEGORY + " = cats." +
                    Contract.IncCats._ID +
                " WHERE " +
                    Contract.Inc.COLUMN_NAME_DATE + ">=? AND " +
                    Contract.Inc.COLUMN_NAME_DATE + "<=?" +
                " ORDER BY " + Contract.Inc.COLUMN_NAME_DATE+ " DESC";

        Cursor cursor = db.rawQuery(query, params);
        return cursor;
    }

    public Cursor getIncomeList(int cat, long start, long end) {
        SQLiteDatabase db = ctrl.openDatabase();
        String[] params = new String[]{ String.valueOf(cat), String.valueOf(start), String.valueOf(end) };
        String query = "SELECT income." + Contract.Inc._ID +  ", " +
                        "income." + Contract.Inc.COLUMN_NAME_TITLE + ", " +
                        "income." + Contract.Inc.COLUMN_NAME_AMOUNT + ", " +
                        "income." + Contract.Inc.COLUMN_NAME_DATE + ", " +
                        "cats." + Contract.IncCats.COLUMN_INC_CAT_NAME + " " +
                    "FROM " + Contract.Inc.TABLE_NAME +
                    " income INNER JOIN " +
                        Contract.IncCats.TABLE_NAME + " cats ON income." +
                        Contract.Inc.COLUMN_NAME_CATEGORY + " = cats." +
                        Contract.IncCats._ID +
                " WHERE cats." + Contract.IncCats._ID + "=? AND " +
                        Contract.Inc.COLUMN_NAME_DATE + ">=? AND " +
                        Contract.Inc.COLUMN_NAME_DATE + "<=?" +
                " ORDER BY " + Contract.Inc.COLUMN_NAME_DATE+ " DESC";

        Cursor cursor = db.rawQuery(query, params);
        return cursor;
    }

    public Income getIncome(int id) {
        SQLiteDatabase db = ctrl.openDatabase();
        Income inc = new Income();
        String[] params = new String[]{String.valueOf(id) };
        String query = "SELECT inc." + Contract.Inc._ID +  ", " +
                "inc." + Contract.Inc.COLUMN_NAME_TITLE + ", " +
                "inc." + Contract.Inc.COLUMN_NAME_AMOUNT + ", " +
                "inc." + Contract.Inc.COLUMN_NAME_DATE + ", " +
                "cats." + Contract.IncCats.COLUMN_INC_CAT_NAME + " " +
                "FROM " + Contract.Inc.TABLE_NAME +
                " inc INNER JOIN " +
                Contract.IncCats.TABLE_NAME + " cats ON inc." +
                Contract.Inc.COLUMN_NAME_CATEGORY + " = cats." +
                Contract.IncCats._ID +
                " WHERE inc." +
                Contract.Inc._ID + " = ?";

        Cursor cursor = db.rawQuery(query, params);

        cursor.moveToFirst();
        inc.setIncID(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Inc._ID)));
        inc.setIncTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Inc.COLUMN_NAME_TITLE)));
        inc.setIncAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(Contract.Inc.COLUMN_NAME_AMOUNT)));
        inc.setIncDate(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Inc.COLUMN_NAME_DATE)));
        inc.setIncCat(cursor.getString(cursor.getColumnIndexOrThrow(Contract.IncCats.COLUMN_INC_CAT_NAME)));
//        DatabaseUtils.dumpCursor(cursor);
        cursor.close();
        return inc;
    }

    public void deleteIncome(int id) {
        SQLiteDatabase db = ctrl.openDatabase();
        String params = String.valueOf(id);
        db.delete(Contract.Inc.TABLE_NAME, Contract.Inc._ID + "=" + params, null);
    }
}
