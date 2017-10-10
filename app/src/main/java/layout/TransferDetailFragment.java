package layout;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

import se.mah.aliona.watchmywallet.MainActivity;
import se.mah.aliona.watchmywallet.R;
import se.mah.aliona.watchmywallet.beans.Expenditure;
import se.mah.aliona.watchmywallet.beans.Income;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferDetailFragment extends BaseFragment {
    public static final int EXPENDITURES = 1;
    public static final int INCOME = 2;

    private static final String TYPE = "detail_type";
    private static final String TRANSFER_OBJECT = "transfer_object";
    private static final String ID = "_id";
    private static final String TITLE = "title";
    private static final String AMOUNT = "amount";
    private static final String DATE = "date";
    private static final String CATEGORY = "category";

    private Expenditure mExp;
    private Income mInc;

    private int mType;
    private int mId;
    private String mTitle;
    private double mAmount;
    private long mDate;
    private String mCategory;

    private TextView mTitleValue;
    private TextView mAmountValue;
    private TextView mDateValue;
    private TextView mCategoryValue;


    public TransferDetailFragment() {
        // Required empty public constructor
    }

    public static TransferDetailFragment newInstance(Expenditure exp) {
        TransferDetailFragment instance = new TransferDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, EXPENDITURES);
        bundle.putSerializable(TRANSFER_OBJECT, exp);
        instance.setArguments(bundle);

        return instance;
    }

    public static TransferDetailFragment newInstance(Income inc) {
        TransferDetailFragment instance = new TransferDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, INCOME);
        bundle.putSerializable(TRANSFER_OBJECT, inc);
        instance.setArguments(bundle);

        return instance;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(this.toString(), "ON ACTIVITY CREATED");
//        if (savedInstanceState != null) {
//            restoreState(savedInstanceState);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(this.toString(), "ON ATTACH");
        if (context instanceof MainActivity){
            mMainActivity = (MainActivity) context;
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        Log.i(this.toString(), "Saved instance is not null!!!!!!!!!!!!!!!");
        mType = savedInstanceState.getInt(TYPE, 0);
        Serializable transfer = savedInstanceState.getSerializable(TRANSFER_OBJECT);
        if (mType == EXPENDITURES) {
            mExp = (Expenditure) transfer;
        } else if (mType == INCOME) {
            mInc = (Income) transfer;
        }
    }

    private void getArgs() {
        mType = getArguments().getInt(TYPE, 0);
        Serializable transfer = getArguments().getSerializable(TRANSFER_OBJECT);
        if (mType == EXPENDITURES) {
            mExp = (Expenditure) transfer;
        } else if (mType == INCOME) {
            mInc = (Income) transfer;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(this.toString(), "ON CREATE VIEW");
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            getArgs();
        }

        cleanContainer(container);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_transfer_detail, container, false);

        initialiseUI(view, mExp, mInc);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.appbar_button_refresh_charts).setVisible(false);
        menu.findItem(R.id.appbar_button_refresh_transfers).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private void initialiseUI(View view, Expenditure exp, Income inc) {
        mTitleValue = view.findViewById(R.id.trans_detail_value_title);
        mAmountValue = view.findViewById(R.id.trans_detail_value_sum);
        mDateValue = view.findViewById(R.id.trans_detail_value_date);
        mCategoryValue = view.findViewById(R.id.trans_detail_value_cat);

        if (exp != null) {
            mTitleValue.setText(exp.getExpTitle());
            mAmountValue.setText(String.valueOf(exp.getExpCost()));
            mDateValue.setText(MainActivity.prettify(exp.getExpDate()));
            mCategoryValue.setText(exp.getExpCat());
        } else if (inc != null){
            mTitleValue.setText(inc.getIncTitle());
            mAmountValue.setText(String.valueOf(inc.getIncAmount()));
            mDateValue.setText(MainActivity.prettify(inc.getIncDate()));
            mCategoryValue.setText(inc.getIncCat());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(this.toString(), "ON SAVE INSTANCE");
        super.onSaveInstanceState(outState);
        outState.putInt(TYPE, mType);
        if (mExp != null) {
            outState.putSerializable(TRANSFER_OBJECT, mExp);
        } else {
            outState.putSerializable(TRANSFER_OBJECT, mInc);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(this.toString(), "ON START");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(this.toString(), "ON RESUME");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(this.toString(), "ON PAUSE");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(this.toString(), "ON STOP");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(this.toString(), "ON DETACH");
    }

}
