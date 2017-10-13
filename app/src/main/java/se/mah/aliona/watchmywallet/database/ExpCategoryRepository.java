package se.mah.aliona.watchmywallet.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by aliona on 2017-09-11.
 */

public class ExpCategoryRepository {
    private DatabaseController ctrl;
    private String[] allColumns =
                    {Contract.ExpCats._ID,
                    Contract.ExpCats.COLUMN_EXP_CAT_NAME};

    public ExpCategoryRepository(DatabaseController ctrl) {
        this.ctrl = ctrl;
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = ctrl.openDatabase();
        String sortOrder =
                Contract.ExpCats.COLUMN_EXP_CAT_NAME + " ASC";
        Cursor cursor = db.query(Contract.ExpCats.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                sortOrder);
        return cursor;
    }
}
