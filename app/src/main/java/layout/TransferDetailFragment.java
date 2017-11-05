package layout;

import android.content.DialogInterface;
import android.os.Bundle;
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
 * Simple fragment that shows details of a transferand allows user to delete it.
 * A simple {@link Fragment} subclass.
 */
public class TransferDetailFragment extends BaseFragment implements View.OnClickListener {
    private static final String TYPE = "detail_type";
    private static final String TRANSFER_OBJECT = "transfer_object";

    private Expenditure mExp;
    private Income mInc;
    private int mType;
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
        TextView titleValue = view.findViewById(R.id.trans_detail_value_title);
        TextView amountValue = view.findViewById(R.id.trans_detail_value_sum);
        TextView dateValue = view.findViewById(R.id.trans_detail_value_date);
        TextView categoryValue = view.findViewById(R.id.trans_detail_value_cat);
        mDeleteButton = view.findViewById(R.id.transfer_detail_delete_button);
        mDeleteButton.setOnClickListener(this);

        if (exp != null && mType == EXPENDITURES) {
            titleValue.setText(exp.getExpTitle());
            String amt = "-" + String.valueOf(exp.getExpCost());
            amountValue.setText(amt);
            dateValue.setText(MainActivity.prettify(exp.getExpDate()));
            categoryValue.setText(exp.getExpCat());
            amountValue.setTextColor(ContextCompat.getColor(getContext(), R.color.expenditureColor));

        } else if (inc != null && mType == INCOME){
            titleValue.setText(inc.getIncTitle());
            amountValue.setText(String.valueOf(inc.getIncAmount()));
            dateValue.setText(MainActivity.prettify(inc.getIncDate()));
            categoryValue.setText(inc.getIncCat());
            amountValue.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccentGreen));
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
        builder.setMessage(R.string.confirm_delete_transfer)
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
