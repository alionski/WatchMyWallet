package se.mah.aliona.watchmywallet.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by aliona on 2017-10-05.
 */

public class TransfersRepository {
    public static final String UNION_COLUMN_ID = "_id";
    public static final String UNION_COLUMN_TITLE = "title";
    public static final String UNION_COLUMN_AMOUNT = "amount";
    public static final String UNION_COLUMN_DATE = "date";
    public static final String UNION_COLUMN_CATEGORY = "category";
    public static final String UNION_COLUMN_SOURCE = "source";
    public static final String INCOME_SOURCE = "'income'";
    public static final String EXP_SOURCE = "'expenditure'";
    private DatabaseController ctrl;

    public TransfersRepository(DatabaseController ctrl) {
        this.ctrl = ctrl;
    }

    public Cursor getAllTransfersList() {
        SQLiteDatabase db = ctrl.openDatabase();
        String allTransfersQuery =
                "SELECT " +
                        "exp." + Contract.Exp._ID + " as " + UNION_COLUMN_ID + ", " +
                        "exp." + Contract.Exp.COLUMN_NAME_TITLE + " as " + UNION_COLUMN_TITLE + ", " +
                        "exp." + Contract.Exp.COLUMN_NAME_COST + " as " + UNION_COLUMN_AMOUNT + ", " +
                        "exp." + Contract.Exp.COLUMN_NAME_DATE + " as " + UNION_COLUMN_DATE + ", " +
                        "exp_cats." + Contract.ExpCats.COLUMN_EXP_CAT_NAME + " as " + UNION_COLUMN_CATEGORY + ", " +
                        EXP_SOURCE + " as " + UNION_COLUMN_SOURCE +
                        " from " + Contract.Exp.TABLE_NAME + " exp " +
                    "INNER JOIN " +
                        Contract.ExpCats.TABLE_NAME + " exp_cats ON exp." +
                        Contract.Exp.COLUMN_NAME_CATEGORY + " = exp_cats." +
                        Contract.ExpCats._ID + " " +
                "UNION ALL " +
                "SELECT " +
                        "income." + Contract.Inc._ID + " as " + UNION_COLUMN_ID + ", " +
                        "income." + Contract.Inc.COLUMN_NAME_TITLE + " as " + UNION_COLUMN_TITLE + ", " +
                        "income." + Contract.Inc.COLUMN_NAME_AMOUNT+ " as " + UNION_COLUMN_AMOUNT + ", " +
                        "income." + Contract.Inc.COLUMN_NAME_DATE + " as " + UNION_COLUMN_DATE + ", " +
                        "inc_cats." + Contract.IncCats.COLUMN_INC_CAT_NAME + " as " + UNION_COLUMN_CATEGORY + ", " +
                        INCOME_SOURCE + " as " + UNION_COLUMN_SOURCE +
                        " from " + Contract.Inc.TABLE_NAME + " income " +
                    "INNER JOIN " +
                        Contract.IncCats.TABLE_NAME + " inc_cats ON income." +
                        Contract.Inc.COLUMN_NAME_CATEGORY + " = inc_cats." +
                        Contract.IncCats._ID +
                " ORDER BY " + UNION_COLUMN_DATE +  " DESC";
        Cursor cursor = db.rawQuery(allTransfersQuery, null);
//        Log.i(this.toString(), String.valueOf(CURSOR_ID));
        return cursor;
    }

    public Cursor getAllTransfersList(long start, long end) {
        SQLiteDatabase db = ctrl.openDatabase();
//        Log.i(this.toString(), "getAllTransfersList(long start, long end) reached");
        String[] params = new String[]{ String.valueOf(start), String.valueOf(end), String.valueOf(start), String.valueOf(end)  };
        String allTransfersQuery =
                        "SELECT " +
                        "exp." + Contract.Exp._ID + " as " + UNION_COLUMN_ID + ", " +
                        "exp." + Contract.Exp.COLUMN_NAME_TITLE + " as " + UNION_COLUMN_TITLE + ", " +
                        "exp." + Contract.Exp.COLUMN_NAME_COST + " as " + UNION_COLUMN_AMOUNT + ", " +
                        "exp." + Contract.Exp.COLUMN_NAME_DATE + " as " + UNION_COLUMN_DATE + ", " +
                        "exp_cats." + Contract.ExpCats.COLUMN_EXP_CAT_NAME + " as " + UNION_COLUMN_CATEGORY + ", " +
                        EXP_SOURCE + " as " + UNION_COLUMN_SOURCE +
                        " from " + Contract.Exp.TABLE_NAME + " exp " +
                    "INNER JOIN " +
                        Contract.ExpCats.TABLE_NAME + " exp_cats ON exp." +
                        Contract.Exp.COLUMN_NAME_CATEGORY + " = exp_cats." +
                        Contract.ExpCats._ID + " " +
                    "WHERE " +
                        "exp." + Contract.Exp.COLUMN_NAME_DATE + ">=? AND " +
                        "exp." + Contract.Exp.COLUMN_NAME_DATE + "<=? " +

                "UNION ALL " +
                        "SELECT " +
                        "income." + Contract.Inc._ID + " as " + UNION_COLUMN_ID + ", " +
                        "income." + Contract.Inc.COLUMN_NAME_TITLE + " as " + UNION_COLUMN_TITLE + ", " +
                        "income." + Contract.Inc.COLUMN_NAME_AMOUNT+ " as " + UNION_COLUMN_AMOUNT + ", " +
                        "income." + Contract.Inc.COLUMN_NAME_DATE + " as " + UNION_COLUMN_DATE + ", " +
                        "inc_cats." + Contract.IncCats.COLUMN_INC_CAT_NAME + " as " + UNION_COLUMN_CATEGORY + ", " +
                        INCOME_SOURCE + " as " + UNION_COLUMN_SOURCE +
                        " from " + Contract.Inc.TABLE_NAME + " income " +
                    "INNER JOIN " +
                        Contract.IncCats.TABLE_NAME + " inc_cats ON income." +
                        Contract.Inc.COLUMN_NAME_CATEGORY + " = inc_cats." +
                        Contract.IncCats._ID + " " +
                    "WHERE " +
                        "income." + Contract.Inc.COLUMN_NAME_DATE + ">=? AND " +
                        "income." + Contract.Inc.COLUMN_NAME_DATE + "<=?" +
                    "ORDER BY " + UNION_COLUMN_DATE +  " DESC";
        Cursor c = db.rawQuery(allTransfersQuery, params);
        return c;
    }

    public Cursor getDataForHorizontalChart() {
        SQLiteDatabase db = ctrl.openDatabase();
        String query =
                "SELECT " +
                        "exp." + Contract.Exp._ID + " as " + UNION_COLUMN_ID + ", " +
                        EXP_SOURCE + " as " + UNION_COLUMN_SOURCE + ", " +
                        "SUM(exp." + Contract.Exp.COLUMN_NAME_COST + ") as " + UNION_COLUMN_AMOUNT + " " +
                    "FROM " + Contract.Exp.TABLE_NAME + " exp " +
                    "GROUP BY " + UNION_COLUMN_SOURCE + " " +
                "UNION ALL " +
                "SELECT " +
                        "income." + Contract.Inc._ID + " as " + UNION_COLUMN_ID + ", " +
                        INCOME_SOURCE + " as " + UNION_COLUMN_SOURCE + ", " +
                        "SUM(income." + Contract.Inc.COLUMN_NAME_AMOUNT+ ") as " + UNION_COLUMN_AMOUNT + " " +
                    "FROM " + Contract.Inc.TABLE_NAME + " income " +
                    "GROUP BY " + UNION_COLUMN_SOURCE;
//        Log.i(this.toString(), query);
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor getDataForHorizontalChart(long start, long end) {
        SQLiteDatabase db = ctrl.openDatabase();
        String[] params = new String[]{ String.valueOf(start), String.valueOf(end), String.valueOf(start), String.valueOf(end) };
        String allTransfersQuery =
                "SELECT " +
                        "exp." + Contract.Exp._ID + " as " + UNION_COLUMN_ID + ", " +
                        EXP_SOURCE + " as " + UNION_COLUMN_SOURCE + ", " +
                        "SUM(exp." + Contract.Exp.COLUMN_NAME_COST + ") as " + UNION_COLUMN_AMOUNT + " " +
                    "FROM " + Contract.Exp.TABLE_NAME + " exp " +
                    "WHERE " +
                        "exp." + Contract.Exp.COLUMN_NAME_DATE + ">=? AND " +
                        "exp." + Contract.Exp.COLUMN_NAME_DATE + "<=? " +
                    "GROUP BY " + UNION_COLUMN_SOURCE + " " +
                "UNION ALL " +
                "SELECT " +
                        "income." + Contract.Inc._ID + " as " + UNION_COLUMN_ID + ", " +
                        INCOME_SOURCE + " as " + UNION_COLUMN_SOURCE + ", " +
                        "SUM(income." + Contract.Inc.COLUMN_NAME_AMOUNT+ ") as " + UNION_COLUMN_AMOUNT + " " +
                    "FROM " + Contract.Inc.TABLE_NAME + " income " +
                    "WHERE " +
                        "income." + Contract.Inc.COLUMN_NAME_DATE + ">=? AND " +
                        "income." + Contract.Inc.COLUMN_NAME_DATE + "<=?" +
                    "GROUP BY " + UNION_COLUMN_SOURCE;
//        Log.i(this.toString(), allTransfersQuery);
        Cursor c = db.rawQuery(allTransfersQuery, params);
        return c;
    }

    public Cursor getDataForExpPieChart(long start, long end) {
        SQLiteDatabase db = ctrl.openDatabase();
        String[] params = new String[]{ String.valueOf(start), String.valueOf(end) };
        String query =
                "SELECT " + Contract.ExpCats.COLUMN_EXP_CAT_NAME  + ", " +
                        Contract.Exp.COLUMN_NAME_COST + " " +
                "FROM " + Contract.Exp.TABLE_NAME + " exp " +
                "INNER JOIN " + Contract.ExpCats.TABLE_NAME + " cats " +
                "ON exp." + Contract.Exp.COLUMN_NAME_CATEGORY + " = cats." +
                    Contract.ExpCats._ID + " " +
                "WHERE " +
                    "exp." + Contract.Exp.COLUMN_NAME_DATE + ">=? AND " +
                    "exp." + Contract.Exp.COLUMN_NAME_DATE + "<=? " +
                "GROUP BY " + Contract.ExpCats.COLUMN_EXP_CAT_NAME;
        Cursor c = db.rawQuery(query, params);
        return c;
    }

    public Cursor getDataForExpPieChart() {
        SQLiteDatabase db = ctrl.openDatabase();
        String query =
                "SELECT " + Contract.ExpCats.COLUMN_EXP_CAT_NAME + ", " +
                        Contract.Exp.COLUMN_NAME_COST + " " +
                "FROM " + Contract.Exp.TABLE_NAME + " exp " +
                        "INNER JOIN " + Contract.ExpCats.TABLE_NAME + " cats " +
                        "ON exp." + Contract.Exp.COLUMN_NAME_CATEGORY + " = cats." +
                        Contract.ExpCats._ID + " " +
                        "GROUP BY " + Contract.ExpCats.COLUMN_EXP_CAT_NAME;
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor getDataForIncPieChart(long start, long end) {
        SQLiteDatabase db = ctrl.openDatabase();
        String[] params = new String[]{ String.valueOf(start), String.valueOf(end) };
        String query =
                "SELECT " + Contract.IncCats.COLUMN_INC_CAT_NAME + ", " +
                        Contract.Inc.COLUMN_NAME_AMOUNT + " " +
                        "FROM " + Contract.Inc.TABLE_NAME + " exp " +
                        "INNER JOIN " + Contract.IncCats.TABLE_NAME + " cats " +
                        "ON exp." + Contract.Inc.COLUMN_NAME_CATEGORY + " = cats." +
                        Contract.IncCats._ID + " " +
                        "WHERE " +
                        "exp." + Contract.Inc.COLUMN_NAME_DATE + ">=? AND " +
                        "exp." + Contract.Inc.COLUMN_NAME_DATE + "<=? " +
                        "GROUP BY " + Contract.IncCats.COLUMN_INC_CAT_NAME;
        Cursor c = db.rawQuery(query, params);
        return c;
    }

    public Cursor getDataForIncPieChart() {
        SQLiteDatabase db = ctrl.openDatabase();
        String query =
                "SELECT " + Contract.IncCats.COLUMN_INC_CAT_NAME + ", " +
                        Contract.Inc.COLUMN_NAME_AMOUNT + " " +
                        "FROM " + Contract.Inc.TABLE_NAME + " exp " +
                        "INNER JOIN " + Contract.IncCats.TABLE_NAME + " cats " +
                        "ON exp." + Contract.Inc.COLUMN_NAME_CATEGORY + " = cats." +
                        Contract.IncCats._ID + " " +
                        "GROUP BY " + Contract.IncCats.COLUMN_INC_CAT_NAME;
        Cursor c = db.rawQuery(query, null);
        return c;
    }
}
