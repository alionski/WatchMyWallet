package se.mah.aliona.watchmywallet.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import se.mah.aliona.watchmywallet.beans.Income;

/**
 * Repository for the table "income".
 * Created by aliona on 2017-09-11.
 */

class IncomeRepository {
    private DatabaseController ctrl;

    IncomeRepository(DatabaseController ctrl) {
        this.ctrl = ctrl;
    }


    void saveIncome(Income income) {
        SQLiteDatabase db = ctrl.getDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.Inc.COLUMN_NAME_TITLE, income.getIncTitle());
        values.put(Contract.Inc.COLUMN_NAME_AMOUNT, income.getIncAmount());
        values.put(Contract.Inc.COLUMN_NAME_DATE, income.getIncDate());
        values.put(Contract.Inc.COLUMN_NAME_CATEGORY, income.getIncCatId());
        // Insert the new row, returning the primary key value of the new row
        db.insert(Contract.Inc.TABLE_NAME, null, values);
    }

    Cursor getIncomeList() {
        SQLiteDatabase db = ctrl.getDatabase();
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

        return db.rawQuery(query, null);
    }

    Cursor getIncomeList(int cat) {
        SQLiteDatabase db = ctrl.getDatabase();
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

        return db.rawQuery(query, params);
    }

    Cursor getIncomeList(long start, long end) {
        SQLiteDatabase db = ctrl.getDatabase();
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

        return db.rawQuery(query, params);
    }

    Cursor getIncomeList(int cat, long start, long end) {
        SQLiteDatabase db = ctrl.getDatabase();
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

        return db.rawQuery(query, params);
    }

   Income getIncome(int id) {
        SQLiteDatabase db = ctrl.getDatabase();
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
        cursor.close();
        return inc;
    }

    void deleteIncome(int id) {
        SQLiteDatabase db = ctrl.getDatabase();
        String params = String.valueOf(id);
        db.delete(Contract.Inc.TABLE_NAME, Contract.Inc._ID + "=" + params, null);
    }
}
