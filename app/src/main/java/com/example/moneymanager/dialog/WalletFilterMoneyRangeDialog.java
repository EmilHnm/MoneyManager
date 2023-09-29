package com.example.moneymanager.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.moneymanager.R;
import com.example.moneymanager.activities.WalletFilterMoneyActivity;
import com.example.moneymanager.methods.SharedMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WalletFilterMoneyRangeDialog extends AppCompatDialogFragment {
    EditText et_more_than, et_less_than;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_money_range, null);
        et_more_than = view.findViewById(R.id.wallet_filter_money_range_et_more_than);
        et_less_than = view.findViewById(R.id.wallet_filter_money_range_et_less_than);

        builder.setView(view)
                .setTitle("Chọn khoảng thời gian")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d!=null) {
            Button neutralButton = (Button) d.getButton(Dialog.BUTTON_NEUTRAL);

            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean isClose = false;
                    String moreThan = et_more_than.getText().toString();
                    String lessThan = et_less_than.getText().toString();

                    if (Integer.parseInt(moreThan) < Integer.parseInt(lessThan)) {
                        isClose = true;
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(WalletFilterMoneyActivity.MORE_THAN_MONEY_EXTRA_KEY, moreThan);
                        returnIntent.putExtra(WalletFilterMoneyActivity.LESS_THAN_MONEY_EXTRA_KEY, lessThan);
                        getActivity().setResult(WalletFilterMoneyActivity.IN_RANGE_MONEY_RESULT_CODE, returnIntent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Khoảng tiền nhập không hợp lệ!", Toast.LENGTH_LONG).show();
                    }
                    if (isClose) {
                        d.dismiss();
                    }
                }
            });
        }
    }
}
