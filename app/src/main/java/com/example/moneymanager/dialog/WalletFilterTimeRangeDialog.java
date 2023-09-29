package com.example.moneymanager.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.moneymanager.R;
import com.example.moneymanager.activities.WalletFilterTimeActivity;
import com.example.moneymanager.methods.SharedMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WalletFilterTimeRangeDialog extends AppCompatDialogFragment {
    TextView tv_after, tv_before;
    DatePickerDialog.OnDateSetListener tv_add_before_date_listener, tv_add_after_date_listener;

    private void createDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener){
        Calendar calendar = Calendar.getInstance();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, listener, currentYear, currentMonth, currentDayOfMonth);
        datePickerDialog.show();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_time_range, null);
        tv_after = view.findViewById(R.id.wallet_filter_time_range_after);
        tv_before = view.findViewById(R.id.wallet_filter_time_range_before);
        tv_before.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createDatePickerDialog(getActivity(), tv_add_before_date_listener = new DatePickerDialog.OnDateSetListener() {
                    //Hàm này chạy khi ấn OK, trả lại year, month, dayOfMonth user chọn
                    //Format lại date và hiển thị ngày lên cho user
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tv_before.setText(SharedMethods.formatDate(year, month, dayOfMonth));
                    }
                });
            }
        });
        tv_after.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createDatePickerDialog(getActivity(), tv_add_after_date_listener = new DatePickerDialog.OnDateSetListener() {
                    //Hàm này chạy khi ấn OK, trả lại year, month, dayOfMonth user chọn
                    //Format lại date và hiển thị ngày lên cho user
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tv_after.setText(SharedMethods.formatDate(year, month, dayOfMonth));
                    }
                });
            }
        });
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
                    String after = tv_after.getText().toString();
                    String before = tv_before.getText().toString();
                    Date dAfter = null, dBefore = null;
                    try {
                        dAfter = new SimpleDateFormat("dd/MM/yyyy").parse(after);
                        dBefore = new SimpleDateFormat("dd/MM/yyyy").parse(before);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (!dAfter.after(dBefore)) {
                        isClose = true;
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(WalletFilterTimeActivity.AFTER_TIME_EXTRA_KEY, after);
                        returnIntent.putExtra(WalletFilterTimeActivity.BEFORE_TIME_EXTRA_KEY, before);
                        getActivity().setResult(WalletFilterTimeActivity.IN_RANGE_TIME_RESULT_CODE, returnIntent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Thời gian bắt đầu phải trước thời gian kết thúc!", Toast.LENGTH_LONG).show();
                    }
                    if (isClose) {
                        d.dismiss();
                    }
                }
            });
        }
    }
}
