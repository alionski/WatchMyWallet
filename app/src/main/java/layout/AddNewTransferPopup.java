package layout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import se.mah.aliona.watchmywallet.R;

/**
 * Custom dialog fragment which asks the user if they want to add a new income or expenditure.
 * Launched after the user cliclks on the floating button in TransfersFragment.
 * Created by aliona on 2017-10-07.
 */

public class AddNewTransferPopup extends DialogFragment {
    private Callback callback;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_transfer_dialog_message)
                .setItems(R.array.add_new_transfer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onResult(which);
                    }
                });
        return builder.create();
    }

    public void setCallback(AddNewTransferPopup.Callback callback) {
        this.callback = callback;
    }

    public static AddNewTransferPopup newInstance(AddNewTransferPopup.Callback callback) {
        AddNewTransferPopup f = new AddNewTransferPopup();
        f.setCallback(callback);
        return f;
    }

    public interface Callback {
        void onResult(int result);
    }
}

