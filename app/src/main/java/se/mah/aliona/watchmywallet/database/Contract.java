package se.mah.aliona.watchmywallet.database;

import android.provider.BaseColumns;

/**
 * Contract, describes what tables and columns are to be present in the database.
 * Created by aliona on 2017-09-07.
 */

public class Contract {

    private Contract() {}

    public static class Exp implements BaseColumns {
        public static final String TABLE_NAME = "expenditures";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_COST = "cost";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }

    public static class Inc implements BaseColumns {
        public static final String TABLE_NAME = "income";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }

    public static class ExpCats implements BaseColumns {
        public static final String TABLE_NAME = "expenditure_category";
        public static final String _ID = "_id";
        public static final String COLUMN_EXP_CAT_NAME = "name";
    }

    public static class IncCats implements BaseColumns {
        public static final String TABLE_NAME = "income_category";
        public static final String _ID = "_id";
        public static final String COLUMN_INC_CAT_NAME = "name";
    }

    public static class Barcodes implements BaseColumns {
        public static final String TABLE_NAME = "barcodes";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_BARCODE = "barcode_value"; // long
        public static final String COLUMN_NAME_PRODUCT_NAME = "product_name";
        public static final String COLUMN_NAME_INITIAL_PRICE = "initial_price";
    }

    public static class ExpBarcode implements BaseColumns {
        public static final String TABLE_NAME = "expenditure_barcodes";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_BARCODE_ID = "barcode_id"; // long
        public static final String COLUMN_NAME_EXPENDITURE_ID = "expenditure_id";
    }
}
