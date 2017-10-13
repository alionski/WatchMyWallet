package se.mah.aliona.watchmywallet.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by aliona on 2017-09-11.
 */

public class IncCategoryRepository {
    private DatabaseController ctrl;
    private String[] allColumns =
                    {Contract.IncCats._ID,
                    Contract.IncCats.COLUMN_INC_CAT_NAME};

    public IncCategoryRepository (DatabaseController ctrl) {
        this.ctrl = ctrl;
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = ctrl.openDatabase();
        String sortOrder =
                Contract.IncCats.COLUMN_INC_CAT_NAME + " ASC";
        Cursor cursor = db.query(Contract.IncCats.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                sortOrder);
        return cursor;
    }
}
