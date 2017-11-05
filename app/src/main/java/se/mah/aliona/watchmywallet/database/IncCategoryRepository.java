package se.mah.aliona.watchmywallet.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Repository for the table "income_category".
 * Created by aliona on 2017-09-11.
 */

class IncCategoryRepository {
    private DatabaseController ctrl;
    private String[] allColumns =
                    {Contract.IncCats._ID,
                    Contract.IncCats.COLUMN_INC_CAT_NAME};

    IncCategoryRepository (DatabaseController ctrl) {
        this.ctrl = ctrl;
    }

    Cursor getAllCategories() {
        SQLiteDatabase db = ctrl.getDatabase();
        String sortOrder =
                Contract.IncCats.COLUMN_INC_CAT_NAME + " ASC";
        return db.query(Contract.IncCats.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                sortOrder);
    }
}
