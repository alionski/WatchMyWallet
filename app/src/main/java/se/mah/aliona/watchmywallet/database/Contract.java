package se.mah.aliona.watchmywallet.database;

import android.provider.BaseColumns;

/**
 * Created by aliona on 2017-09-07.
 */

public class Contract {

    private Contract() {}

    public static class Exp implements BaseColumns {
        public static final String TABLE_NAME = "expenditures";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_TITLE = "title";
        // TODO: change "cost" to "amount"
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
}
