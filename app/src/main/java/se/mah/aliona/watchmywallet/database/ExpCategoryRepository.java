package se.mah.aliona.watchmywallet.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Repository for the table "expenditure_category".
 * Created by aliona on 2017-09-11.
 */

class ExpCategoryRepository {
    private DatabaseController ctrl;
    private String[] allColumns =
                    {Contract.ExpCats._ID,
                    Contract.ExpCats.COLUMN_EXP_CAT_NAME};

    ExpCategoryRepository(DatabaseController ctrl) {
        this.ctrl = ctrl;
    }

    Cursor getAllCategories() {
        SQLiteDatabase db = ctrl.getDatabase();
        String sortOrder =
                Contract.ExpCats.COLUMN_EXP_CAT_NAME + " ASC";

        return db.query(Contract.ExpCats.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                sortOrder);
    }
}
