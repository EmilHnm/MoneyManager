package com.example.moneymanager.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymanager.R;
import com.example.moneymanager.dialog.WalletFilterTimeRangeDialog;
import com.example.moneymanager.methods.SharedMethods;

import java.util.Calendar;

public class WalletFilterTimeActivity extends AppCompatActivity {
    //Mã dialog
    //Dialog In_Range_Time viết riêng do phải check thoả mãn điều kiện ngày
    private static final int BEFORE_TIME_DIALOG_ID = 1;
    private static final int AFTER_TIME_DIALOG_ID = 2;
    //private static final int IN_RANGE_TIME_DIALOG_ID = 3;
    private static final int EXACT_TIME_DIALOG_ID = 4;

    //Mã result trả về cho WalletFilterActivity
    public static final int ALL_TIME_RESULT_CODE = 5;
    public static final int BEFORE_TIME_RESULT_CODE = 6;
    public static final int AFTER_TIME_RESULT_CODE = 7;
    public static final int IN_RANGE_TIME_RESULT_CODE = 8;
    public static final int EXACT_TIME_RESULT_CODE = 9;

    //Key extra trả về cho WalletFilterActivity
    public static final String BEFORE_TIME_EXTRA_KEY = "before";
    public static final String AFTER_TIME_EXTRA_KEY = "after";
    public static final String EXACT_TIME_EXTRA_KEY = "exact";


    TextView tv_all, tv_after, tv_before, tv_in_range, tv_exact;
    ImageButton ib_back;


    DatePickerDialog.OnDateSetListener tv_add_before_date_listener, tv_add_after_date_listener, tv_add_at_date_listener;

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        Dialog dialog = null;
        switch(id) {
            case BEFORE_TIME_DIALOG_ID :
                dialog = BeforeTimeDialog();
                break;
            case AFTER_TIME_DIALOG_ID :
                dialog = AfterTimeDialog();
                break;
//            case IN_RANGE_TIME_DIALOG_ID :
//                dialog = InRangeTimeDialog();
//                break;
            case EXACT_TIME_DIALOG_ID :
                dialog = ExactTimeDialog();
                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
    }

    private void createDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener){
        Calendar calendar = Calendar.getInstance();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, listener, currentYear, currentMonth, currentDayOfMonth);
        datePickerDialog.show();
    }

    private Dialog AfterTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_time, null);
        TextView tv_title = view.findViewById(R.id.wallet_filter_time_tv_title);
        TextView tv_after_time = view.findViewById(R.id.wallet_filter_time_tv_time);

        tv_title.setText("Sau");
        tv_after_time.setText("Nhấn để chọn");

        tv_after_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createDatePickerDialog(WalletFilterTimeActivity.this, tv_add_after_date_listener = new DatePickerDialog.OnDateSetListener() {
                    //Hàm này chạy khi ấn OK, trả lại year, month, dayOfMonth user chọn
                    //Gọi lại hàm displayDateToTextView() để hiển thị ngày lên cho user
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tv_after_time.setText(SharedMethods.formatDate(year, month, dayOfMonth));
                    }
                });
            }
        });
        builder.setView(view)
                .setTitle("Chọn thời gian")
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(AFTER_TIME_EXTRA_KEY, tv_after_time.getText().toString());
                        setResult(AFTER_TIME_RESULT_CODE, returnIntent);
                        finish();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    private Dialog BeforeTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_time, null);
        TextView tv_title = view.findViewById(R.id.wallet_filter_time_tv_title);
        TextView tv_before_time = view.findViewById(R.id.wallet_filter_time_tv_time);

        tv_title.setText("Trước");
        tv_before_time.setText("Nhấn để chọn");

        tv_before_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createDatePickerDialog(WalletFilterTimeActivity.this, tv_add_before_date_listener = new DatePickerDialog.OnDateSetListener() {
                    //Hàm này chạy khi ấn OK, trả lại year, month, dayOfMonth user chọn
                    //Gọi lại hàm displayDateToTextView() để hiển thị ngày lên cho user
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tv_before_time.setText(SharedMethods.formatDate(year, month, dayOfMonth));
                    }
                });
            }
        });

        builder.setView(view)
                .setTitle("Chọn thời gian")
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(BEFORE_TIME_EXTRA_KEY, tv_before_time.getText().toString());
                        setResult(BEFORE_TIME_RESULT_CODE, returnIntent);
                        finish();

                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    private Dialog ExactTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_time, null);
        TextView tv_title = view.findViewById(R.id.wallet_filter_time_tv_title);
        TextView tv_exact_time = view.findViewById(R.id.wallet_filter_time_tv_time);

        tv_title.setText("Chính xác");
        tv_exact_time.setText("Nhấn để chọn");

        tv_exact_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createDatePickerDialog(WalletFilterTimeActivity.this, tv_add_at_date_listener = new DatePickerDialog.OnDateSetListener() {
                    //Hàm này chạy khi ấn OK, trả lại year, month, dayOfMonth user chọn
                    //Gọi lại hàm displayDateToTextView() để hiển thị ngày lên cho user
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tv_exact_time.setText(SharedMethods.formatDate(year, month, dayOfMonth));
                    }
                });
            }
        });
        builder.setView(view)
                .setTitle("Chọn thời gian")
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(EXACT_TIME_EXTRA_KEY, tv_exact_time.getText().toString());
                        setResult(EXACT_TIME_RESULT_CODE, returnIntent);
                        finish();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
    private void getViews() {
        tv_all = findViewById(R.id.wallet_filter_time_tv_all);
        tv_after = findViewById(R.id.wallet_filter_time_tv_after);
        tv_before = findViewById(R.id.wallet_filter_time_tv_before);
        tv_in_range = findViewById(R.id.wallet_filter_time_tv_in_range);
        tv_exact = findViewById(R.id.wallet_filter_time_tv_exact);
        ib_back = findViewById(R.id.wallet_filter_time_ib_back);
    }
    private void setEventListener() {
        tv_all.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(ALL_TIME_RESULT_CODE, returnIntent);
                finish();
            }
        });
        tv_after.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(AFTER_TIME_DIALOG_ID);
            }
        });
        tv_before.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(BEFORE_TIME_DIALOG_ID);
            }
        });
        tv_in_range.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                WalletFilterTimeRangeDialog walletFilterChooseTimeRangeRangeDialog = new WalletFilterTimeRangeDialog();
                walletFilterChooseTimeRangeRangeDialog.show(getSupportFragmentManager(),null);
            }
        });
        tv_exact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(EXACT_TIME_DIALOG_ID);
            }
        });
        ib_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 finish();
            }
        });
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_filter_time);
        getViews();
        setEventListener();
    }
}
