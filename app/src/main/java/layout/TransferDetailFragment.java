package layout;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
public class TransferDetailFragment extends BaseFragment implements View.OnClickListener {
    private static final String TYPE = "detail_type";
    private static final String TRANSFER_OBJECT = "transfer_object";

    private Expenditure mExp;
    private Income mInc;

    private int mType;

    private CoordinatorLayout mLayout;
    private TextView mTitleValue;
    private TextView mAmountValue;
    private TextView mDateValue;
    private TextView mCategoryValue;
    private FloatingActionButton mDeleteButton;


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

    protected void restoreState(Bundle savedInstanceState) {
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
        mDeleteButton = view.findViewById(R.id.transfer_detail_delete_button);
        mDeleteButton.setOnClickListener(this);
        // TODO: is it actually lookig cool?..
        mLayout = view.findViewById(R.id.transfer_detail_coordinator_layout);
//        mLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGray));

        if (exp != null && mType == EXPENDITURES) {
            mTitleValue.setText(exp.getExpTitle());
            mAmountValue.setText("-" + String.valueOf(exp.getExpCost()));
            mDateValue.setText(MainActivity.prettify(exp.getExpDate()));
            mCategoryValue.setText(exp.getExpCat());

//            mTitleValue.setTextColor(ContextCompat.getColor(getContext(), R.color.expenditureColor));
            mAmountValue.setTextColor(ContextCompat.getColor(getContext(), R.color.expenditureColor));
//            mDateValue.setTextColor(ContextCompat.getColor(getContext(), R.color.expenditureColor));
//            mCategoryValue.setTextColor(ContextCompat.getColor(getContext(), R.color.expenditureColor));

//            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_mood_bad_grey_300_24dp);
//            BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(),bitmap);
//            drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//            mLayout.setBackground(drawable);

        } else if (inc != null && mType == INCOME){
            mTitleValue.setText(inc.getIncTitle());
            mAmountValue.setText(String.valueOf(inc.getIncAmount()));
            mDateValue.setText(MainActivity.prettify(inc.getIncDate()));
            mCategoryValue.setText(inc.getIncCat());

//            mTitleValue.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccentGreen));
            mAmountValue.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccentGreen));
//            mDateValue.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccentGreen));
//            mCategoryValue.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccentGreen));
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
    public void onClick(View view) {
        if (view == mDeleteButton) {
            showConfirmationDialog();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Delete this transfer?")
                .setCancelable(false)
                .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (mType == EXPENDITURES) {
                            mMainActivity.deleteTransfer(EXPENDITURES, mExp.getExpID());
                        } else if (mType == INCOME) {
                            mMainActivity.deleteTransfer(INCOME, mInc.getIncID());
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
